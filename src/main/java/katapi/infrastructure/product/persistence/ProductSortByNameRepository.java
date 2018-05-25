package katapi.infrastructure.product.persistence;

import katapi.domain.product.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductSortByNameRepository extends PagingAndSortingRepository<Product,String>{
}
