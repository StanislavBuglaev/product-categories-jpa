package com.product.categories.jpa.rest_app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping(value = "/hello")
    public String version() {
        return "Hello Rest";
    }
}
