package com.babykidsstore.model;

import java.util.Objects;

public class CartItem {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private String imageUrl;
    private String size;       // 🎯 FIX: సైజ్ స్టోర్ చేయడానికి కొత్త వేరియబుల్
    private Double totalPrice; // 🎯 FIX: కంట్రోలర్ లో సెట్ చేయడానికి టోటల్ ప్రైస్ వేరియబుల్

    // Default Constructor
    public CartItem() {
        this.size = "0-6M"; // డిఫాల్ట్ సైజ్ సెట్ చేస్తున్నాం అన్నా
    }

    // Parameterized Constructor
    public CartItem(Long id, String name, Double price, Integer quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.size = "0-6M"; // డిఫాల్ట్ సైజ్
        this.totalPrice = (price != null && quantity != null) ? price * quantity : 0.0;
    }

    // Helper for UI - calculates total for this specific item
    public Double getTotalPrice() {
        return (this.price != null && this.quantity != null) ? this.price * this.quantity : 0.0;
    }

    // 🎯 FIX: కంట్రోలర్ నుండి టోటల్ ప్రైస్ ని ఫోర్స్ అప్‌డేట్ చేయడానికి సెట్టర్
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // 🎯 FIX: సైజ్ కి సంబంధించిన గెట్టర్ అండ్ సెట్టర్ యాడ్ చేసాను
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    // Critical for HashMap operations in your Hybrid Framework
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem item = (CartItem) o;
        // 🎯 FIX: ఒకే ప్రొడక్ట్ వేర్వేరు సైజుల్లో ఉంటే హాష్ మ్యాప్ లో సపరేట్ గా ఉండటానికి 'size' ని కూడా ఈక్వల్స్ లో యాడ్ చేసాం
        return Objects.equals(id, item.id) && Objects.equals(size, item.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size);
    }
}