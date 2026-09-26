package com.babykidsstore.controller;

import com.babykidsstore.repository.ProductRepository;
import com.babykidsstore.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.*;

@Controller
public class AdminController {

    @Autowired
    private ProductRepository repository;

    @GetMapping("/login-page")
    public String loginPage() {
        return "admin/admin-login";
    }

    @GetMapping("/login-submit")
    public String loginSubmit(@RequestParam("user") String user, @RequestParam("pass") String pass, HttpSession session) {
        if ("admin".equalsIgnoreCase(user) && "123".equals(pass)) {
            session.setAttribute("isLoggedIn", true);
            return "redirect:/admin/inventory";
        }
        return "redirect:/login-page?error=true";
    }

    // 3. Add or Update Product (Updated for New Arrival Feature)
    @PostMapping("/admin/add")
    public String addOrUpdateProduct(@RequestParam(value = "id", required = false) Long id,
                                     @RequestParam("name") String name,
                                     @RequestParam("price") Double price,
                                     @RequestParam("qty") Integer qty,
                                     @RequestParam("cat") String cat,
                                     // చెక్‌బాక్స్ వాల్యూ ని రీడ్ చేయడం (టిక్ పెట్టకపోతే డిఫాల్ట్ గా false వస్తుంది)
                                     @RequestParam(value = "newArrival", defaultValue = "false") boolean newArrival,
                                     @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                     HttpSession session) {

        if (session.getAttribute("isLoggedIn") == null) return "redirect:/login-page";

        Product p;
        if (id != null) {
            p = repository.findById(id).orElse(new Product());
        } else {
            p = new Product();
        }

        p.setName(name);
        p.setPrice(price);
        p.setQuantity(qty);
        p.setCategory(cat);
        p.setNewArrival(newArrival); // డేటాబేస్ కి స్టేటస్ ని సేవ్ చేయడం

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String contentType = imageFile.getContentType();
                if (contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp"))) {

                    String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

                    String projectRoot = System.getProperty("user.dir");
                    String uploadDir = projectRoot + File.separator + "external-uploads" + File.separator;
                    Path uploadPath = Paths.get(uploadDir);

                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    try (InputStream inputStream = imageFile.getInputStream()) {
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                        p.setImageUrl("/uploads/" + fileName);
                    }
                } else {
                    if (p.getImageUrl() == null || p.getImageUrl().isEmpty()) {
                        p.setImageUrl("/uploads/default.png");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/admin/inventory?error=upload_failed";
            }
        } else {
            if (p.getImageUrl() == null || p.getImageUrl().isEmpty()) {
                p.setImageUrl("/uploads/default.png");
            }
        }

        repository.save(p);
        return "redirect:/admin/inventory";
    }

    @GetMapping("/admin/clear")
    public String clearDatabase(HttpSession session) {
        if (session.getAttribute("isLoggedIn") == null) return "redirect:/login-page";
        repository.deleteAll();
        return "redirect:/admin/inventory";
    }

    @GetMapping("/admin/inventory")
    public String showAllProducts(@RequestParam(defaultValue = "0") int page, Model model, HttpSession session) {
        if (session.getAttribute("isLoggedIn") == null) {
            return "redirect:/login-page";
        }

        int pageSize = 5;
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, pageSize);
        org.springframework.data.domain.Page<Product> productPage = repository.findAll(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "admin/admin-products";
    }

    @GetMapping("/admin/delete")
    public String deleteProduct(@RequestParam("id") Long id, HttpSession session) {
        if (session.getAttribute("isLoggedIn") == null) {
            return "redirect:/login-page";
        }

        try {
            repository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/inventory?error=delete_failed";
        }

        return "redirect:/admin/inventory";
    }
}