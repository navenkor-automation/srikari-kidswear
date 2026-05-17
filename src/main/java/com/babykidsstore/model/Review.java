package com.babykidsstore.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // Lombok unte getters/setters automatic ga vasthayi
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private int rating;
    @Column(length = 1000)
    private String comment;
    private String date;
    private Long productId;
}