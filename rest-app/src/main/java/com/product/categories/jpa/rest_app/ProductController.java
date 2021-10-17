package com.product.categories.jpa.rest_app;

import com.product.categories.jpa.entity.Product;
import com.product.categories.jpa.service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductServiceImpl productService;

    @Autowired
    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/products", produces = {"application/json"})
    public final List<Product> products() {

        LOGGER.debug("categories()");
        return productService.findAll();
    }

    @GetMapping(value = "/products/{id}")
    public ResponseEntity<Product> findById(@PathVariable Integer id) {

        LOGGER.debug("find product by id({})", id);
        Optional<Product> optional = productService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/products", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

        LOGGER.debug("createProduct({})", product);
        Product productResult = productService.createOrUpdate(product);
        return new ResponseEntity<>(productResult, HttpStatus.CREATED);
    }

    @PutMapping(value = "/products/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Product> updateProduct(Product product) {

        LOGGER.debug("updateProduct({})", product);
        Product productResult = productService.createOrUpdate(product);
        return new ResponseEntity<>(productResult, HttpStatus.OK);
    }

    @DeleteMapping(value = "/products/{id}", produces = {"application/json"})
    public void deleteProduct(@PathVariable Integer id) {

        LOGGER.debug("deleteProduct({})", id);
        productService.delete(id);
    }
}

