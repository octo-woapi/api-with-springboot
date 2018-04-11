package katapi.infrastructure.product.controller;

import katapi.domain.product.Product;
import katapi.infrastructure.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);


    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public String greeting(Model model) {

        String stringResult = productService.getAllProducts()
                .stream()
                .map(product -> product.toString())
                .reduce("", String::concat);

        model.addAttribute("product", stringResult);
        return "product_page";
    }
}
