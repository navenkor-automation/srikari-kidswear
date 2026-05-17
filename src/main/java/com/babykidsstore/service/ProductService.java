package com.babykidsstore.service;

import com.babykidsstore.repository.ProductRepository;
import com.babykidsstore.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository; // Repository ni inject chey anna

    // Patha ArrayList motham teeseysi idi okkate unchu
    public List<Product> getAllProducts() {
        return productRepository.findAll(); // Database nundi anni testundhi
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }
}