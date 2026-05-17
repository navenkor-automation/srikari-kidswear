package com.babykidsstore.model;

import java.util.Objects;

public class CartItem {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private String imageUrl;

    // Default Constructor
    public CartItem() {}

    // Parameterized Constructor
    public CartItem(Long id, String name, Double price, Integer quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Helper for UI - calculates total for this specific item
    public Double getTotalPrice() {
        return (this.price != null && this.quantity != null) ? this.price * this.quantity : 0.0;
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

    // Critical for HashMap operations in your Hybrid Framework
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem item = (CartItem) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}