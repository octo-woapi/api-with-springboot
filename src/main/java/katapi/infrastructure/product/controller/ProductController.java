package katapi.infrastructure.product.controller;

import katapi.domain.product.Product;
import katapi.domain.product.ProductSortAttributes;
import katapi.infrastructure.product.rest.ProductResource;
import katapi.infrastructure.product.service.ProductService;
import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.awt.print.Pageable;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private int PRODUCT_PER_PAGE = 2;

    @Autowired
    ProductService productService;

    //Basic GET for HYPERMEDIA showing the process of transforming a POJO into HAL Resource
    @GetMapping(value = "", produces =  "application/hal+json")
    public Resources<ProductResource> listAllProductsHypermedia() {
        List<ProductResource> productResourceList = productService.getAllProducts()
                .stream()
                .map(ProductResource::new)
                .collect(Collectors.toList());

        return new Resources<>(productResourceList);
    }

    @GetMapping(value = "", produces =  "application/json")
    public List<Product> listAllProductsByPage(@RequestParam(value = "sort", required = false) String sortParam, @RequestParam(value = "range", required = false) String range) {
            return getSortedAndPaginatedProductList(sortParam, range);
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

    private List<Product> getSortedAndPaginatedProductList(String sortParam, String range) {
        List<Product> productList = getSortedProductList(sortParam);
        return paginateProductList(productList, range);
    }

    private List<Product> paginateProductList(List<Product> productList, String range) {
        if(StringUtils.isNullOrEmpty(range)){
            return productList;
        }
        int startIndex = getStartIndexFromRange(range);
        int endIndex = getEndIndexFromRange(range);
        return productList.subList(startIndex, endIndex);
    }

    private int getStartIndexFromRange(String range) {
        return 0;
    }

    private int getEndIndexFromRange(String range) {
        return 12;
    }

    private List<Product> getSortedProductList(String sortParam) {
        if (ProductSortAttributes.contains(sortParam)) {
            return productService.getAllProducts()
                    .stream()
                    .sorted(Objects.requireNonNull(chooseAttributeToCompare(sortParam)))
                    .collect(Collectors.toList());
        } else {
            return productService.getAllProducts();
        }
    }

    private Comparator<Product> chooseAttributeToCompare(@NotNull String sortParam){
        for (ProductSortAttributes attribute : ProductSortAttributes.values()) {
            if (attribute.getAttributeLowerCase().equals(sortParam)) {
                return attribute.getComparator();
            }
        }
        return null;
    }

}
