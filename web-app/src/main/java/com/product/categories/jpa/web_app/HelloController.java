package com.product.categories.jpa.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Root controller.
 */
@Controller
public class HelloController {

    @GetMapping(value = "/hello")
    public final String gotoHelloPage() {
        return "hello";
    }
}
