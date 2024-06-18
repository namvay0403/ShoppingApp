package com.nam.ShoppingApp.repository;

import com.nam.ShoppingApp.entity.FAQ;
import com.nam.ShoppingApp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
    List<FAQ> findAllByProductId(Long productId);
}
