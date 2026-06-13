package com.babykidsstore.controller;

import com.babykidsstore.model.CartItem;
import com.babykidsstore.model.Product;
import com.babykidsstore.repository.CartItemRepository;
import com.babykidsstore.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // 🎯 ADD TO CART (Database Driven & Ajio Style Multi-Size)
    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<String> addToCart(@RequestParam Long id,
                                            @RequestParam(required = false, defaultValue = "0-6M") String size,
                                            HttpSession session) {

        // 1. సెషన్ నుండి లాగిన్ అయిన యూజర్ పేరును తీసుకుంటున్నాం అన్నా
        String customerName = (String) session.getAttribute("loggedInUser");
        if (customerName == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        // 2. డేటాబేస్ లో ఈ యూజర్ కి, ఈ ప్రొడక్ట్ ఐడీ మరియు సేమ్ సైజ్ తో ఆల్రెడీ ఐటమ్ ఉందో లేదో వెతుకుతుంది
        Optional<CartItem> existingItemOpt = cartItemRepository.findByProductIdAndSizeAndCustomerName(id, size, customerName);

        if (existingItemOpt.isPresent()) {
            // ఆల్రెడీ ఉంటే క్వాంటిటీ 1 పెంచి సేవ్ చేస్తాం
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            // లేకపోతే ప్రొడక్ట్ టేబుల్ నుండి మెయిన్ డేటా తెచ్చి కొత్త కార్ట్ రికార్డ్ క్రియేట్ చేస్తాం
            Product product = productRepository.findById(id).orElse(null);
            if (product != null) {
                CartItem newItem = new CartItem(
                        id,
                        product.getName(),
                        product.getPrice(),
                        1,
                        product.getImageUrl(),
                        customerName
                );
                newItem.setSize(size);
                cartItemRepository.save(newItem);
            } else {
                return ResponseEntity.badRequest().body("Product Not Found");
            }
        }
        return ResponseEntity.ok("Success");
    }

    // 🎯 VIEW CART
    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        String customerName = (String) session.getAttribute("loggedInUser");
        if (customerName == null) {
            return "redirect:/login"; // లాగిన్ లేకపోతే సేఫ్ రీడైరెక్ట్
        }

        // 1. డేటాబేస్ నుండి ఈ యూజర్ కార్ట్ ఐటమ్స్ మాత్రమే తెస్తున్నాం
        List<CartItem> dbCartItems = cartItemRepository.findByCustomerName(customerName);

        // 🎯 MAGIC LOGIC: లేటెస్ట్ గా యాడ్ చేసిన ఐటమ్ కార్ట్ లో అందరికంటే పైన (Top) కనిపించడానికి లిస్ట్ రివర్స్ చేస్తున్నాం అన్నా
        List<CartItem> sortedCartItems = new ArrayList<>(dbCartItems);
        Collections.reverse(sortedCartItems);

        // ప్రతి ఐటమ్ టోటల్ ప్రైస్ ని క్యాలిక్యులేట్ సెట్ చేస్తున్నాం
        sortedCartItems.forEach(item -> item.setTotalPrice(item.getPrice() * item.getQuantity()));

        // కార్ట్ టోటల్ క్యాలిక్యులేషన్
        double total = sortedCartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        // కూపన్ కాలిక్యులేషన్స్
        double discount = 0.0;
        String appliedCoupon = (String) session.getAttribute("appliedCoupon");
        if ("BABY10".equals(appliedCoupon)) {
            discount = total * 0.10;
        } else if ("FREE50".equals(appliedCoupon)) {
            discount = total > 50 ? 50.0 : total;
        }

        double finalTotal = total - discount;

        // యు మే ఆల్సో లైక్ (Recommendations)
        List<Product> recommendedProducts = productRepository.findAll();
        if (recommendedProducts.size() > 4) {
            recommendedProducts = recommendedProducts.subList(0, 4);
        }

        // Thymeleaf UI కి డేటా బైండింగ్
        model.addAttribute("cartItems", sortedCartItems);
        model.addAttribute("savedItems", new ArrayList<CartItem>()); // Saved items ని ఫ్యూచర్ లో టేబుల్ బట్టి పెంచుకోవచ్చు
        model.addAttribute("recommendations", recommendedProducts);
        model.addAttribute("total", total);
        model.addAttribute("discount", discount);
        model.addAttribute("finalTotal", finalTotal);
        model.addAttribute("appliedCoupon", appliedCoupon);
        model.addAttribute("couponMessage", session.getAttribute("couponMessage"));
        model.addAttribute("cartCount", sortedCartItems.stream().mapToInt(CartItem::getQuantity).sum());

        session.removeAttribute("couponMessage");
        return "cart-checkout/cart";
    }

    // 🎯 UPDATE QUANTITY
    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long id,
                                 @RequestParam String action,
                                 @RequestParam(required = false, defaultValue = "0-6M") String size,
                                 HttpSession session) {
        String customerName = (String) session.getAttribute("loggedInUser");
        if (customerName != null) {
            Optional<CartItem> itemOpt = cartItemRepository.findByProductIdAndSizeAndCustomerName(id, size, customerName);
            if (itemOpt.isPresent()) {
                CartItem item = itemOpt.get();
                if ("increase".equals(action)) {
                    item.setQuantity(item.getQuantity() + 1);
                } else if ("decrease".equals(action) && item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                }
                cartItemRepository.save(item); // డేటాబేస్ లో అప్‌డేట్ అవుతుంది అన్నా
            }
        }
        return "redirect:/cart";
    }

    // 🎯 REMOVE FROM CART
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long id,
                                 @RequestParam(required = false, defaultValue = "0-6M") String size,
                                 HttpSession session) {
        String customerName = (String) session.getAttribute("loggedInUser");
        if (customerName != null) {
            Optional<CartItem> itemOpt = cartItemRepository.findByProductIdAndSizeAndCustomerName(id, size, customerName);
            itemOpt.ifPresent(cartItem -> cartItemRepository.delete(cartItem));
        }
        return "redirect:/cart";
    }

    // 🎯 APPLY COUPONS
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
}