package katapi.infrastructure.product.rest;

import katapi.domain.product.Product;
import katapi.infrastructure.product.controller.ProductController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


public class ProductResource extends ResourceSupport {

    private final Product product;

    public ProductResource(Product product) {
        Long id = product.getId();
        this.product = product;
        this.add(linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel());
    }

    public Product getProduct(){return product;}

}
