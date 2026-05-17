package com.babykidsstore;

import com.babykidsstore.model.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("cartCount")
    public int getCartCount(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            return 0;
        }
        return cart.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}