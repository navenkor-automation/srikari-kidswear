package com.babykidsstore.repository;

import com.babykidsstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByNewArrival(boolean newArrival);

    // 🔥 కేటగిరీ మ్యాచింగ్ సేఫ్ గా ఉండటానికి IgnoreCase అప్‌డేట్ చేశాను anna
    List<Product> findByCategoryIgnoreCaseAndIdNot(String category, Long id);

    List<Product> findByCategoryIgnoreCase(String category);
    List<Product> findTop4ByNewArrivalTrueOrderByIdDesc();
}