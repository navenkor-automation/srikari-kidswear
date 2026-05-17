package com.babykidsstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class OrderController {

    @PostMapping("/order/confirm")
    public String confirmOrder(
            @RequestParam String fullName,
            @RequestParam String address,
            @RequestParam String phone,
            @RequestParam String pincode,
            HttpSession session, // Make sure this is present
            Model model) {

        String orderId = "BBL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // CRITICAL: Save to session
        session.setAttribute("lastOrderId", orderId);
        session.setAttribute("customerName", fullName);
        session.setAttribute("deliveryAddress", address + ", " + pincode);
        session.setAttribute("orderStatus", "PLACED");

        model.addAttribute("orderId", orderId);
        model.addAttribute("customerName", fullName);
        model.addAttribute("deliveryAddress", address + ", " + pincode);

        return "order-success";
    }
     // Add these to your existing OrderController.java

    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam String orderId, HttpSession session, Model model) {
        // Update the status in the session memory
        session.setAttribute("orderStatus", "CANCELLED");

        // Refresh the history page to show the updated status
        return "redirect:/order/history";
    }
    @GetMapping("/order/track")
    public String trackOrder(@RequestParam String orderId, HttpSession session, Model model) {
        // Get the current status from the session (or default to PLACED)
        String currentStatus = (String) session.getAttribute("orderStatus");

        model.addAttribute("orderId", orderId);
        model.addAttribute("status", currentStatus != null ? currentStatus : "PLACED");

        return "track-order";
    }

    @PostMapping("/order/return")
    public String returnOrder(@RequestParam String orderId, @RequestParam String reason) {
        // Logic: Update status to 'RETURN_REQUESTED'
        return "redirect:/order/history";
    }

    @GetMapping("/order/history")
    public String viewOrderHistory(HttpSession session, Model model) {
        // Pull from session
        String orderId = (String) session.getAttribute("lastOrderId");

        // TEST OVERRIDE: Remove or comment this line after testing
        session.setAttribute("orderStatus", "DELIVERED");

        if (orderId != null) {
            model.addAttribute("orderId", orderId);
            model.addAttribute("customerName", session.getAttribute("customerName"));
            model.addAttribute("deliveryAddress", session.getAttribute("deliveryAddress"));

            // This must be "DELIVERED" for the Return/Exchange buttons to appear
            model.addAttribute("status", session.getAttribute("orderStatus"));
        }

        return "order-history";
    }
    // Handle Return Request
    @GetMapping("/order/return")
    public String processReturn(@RequestParam String orderId, HttpSession session) {
        // Updates session memory for your Maven/Jenkins testing environment
        session.setAttribute("orderStatus", "RETURN_INITIATED");

        // Redirects to refresh the 'My Orders' page with the new status
        return "redirect:/order/history";
    }

    // Handle Exchange Request
    @GetMapping("/order/exchange")
    public String processExchange(@RequestParam String orderId, HttpSession session) {
        session.setAttribute("orderStatus", "EXCHANGE_INITIATED");

        return "redirect:/order/history";
    }
}