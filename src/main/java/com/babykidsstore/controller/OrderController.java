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
            HttpSession session,
            Model model) {

        String orderId = "BBL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // సెషన్‌లో డేటా సేవ్ చేస్తున్నాం
        session.setAttribute("lastOrderId", orderId);
        session.setAttribute("customerName", fullName);
        session.setAttribute("deliveryAddress", address + ", " + pincode);
        session.setAttribute("orderStatus", "PLACED");

        model.addAttribute("orderId", orderId);
        model.addAttribute("customerName", fullName);
        model.addAttribute("deliveryAddress", address + ", " + pincode);

        // templates/orders/order-success.html కి వెళ్తుంది
        return "orders/order-success";
    }

    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam String orderId, HttpSession session) {
        session.setAttribute("orderStatus", "CANCELLED");
        return "redirect:/order/history";
    }

    @GetMapping("/order/track")
    public String trackOrder(@RequestParam String orderId, HttpSession session, Model model) {
        String currentStatus = (String) session.getAttribute("orderStatus");

        model.addAttribute("orderId", orderId);
        model.addAttribute("status", currentStatus != null ? currentStatus : "PLACED");

        // templates/orders/track-order.html కి వెళ్తుంది
        return "orders/track-order";
    }

    @GetMapping("/order/history")
    public String viewOrderHistory(HttpSession session, Model model) {
        String orderId = (String) session.getAttribute("lastOrderId");

        if (orderId != null) {
            model.addAttribute("orderId", orderId);
            model.addAttribute("customerName", session.getAttribute("customerName"));
            model.addAttribute("deliveryAddress", session.getAttribute("deliveryAddress"));
            model.addAttribute("status", session.getAttribute("orderStatus"));
        }

        // templates/orders/order-history.html కి వెళ్తుంది
        return "orders/order-history";
    }

    @GetMapping("/order/return")
    public String processReturn(@RequestParam String orderId, HttpSession session) {
        session.setAttribute("orderStatus", "RETURN_INITIATED");
        return "redirect:/order/history";
    }

    @GetMapping("/order/exchange")
    public String processExchange(@RequestParam String orderId, HttpSession session) {
        session.setAttribute("orderStatus", "EXCHANGE_INITIATED");
        return "redirect:/order/history";
    }
}