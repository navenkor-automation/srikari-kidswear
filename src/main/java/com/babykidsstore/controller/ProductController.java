package com.babykidsstore.controller;

import com.babykidsstore.model.Product;
import com.babykidsstore.model.Review;
import com.babykidsstore.repository.ProductRepository;
import com.babykidsstore.repository.ReviewRepository;
import com.babykidsstore.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    // 🔥 Root URL (/) కొట్టినప్పుడు ఆటోమేటిక్‌గా /shop కి పంపడానికి
    @GetMapping("/")
    public String redirectToShop() {
        return "redirect:/shop";
    }

 // A. Shop Page with Category Filter (Updated with Session Check for Ajio Header)
    @GetMapping("/shop")
    public String showShop(@RequestParam(required = false) String category, Model model, HttpSession session) {
        List<Product> products;
        if (category != null && !category.isEmpty()) {
            products = productService.getProductsByCategory(category);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);

        // అడ్మిన్ లో టిక్ పెట్టిన లేటెస్ట్ 4 న్యూ అరైవల్స్
        List<Product> newArrivals = productRepository.findTop4ByNewArrivalTrueOrderByIdDesc();
        model.addAttribute("newArrivals", newArrivals);

        // 🔥 సెషన్‌లో లాగిన్ అయిన యూజర్ పేరు ఉంటే దాన్ని హోమ్ పేజీ మోడల్ కి పంపుతాం అన్నా
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        return "shop-home/shop";
    }
    // B. Product Details Page
    @GetMapping("/product/{id}")
    public String viewProductDetails(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);

            String finalSizes = "";

            if (product.getSizes() != null && !product.getSizes().isEmpty()) {
                finalSizes = product.getSizes();
            }
            else if (product.getSize() != null && !product.getSize().isEmpty()) {
                finalSizes = product.getSize();
            }
            else {
                finalSizes = "0-6M, 6-12M, 1-2Y, 2-3Y";
            }

            String[] sizeArray = finalSizes.split(",");
            model.addAttribute("availableSizes", sizeArray);

            // రివ్యూస్ లోడింగ్
            List<Review> reviews = reviewRepository.findByProductId(id);
            model.addAttribute("reviews", reviews);

            // 🔥 సిమిలర్ ప్రొడక్ట్స్ ని కేస్-ఇన్సెన్సిటివ్ మెథడ్ కి మపాన్ చేశాను anna
            List<Product> similarProducts = productRepository.findByCategoryIgnoreCaseAndIdNot(product.getCategory(), id);
            model.addAttribute("similarProducts", similarProducts);

            return "products/product-details";
        }
        return "redirect:/shop";
    }

    // E. Get Product Details with Reviews via API (Enhanced for Automation/Postman)
    @GetMapping("/api/retrieveProductDetails/{id}")
    @ResponseBody
    public java.util.Map<String, Object> getProductDetailsApi(@PathVariable("id") Long id) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();

        // 1. ప్రొడక్ట్ డేటాను తెచ్చుకోవడం
        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            response.put("product", product);

            // 2. ఆ ప్రొడక్ట్‌కి సంబంధించిన రివ్యూస్ లిస్ట్‌ను కూడా తెచ్చుకోవడం
            List<Review> reviews = reviewRepository.findByProductId(id);
            response.put("reviews", reviews);
        } else {
            response.put("message", "Product not found");
        }

        return response;
    }

    // C. Add Review Logic
    @PostMapping("/api/reviews/add")
    @ResponseBody
    public String addReview(@RequestBody Review review) {
        review.setDate(LocalDate.now().toString());
        reviewRepository.save(review);
        return "Success";
    }

    // D. Get Reviews for a specific product via API (New Endpoint for Automation/Postman)
    @GetMapping("/api/retrieveReviewRating/product/{productId}")
    @ResponseBody
    public List<Review> getReviewsByProduct(@PathVariable("productId") Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}