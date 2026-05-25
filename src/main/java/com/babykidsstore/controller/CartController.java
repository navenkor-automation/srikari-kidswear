package com.babykidsstore.controller;

import com.babykidsstore.model.CartItem;
import com.babykidsstore.model.Product;
import com.babykidsstore.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    // 🎯 FIX: LinkedHashMap వాడటం వల్ల మనం యాడ్ చేసే ఆర్డర్ కరెక్ట్ గా ఉంటుంది
    private Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private Map<Long, CartItem> getSavedItems(HttpSession session) {
        Map<Long, CartItem> saved = (Map<Long, CartItem>) session.getAttribute("savedItems");
        if (saved == null) {
            saved = new LinkedHashMap<>();
            session.setAttribute("savedItems", saved);
        }
        return saved;
    }

    // 🎯 ADD TO CART (కొత్త ప్రొడక్ట్ అందరికంటే పైన రావడానికి లాజిక్)
    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<String> addToCart(@RequestParam Long id, @RequestParam(required = false, defaultValue = "0-6M") String size, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        CartItem itemToOrder;

        if (cart.containsKey(id)) {
            // ఒకవేళ ఆల్రెడీ కార్ట్ లో ఉంటే, దాన్ని తీసి క్వాంటిటీ పెంచి మళ్ళీ ఫస్ట్ లో యాడ్ చేస్తాం
            itemToOrder = cart.remove(id);
            itemToOrder.setQuantity(itemToOrder.getQuantity() + 1);
            if (size != null && !size.isEmpty()) {
                itemToOrder.setSize(size);
            }
        } else {
            // కొత్త ప్రొడక్ట్ అయితే డేటాబేస్ నుండి తెచ్చి క్రియేట్ చేస్తాం
            Product product = productRepository.findById(id).orElse(null);
            if (product != null) {
                itemToOrder = new CartItem(
                        id,
                        product.getName(),
                        product.getPrice(),
                        1,
                        product.getImageUrl()
                );
                itemToOrder.setSize(size);
            } else {
                return ResponseEntity.badRequest().body("Product Not Found");
            }
        }

        // 🎯 MAGIC LOGIC: కొత్తగా యాడ్ చేసిన లేదా అప్‌డేట్ చేసిన ఐటమ్‌ని మ్యాప్‌లో అందరికంటే పైన ఉంచడానికి:
        Map<Long, CartItem> newSortedCart = new LinkedHashMap<>();
        newSortedCart.put(id, itemToOrder); // ఫస్ట్ కరెంట్ ప్రొడక్ట్ పెడుతున్నాం
        newSortedCart.putAll(cart);         // ఆ తర్వాత మిగిలిన పాత ప్రొడక్ట్స్ వస్తాయి

        session.setAttribute("cart", newSortedCart); // సెషన్ ని అప్‌డేట్ చేస్తున్నాం

        return ResponseEntity.ok("Success");
    }

    // 🎯 VIEW CART (రికమండేషన్ ప్రొడక్ట్స్ తో సహా)
    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        Map<Long, CartItem> saved = getSavedItems(session);

        cart.values().forEach(item -> item.setTotalPrice(item.getPrice() * item.getQuantity()));
        saved.values().forEach(item -> item.setTotalPrice(item.getPrice()));

        double total = cart.values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // డిస్కౌంట్ క్యాలిక్యులేషన్
        double discount = 0.0;
        String appliedCoupon = (String) session.getAttribute("appliedCoupon");
        if ("BABY10".equals(appliedCoupon)) {
            discount = total * 0.10;
        } else if ("FREE50".equals(appliedCoupon)) {
            discount = total > 50 ? 50.0 : total;
        }

        double finalTotal = total - discount;

        // 🎯 NEW FEATURE: "You May Also Like" కోసం డేటాబేస్ నుండి టాప్ 4 ప్రొడక్ట్స్ పంపుతున్నాం
        List<Product> recommendedProducts = productRepository.findAll();
        if (recommendedProducts.size() > 4) {
            recommendedProducts = recommendedProducts.subList(0, 4); // మొదటి 4 ప్రొడక్ట్స్ రికమండేషన్స్ గా చూపిస్తాం
        }

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("savedItems", saved.values());
        model.addAttribute("recommendations", recommendedProducts); // HTML కి పంపుతున్నాం అన్నా
        model.addAttribute("total", total);
        model.addAttribute("discount", discount);
        model.addAttribute("finalTotal", finalTotal);
        model.addAttribute("appliedCoupon", appliedCoupon);
        model.addAttribute("couponMessage", session.getAttribute("couponMessage"));
        model.addAttribute("cartCount", cart.values().stream().mapToInt(CartItem::getQuantity).sum());

        session.removeAttribute("couponMessage");

        return "cart-checkout/cart";
    }

    @PostMapping("/cart/apply-coupon")
    public String applyCoupon(@RequestParam String couponCode, HttpSession session) {
        if ("BABY10".equalsIgnoreCase(couponCode)) {
            session.setAttribute("appliedCoupon", "BABY10");
            session.setAttribute("couponMessage", "10% Coupon Applied Successfully!");
        } else if ("FREE50".equalsIgnoreCase(couponCode)) {
            session.setAttribute("appliedCoupon", "FREE50");
            session.setAttribute("couponMessage", "₹50 Flat Discount Applied!");
        } else {
            session.setAttribute("couponMessage", "Invalid Coupon Code!");
            session.removeAttribute("appliedCoupon");
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/save-for-later")
    public String saveForLater(@RequestParam Long id, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        Map<Long, CartItem> saved = getSavedItems(session);
        if (cart.containsKey(id)) {
            saved.put(id, cart.remove(id));
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/move-to-bag")
    public String moveToBag(@RequestParam Long id, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        Map<Long, CartItem> saved = getSavedItems(session);
        if (saved.containsKey(id)) {
            CartItem item = saved.remove(id);
            // బ్యాక్ కి మూవ్ చేసినప్పుడు కూడా అది అందరికంటే పైన రావాలి కాబసట్టి:
            Map<Long, CartItem> newSortedCart = new LinkedHashMap<>();
            newSortedCart.put(id, item);
            newSortedCart.putAll(cart);
            session.setAttribute("cart", newSortedCart);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long id, @RequestParam String action, @RequestParam(required = false) String size, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        if (cart.containsKey(id)) {
            CartItem item = cart.get(id);
            if ("increase".equals(action)) {
                item.setQuantity(item.getQuantity() + 1);
            } else if ("decrease".equals(action) && item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            }
            if (size != null && !size.isEmpty()) {
                item.setSize(size);
            }
            item.setTotalPrice(item.getPrice() * item.getQuantity());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long id, HttpSession session) {
        getCart(session).remove(id);
        return "redirect:/cart";
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/cart/remove-saved")
    public String removeSaved(@RequestParam Long id, HttpSession session) {
        getSavedItems(session).remove(id);
        return "redirect:/cart";
    }
}