package com.example.catalog.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.catalog.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryIgnoreCase(String category);
}
