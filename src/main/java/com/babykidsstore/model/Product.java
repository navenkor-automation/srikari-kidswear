package com.babykidsstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private double price;
    private String description;
    private String imageUrl; // ఇమేజ్ పాత్ స్టోర్ చేసే వేరియబుల్
    private int quantity;
    private String size;

    // 🔥 NULL values ని సేఫ్ గా హ్యాండిల్ చేయడానికి primitive boolean నుండి Wrapper Boolean కి మార్చాను anna
    @Column(name = "new_arrival")
    private Boolean newArrival = false;

    private String sizes;
    private String sizeChartUrl;
}