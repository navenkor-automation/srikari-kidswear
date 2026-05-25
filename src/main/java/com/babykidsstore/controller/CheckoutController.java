package com.babykidsstore.controller;

import com.babykidsstore.model.CartItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class CheckoutController {

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        // Retrieve the cart from the session
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");

        // If cart is empty, send them back to the cart page
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        // 🎯 FIX: getTotalPrice() లేకపోయినా ఎర్రర్ రాకుండా ఇక్కడే సేఫ్ గా క్యాలిక్యులేట్ చేస్తున్నాం
        double subtotal = cart.values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Logical rule: Free shipping over ₹1000, otherwise ₹50
        double shipping = subtotal > 1000 ? 0.0 : 50.0;
        double total = subtotal + shipping;

        // Add data to the Model for checkout.html
        model.addAttribute("cartItems", cart.values());
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("total", total);

        // 🎯 FIX: ఒకవేళ నీ చెకౌట్ పేజీ కూడా cart-checkout లోపలే ఉంటే "cart-checkout/checkout" అని ఇవ్వాలి.
        // ప్రస్తుతానికి నీ పాత పాత్ ని సేఫ్ గా ఉంచాను. ఎర్రర్ వస్తే దీన్ని "cart-checkout/checkout" కి మార్చుకోవచ్చు.
        //  దీనితో రీప్లేస్ చేయండి
        return "cart-checkout/checkout";
    }
}