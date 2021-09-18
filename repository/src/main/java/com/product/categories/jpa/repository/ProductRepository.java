package com.product.categories.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.product.categories.jpa.entity.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    /*@Query(value = "select products.product_id, products.product_name, categories.category_name, products.product_price, products.category_id " +
            "from products inner join categories on products.Z = categories.category_id group by products.product_id;",
            nativeQuery = true)
    List<Product> findAll();
*/

    @Query(value = "select products.product_id, products.product_name, categories.category_name, products.product_price, products.category_id " +
            "from products inner join categories on products.category_id = categories.category_id group by products.product_id;",
            nativeQuery = true)
    List<Product> findAll();

}


