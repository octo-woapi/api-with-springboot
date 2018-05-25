package katapi.infrastructure.product.service;

import katapi.domain.product.Product;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface ProductSortByNameService  {

    Page<Product> listAllByPageByPrice(Pageable pageable);
}
