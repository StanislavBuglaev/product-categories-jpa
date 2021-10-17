package com.product.categories.jpa.web_app;

import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.entity.Product;
import com.product.categories.jpa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/products")
    public final String products(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products";
    }

    @GetMapping(value = "/addProduct/{categoryId}")
    public final String gotoAddProductPage(Model model, @PathVariable("categoryId") Integer categoryId) {

        model.addAttribute("isNew", true);
        model.addAttribute("product", new Product());
        model.addAttribute("categoryId", categoryId);
        return "addProduct";
    }

    @PostMapping(value = "/addProduct/{categoryId}")
    public String addProduct(Product product, @PathVariable("categoryId") Integer categoryId) {
        product.setCategory(new Category(categoryId));
        productService.createOrUpdate(product);
        return "redirect:/categories";
    }

    @GetMapping(value = "/editProduct/{id}")
    public final String gotoEditProductPage(@PathVariable Integer id, Model model) {

        Optional<Product> optionalProduct = productService.findById(id);
        if (optionalProduct.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("product", optionalProduct.get());
            model.addAttribute("productId", id);
            return "editProduct";
        } else {
            return "redirect:/products";
        }
    }

    @PostMapping(value = "/editProduct/{categoryId}")
    public final String updateProduct(Product product, @PathVariable("categoryId") Integer categoryId) {
        product.setCategory(new Category(categoryId));
        productService.createOrUpdate(product);
        return "redirect:/products";
    }

    @GetMapping(value = "/products/{id}")
    public final String deleteProductById(@PathVariable Integer id) {
        productService.delete(id);
        return "redirect:/products";
    }
}

