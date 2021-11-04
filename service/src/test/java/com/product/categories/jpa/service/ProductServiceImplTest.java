package com.product.categories.jpa.service;

import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.entity.Product;
import com.product.categories.jpa.repository.CategoryRepository;
import com.product.categories.jpa.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void findAll() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(new Product()));

        List<Product> products = productService.findAll();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    void findById() {
        Integer productId = 1;
        String productName = "Test";
        Double productPrice = 400.0;
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductPrice(productPrice);

        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        Optional<Product> optionalProduct = productService.findById(productId);
        assertTrue(optionalProduct.isPresent());
        assertSame(product, optionalProduct.get());
    }

    @Test
    void createOrUpdate() {
        Product product = new Product();
        when(productRepository.save(any())).thenReturn(product);

        product = productService.createOrUpdate(any());

        assertNotNull(product);

    }

    @Test
    void delete() {
      //  ?
    }
}