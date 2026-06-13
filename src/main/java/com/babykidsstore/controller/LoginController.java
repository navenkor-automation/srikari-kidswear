package com.babykidsstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;

@Controller
public class LoginController {

    private static final String STRICT_EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final Set<String> ALLOWED_DOMAINS = Set.of(
            "gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "icloud.com", "zoho.com",
            "gmail.co.in", "yahoo.co.in", "rediffmail.com", "live.com"
    );

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("step", "mobile");
        return "user-loginpage/user-login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam Map<String, String> allParams, Model model, HttpSession session) {
        String formStep = allParams.get("formStep");
        String mobileNumber = allParams.get("mobileNumber");

        // --- STEP 1: MOBILE VALIDATION ---
        if ("SUBMIT_MOBILE".equals(formStep)) {
            if (mobileNumber == null || !mobileNumber.matches("^[0-9]{10}$")) {
                model.addAttribute("error", "Please enter a valid 10-digit mobile number.");
                model.addAttribute("step", "mobile");
                return "user-loginpage/user-login";
            }
            model.addAttribute("mobileNumber", mobileNumber);
            model.addAttribute("step", "otp");
            return "user-loginpage/user-login";
        }

        // --- STEP 2: OTP VALIDATION ---
        if ("SUBMIT_OTP".equals(formStep)) {
            String otp = allParams.get("otp");
            if (otp == null || !"1234".equals(otp.trim())) {
                model.addAttribute("error", "Invalid OTP. Please enter 1234 to clear verification.");
                model.addAttribute("mobileNumber", mobileNumber);
                model.addAttribute("step", "otp");
                return "user-loginpage/user-login";
            }

            // EXISTING USER (Ends with 60) -> Save to Session & Redirect to Shop Home
            if (mobileNumber != null && mobileNumber.endsWith("60")) {
                session.setAttribute("loggedInUser", "Premium Customer");
                session.setAttribute("userMobile", mobileNumber);
                session.setAttribute("userEmail", "customer@gmail.com");
                session.setAttribute("userGender", "Male");
                return "redirect:/shop";
            } else {
                model.addAttribute("mobileNumber", mobileNumber);
                model.addAttribute("step", "register");
                return "user-loginpage/user-login";
            }
        }

        // --- STEP 3: REGISTRATION VALIDATION ---
        if ("SUBMIT_REGISTER".equals(formStep)) {
            String fullName = allParams.get("fullName");
            String email = allParams.get("email");
            String gender = allParams.get("gender");

            if (fullName == null || fullName.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    gender == null || gender.trim().isEmpty()) {

                model.addAttribute("error", "Please fill up all profile fields.");
                retainFormFields(model, mobileNumber, fullName, email, gender);
                return "user-loginpage/user-login";
            }

            String cleanEmail = email.trim().toLowerCase();

            if (!cleanEmail.matches(STRICT_EMAIL_REGEX)) {
                model.addAttribute("error", "Invalid email format. Please check your email entry.");
                retainFormFields(model, mobileNumber, fullName, email, gender);
                return "user-loginpage/user-login";
            }

            String domain = cleanEmail.substring(cleanEmail.indexOf("@") + 1);
            if (!ALLOWED_DOMAINS.contains(domain)) {
                model.addAttribute("error", "Registration is only allowed with trusted email providers.");
                retainFormFields(model, mobileNumber, fullName, email, gender);
                return "user-loginpage/user-login";
            }

            // NEW USER -> Save details to Session
            session.setAttribute("loggedInUser", fullName);
            session.setAttribute("userMobile", mobileNumber);
            session.setAttribute("userEmail", cleanEmail);
            session.setAttribute("userGender", gender);

            return "redirect:/shop";
        }

        model.addAttribute("step", "mobile");
        return "user-loginpage/user-login";
    }

    @GetMapping("/account")
    public String showAccountPage(HttpSession session, Model model) {
        String user = (String) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("userFullName", session.getAttribute("loggedInUser"));
        model.addAttribute("userMobile", session.getAttribute("userMobile"));
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("userGender", session.getAttribute("userGender"));

        return "user-loginpage/user-account";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/shop";
    }

    private void retainFormFields(Model model, String mobile, String name, String email, String gender) {
        model.addAttribute("mobileNumber", mobile);
        model.addAttribute("fullName", name);
        model.addAttribute("email", email);
        model.addAttribute("gender", gender);
        model.addAttribute("step", "register");
    }
}