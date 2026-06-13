package com.babykidsstore.repository;

import com.babykidsstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 🎯 Ajio Logic: లాగిన్ అయిన యూజర్ యొక్క కార్ట్ ఐటమ్స్ మాత్రమే లిస్ట్ చేయడానికి
    List<CartItem> findByCustomerName(String customerName);

    // 🎯 Multi-Size Check: డేటాబేస్ లో ఒకే యూజర్ కి, ఒకే ప్రొడక్ట్ ఐడీ మరియు సేమ్ సైజ్ తో ఆల్రెడీ ఐటమ్ ఉందో లేదో చెక్ చేయడానికి
    Optional<CartItem> findByProductIdAndSizeAndCustomerName(Long productId, String size, String customerName);

    // 🎯 Clear Cart Logic: ఆర్డర్ సక్సెస్ అయ్యాక డేటాబేస్ లో ఆ యూజర్ కార్ట్ ఐటమ్స్ ని డిలీట్ చేయడానికి
    @Transactional
    void deleteByCustomerName(String customerName);
}