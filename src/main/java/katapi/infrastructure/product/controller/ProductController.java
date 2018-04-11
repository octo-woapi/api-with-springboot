package katapi.infrastructure.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @GetMapping("/products")
    public String greeting(@RequestParam(name="product", required=false, defaultValue="Product") String product, Model model) {
        model.addAttribute("product", product);
        return "product_page";
    }
}
