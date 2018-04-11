package katapi.infrastructure.product.persistence;

import katapi.domain.product.Product;

import java.util.List;

public interface ProductDao// extends CrudRepository {
{
    List<Product> getAllProducts();
}
