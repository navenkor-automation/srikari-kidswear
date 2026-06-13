package com.babykidsstore.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId; // 🎯 FIX: డేటాబేస్ లో ఏ ప్రొడక్టో గుర్తుపట్టడానికి ప్రొడక్ట్ ఐడీ
    private String name;
    private Double price;
    private Integer quantity;
    private String imageUrl;
    private String size;       // Ajio Style: సైజ్ మేనేజ్‌మెంట్
    private Double totalPrice; // UI లో టోటల్ ప్రైస్ డిస్‌ప్లే కోసం

    // 🎯 FIX: లాగిన్ అయిన యూజర్ ఐడీ లేదా పేరుతో కార్ట్ ఐటమ్స్ ని లింక్ చేయడానికి
    private String customerName;

    // Default Constructor
    public CartItem() {
        this.size = "0-6M"; // డిఫాల్ట్ సైజ్
    }

    // Parameterized Constructor
    public CartItem(Long productId, String name, Double price, Integer quantity, String imageUrl, String customerName) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.customerName = customerName;
        this.size = "0-6M";
        this.totalPrice = (price != null && quantity != null) ? price * quantity : 0.0;
    }

    // Helper for UI - Calculates total for this specific item size combo
    public Double getTotalPrice() {
        return (this.price != null && this.quantity != null) ? this.price * this.quantity : 0.0;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    // Ajio Logic: ఒకే ప్రొడక్ట్ వేర్వేరు సైజుల్లో ఉంటే డేటాబేస్/హాష్ మ్యాప్ ఆపరేషన్స్ లో సపరేట్ గా ఉండటానికి
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem item = (CartItem) o;
        return Objects.equals(productId, item.productId) && Objects.equals(size, item.size) && Objects.equals(customerName, item.customerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, size, customerName);
    }
}