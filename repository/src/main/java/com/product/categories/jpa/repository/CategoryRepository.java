package com.product.categories.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.product.categories.jpa.entity.Category;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

    @Query(value = "select categories.category_id, categories.category_name, count(products.product_id) as products_count " +
            "from categories left join products on categories.category_id = products.category_id group by categories.category_name;",
            nativeQuery = true)
    List<Category> findAll();

}
