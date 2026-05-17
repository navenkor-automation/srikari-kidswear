package com.babykidsstore.controller;
import org.springframework.http.ResponseEntity;
import com.babykidsstore.model.CartItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import com.babykidsstore.model.Product; // Adjust if your Product model path is different
// ProductRepository should already be in com.babykidsstore.repository or similar
import com.babykidsstore.repository.ProductRepository;


@Controller
public class CartController {



    // Helper for active shopping bag
    private Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // Helper for Saved for Later items
    private Map<Long, CartItem> getSavedItems(HttpSession session) {
        Map<Long, CartItem> saved = (Map<Long, CartItem>) session.getAttribute("savedItems");
        if (saved == null) {
            saved = new HashMap<>();
            session.setAttribute("savedItems", saved);
        }
        return saved;
    }

   /* @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long id, @RequestParam String name, @RequestParam Double price, @RequestParam String imageUrl, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        if (cart.containsKey(id)) {
            CartItem item = cart.get(id);
            item.setQuantity(item.getQuantity() + 1);
        } else {
            cart.put(id, new CartItem(id, name, price, 1, imageUrl));
        }
        return "redirect:/shop";
    }*/
   // 1. Ensure this is at the top of your class

    @Autowired
   private ProductRepository productRepository;
    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<String> addToCart(@RequestParam Long id, @RequestParam String size, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);

        if (cart.containsKey(id)) {
            CartItem item = cart.get(id);
            item.setQuantity(item.getQuantity() + 1);
        } else {
            // 2. FETCH REAL DATA HERE
            // Retrieve the product using the ID provided by the AJAX call
            Product product = productRepository.findById(id).orElse(null);

            if (product != null) {
                // Use real data: product.getName(), product.getPrice(), etc.
                cart.put(id, new CartItem(
                        id,
                        product.getName(),
                        product.getPrice(),
                        1,
                        product.getImageUrl()
                ));
            }
        }
        return ResponseEntity.ok("Success");
    }
      @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        Map<Long, CartItem> saved = getSavedItems(session);

        double total = cart.values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("savedItems", saved.values()); // Added to model
        model.addAttribute("total", total);
        return "cart";
    }

    // NEW: Logic to move item from Cart to "Save for Later"
    @PostMapping("/cart/save-for-later")
    public String saveForLater(@RequestParam Long id, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        Map<Long, CartItem> saved = getSavedItems(session);

        if (cart.containsKey(id)) {
            saved.put(id, cart.remove(id));
        }
        return "redirect:/cart";
    }

    // NEW: Logic to move item from "Save for Later" back to Cart
    @PostMapping("/cart/move-to-bag")
    public String moveToBag(@RequestParam Long id, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        Map<Long, CartItem> saved = getSavedItems(session);

        if (saved.containsKey(id)) {
            cart.put(id, saved.remove(id));
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long id, @RequestParam String action, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        if (cart.containsKey(id)) {
            CartItem item = cart.get(id);
            if ("increase".equals(action)) {
                item.setQuantity(item.getQuantity() + 1);
            } else if ("decrease".equals(action) && item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long id, HttpSession session) {
        getCart(session).remove(id);
        return "redirect:/cart";
    }

    // NEW: Remove specifically from the saved list
    @PostMapping("/cart/remove-saved")
    public String removeSaved(@RequestParam Long id, HttpSession session) {
        getSavedItems(session).remove(id);
        return "redirect:/cart";
    }
}