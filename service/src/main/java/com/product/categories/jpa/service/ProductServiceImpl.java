package com.product.categories.jpa.service;

import com.product.categories.jpa.entity.Product;
import com.product.categories.jpa.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> findAll() {

        LOGGER.trace("findAll()");
        return productRepository.findAll();
    }

    public Optional<Product> findById(Integer productId) {
        LOGGER.debug("findById(id:{})", productId);
        return productRepository.findById(productId);
    }

    public Product create(Product product) {
        LOGGER.debug("create(product:{})", product);
        return productRepository.save(product);
    }

    public Product update(Integer productId) {
        LOGGER.debug("update(product:{})", productId);
        return productRepository.findById(productId).get();
    }

    public void delete(Integer productId) {
        LOGGER.debug("delete(productId:{})", productId);
        productRepository.deleteById(productId);
    }
}
