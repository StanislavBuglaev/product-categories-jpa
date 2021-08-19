package com.product.categories.jpa.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping()
    public String defaultPageRedirect() {
        return "redirect:categories";
    }
}
