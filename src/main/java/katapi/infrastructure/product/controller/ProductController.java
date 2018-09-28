package katapi.infrastructure.product.controller;

import katapi.domain.product.Product;
import katapi.infrastructure.product.rest.ProductResource;
import katapi.infrastructure.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    static final int MAX_RANGE = 5;

    @Autowired
    ProductService productService;

    /*************************************************************************
                                    GET methods
    ************************************************************************ */

    /**
     * Basic GET for HYPERMEDIA showing the process of transforming a POJO into HAL Resource
     * @return
     */
    @GetMapping(value = "", produces =  "application/hal+json")
    public Resources<ProductResource> listAllProductsHypermedia() {
        List<ProductResource> productResourceList = productService.getAllProducts()
                .stream()
                .map(ProductResource::new)
                .collect(Collectors.toList());

        return new Resources<>(productResourceList);
    }

    /**
     * Handles pagination with a HTTP 206 Partial Content status
     * @param sortParam
     * @param range
     * @return Prodcut list, sorted, paginated
     */
    @GetMapping(value = "", produces =  "application/json", params = {"sort", "range"})
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    public List<Product> listAllProductsSortedAndPaginated(
            @RequestParam(value = "sort", required = false) String sortParam,
            @RequestParam(value = "range", required = false) String range,
            final HttpServletResponse response) {

        int totalCount = productService.getAllProducts().size();

        response.setHeader(HttpHeaders.ACCEPT_RANGES, "product "+MAX_RANGE);
        response.setHeader(HttpHeaders.CONTENT_RANGE , range+"/"+totalCount);

        return productService.getSortedAndPaginatedProductList(sortParam, range, MAX_RANGE);
    }

    @GetMapping(value = "", produces =  "application/json", params = {"range"})
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    public List<Product> listAllProductsPaginated(
            @RequestParam(value = "range", required = false) String range,
            final HttpServletResponse response) {

        int totalCount = productService.getAllProducts().size();

        response.setHeader(HttpHeaders.ACCEPT_RANGES, "product "+MAX_RANGE);
        response.setHeader(HttpHeaders.CONTENT_RANGE , range+"/"+totalCount);

        return productService.getSortedAndPaginatedProductList(null, range, MAX_RANGE);
    }

    @GetMapping(value = "", produces =  "application/json", params = {"sort"})
    @ResponseStatus(HttpStatus.OK)
    public List<Product> listAllProductsSorted(@RequestParam(value = "sort", required = false) String sortParam) {
        return productService.getSortedAndPaginatedProductList(sortParam, null, MAX_RANGE);
    }

    /**
     * Simple call with all results and a HTTP 200 status
     * @return
     */
    @GetMapping(value = "", produces =  "application/json", params = {})
    @ResponseStatus(HttpStatus.OK)
    public List<Product> listAllProducts() {
        return productService.getAllProducts();
    }


    @GetMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public ProductResource getProductById(@PathVariable Long productId){
        return new ProductResource(productService.getProductById(productId));
    }

    /*************************************************************************
                                    POST methods
     ************************************************************************ */

    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createProduct(@RequestBody Product product){
        Product createdProduct = productService.createProduct(product.getName(), product.getPrice(), product.getWeight());
        Link linkToThisProduct = new ProductResource(createdProduct).getLink("self");
        return ResponseEntity.created(URI.create(linkToThisProduct.getHref())).body(createdProduct);
    }

    /*************************************************************************
                                    DELETE methods
     ************************************************************************ */

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long productId){
        productService.deleteProductFromItsID(productId);
    }



}
