package katapi.infrastructure.product.controller;

import katapi.domain.product.Product;
import katapi.infrastructure.product.rest.ProductResource;
import katapi.infrastructure.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public Resources<ProductResource> listAllProducts() {
        List<ProductResource> productList = productService.getAllProducts()
                .stream()
                .map(ProductResource::new)
                .collect(Collectors.toList());

        return new Resources<>(productList);
    }

    @GetMapping("/{productId}")
    public ProductResource getProductById(@PathVariable Long productId){
        return new ProductResource(productService.getProductById(productId));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResource createProduct(@RequestBody Product product){
        return new ProductResource(productService.createProduct(product.getName(), product.getPrice(), product.getWeight()));
    }

}
