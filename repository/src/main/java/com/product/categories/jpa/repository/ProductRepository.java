package com.product.categories.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.product.categories.jpa.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
