package com.product.categories.jpa.rest_app;


import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.service.CategoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {


    Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryServiceImpl categoryService;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/categories", produces = {"application/json"})
    public final List<Category> categories() {

        LOGGER.debug("categories()");
        return categoryService.findAll();
    }

    @GetMapping(value = "/categories/{id}")
    public ResponseEntity<Category> findById(@PathVariable Integer id) {

        LOGGER.debug("find category by id({})", id);
        Optional<Category> optional = categoryService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/categories", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {

        LOGGER.debug("createCategory({})", category);
        Category categoryResult = categoryService.create(category);
        return new ResponseEntity<>(categoryResult, HttpStatus.CREATED);
    }

    @PutMapping(value = "/categories/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Category> updateCategory(Category category) {

        LOGGER.debug("updateCategory({})", category);
        Category categoryResult = categoryService.create(category);
        return new ResponseEntity<>(categoryResult, HttpStatus.OK);
    }

    @DeleteMapping(value = "/categories/{id}", produces = {"application/json"})
    public void deleteCategory(@PathVariable Integer id) {

        LOGGER.debug("deleteCategory({})", id);
        categoryService.delete(id);
    }
}