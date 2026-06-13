package com.babykidsstore.controller;

import com.babykidsstore.model.CartItem;
import com.babykidsstore.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        // 1. సెషన్ నుండి లాగిన్ అయిన యూజర్ నేమ్ తీసుకుంటున్నాం అన్నా
        String customerName = (String) session.getAttribute("loggedInUser");
        if (customerName == null) {
            return "redirect:/login"; // లాగిన్ అవ్వకపోతే సేఫ్ గా లాగిన్ పేజీకి
        }

        // 2. 🎯 FIX: సెషన్ నుండి కాకుండా నేరుగా డేటాబేస్ నుండి ఈ యూజర్ కార్ట్ ఐటమ్స్ తెచ్చుకుంటున్నాం
        List<CartItem> cartItems = cartItemRepository.findByCustomerName(customerName);

        // ఒకవేళ డేటాబేస్ లో కార్ట్ ఖాళీగా ఉంటే సేఫ్‌గా కార్ట్ పేజీకి రీడైరెక్ట్ చేస్తాం
        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        // 3. ప్రతి ఐటమ్ టోటల్ ప్రైస్ ని క్యాలిక్యులేట్ చేసి సబ్‌టోటల్ చేస్తాం
        double subtotal = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // డెలివరీ రూల్: ₹1000 దాటితే ఫ్రీ షిప్పింగ్, లేదంటే ₹50
        double shipping = subtotal > 1000 ? 0.0 : 50.0;

        // కూపన్ డిస్కౌంట్ కాలిక్యులేషన్ (సెషన్ నుండి కూపన్ కోడ్ చెక్ చేస్తాం)
        double discount = 0.0;
        String appliedCoupon = (String) session.getAttribute("appliedCoupon");
        if ("BABY10".equals(appliedCoupon)) {
            discount = subtotal * 0.10;
        } else if ("FREE50".equals(appliedCoupon)) {
            discount = subtotal > 50 ? 50.0 : subtotal;
        }

        double total = (subtotal - discount) + shipping;

        // checkout.html కి డేటాని మోడల్ ద్వారా పంపుతున్నాం అన్నా
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("discount", discount);
        model.addAttribute("shipping", shipping);
        model.addAttribute("total", total);

        // templates/cart-checkout/checkout.html పాత్ కి రిటర్న్ చేస్తున్నాం
        return "cart-checkout/checkout";
    }
}