package katapi.domain.product;

import org.hibernate.validator.internal.util.stereotypes.Immutable;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.math.BigDecimal;


public class Product {

    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal weight;

    public Product() {
    }

    public Product(Long id, String name, BigDecimal price, BigDecimal weight) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                '}';
    }
}
