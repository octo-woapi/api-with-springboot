package katapi.infrastructure.product.controller;

import katapi.domain.product.Product;
import katapi.infrastructure.product.rest.ProductResource;
import katapi.infrastructure.product.service.ProductService;
import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @GetMapping(value = "", produces =  "application/hal+json")
    public Resources<ProductResource> listAllProductsHypermedia() {
        List<ProductResource> productResourceList = productService.getAllProducts()
                .stream()
                .map(ProductResource::new)
                .collect(Collectors.toList());

        return new Resources<>(productResourceList);
    }

    @GetMapping(value = "", produces =  "application/json")
    public List<Product> listAllProducts(@RequestParam(value = "sort", required = false) String sortParam) {
        if(! StringUtils.isNullOrEmpty(sortParam)) {
            return getSortedProductList(sortParam);
        }
        return productService.getAllProducts();
    }

    @GetMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ProductResource getProductById(@PathVariable Long productId){
        return new ProductResource(productService.getProductById(productId));
    }

    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createProduct(@RequestBody Product product){
        Product createdProduct = productService.createProduct(product.getName(), product.getPrice(), product.getWeight());
        Link linkToThisProduct = new ProductResource(createdProduct).getLink("self");
        return ResponseEntity.created(URI.create(linkToThisProduct.getHref())).body(createdProduct);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long productId){
        productService.deleteProductFromItsID(productId);
    }

    /* ****************************************************************************************************************
                                                    PRIVATE METHODS
    **************************************************************************************************************** */

    private List<Product> getSortedProductList(@NotNull String sortParam) {
        if(ProductAttribute.contains(sortParam)) {
            return productService.getAllProducts()
                    .stream()
                    .sorted(chooseAttributeToCompare(sortParam))
                    .collect(Collectors.toList());
        }else{
            return productService.getAllProducts();
        }

    }

    private Comparator<Product> chooseAttributeToCompare(@NotNull String sortParam){
        for (ProductAttribute attribute : ProductAttribute.values()) {
            if (attribute.getAttributeLowerCase().equals(sortParam)) {
                return attribute.getComparator();
            }
        }
        return null;
    }


    private enum ProductAttribute{
        NAME("name", Comparator.comparing(Product::getName)),
        PRICE("price", Comparator.comparing(Product::getPrice)),
        WEIGHT("weight", Comparator.comparing(Product::getWeight));

        private String attributeLowerCase;
        private Comparator<Product> comparator;

        ProductAttribute(String attributeLowerCase, Comparator<Product> comparator){
            this.attributeLowerCase = attributeLowerCase;
            this.comparator = comparator;
        }

        public String getAttributeLowerCase(){
            return attributeLowerCase;
        }

        public Comparator<Product> getComparator() {
            return comparator;
        }

        public static boolean contains(String value) {

            for (ProductAttribute attribute : ProductAttribute.values()) {
                if (attribute.getAttributeLowerCase().equals(value)) {
                    return true;
                }
            }

            return false;
        }
    }


}
