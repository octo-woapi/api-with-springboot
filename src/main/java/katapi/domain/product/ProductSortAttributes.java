package katapi.domain.product;

import java.util.Comparator;

public enum ProductSortAttributes {

    NAME("name", Comparator.comparing(Product::getName)),
    PRICE("price", Comparator.comparing(Product::getPrice)),
    WEIGHT("weight", Comparator.comparing(Product::getWeight));

    private String attributeLowerCase;
    private Comparator<Product> comparator;

    ProductSortAttributes(String attributeLowerCase, Comparator<Product> comparator) {
        this.attributeLowerCase = attributeLowerCase;
        this.comparator = comparator;
    }

    public String getAttributeLowerCase() {
        return attributeLowerCase;
    }

    public Comparator<Product> getComparator() {
        return comparator;
    }

    public static boolean contains(String value) {

        for (ProductSortAttributes attribute : ProductSortAttributes.values()) {
            if (attribute.getAttributeLowerCase().equals(value)) {
                return true;
            }
        }

        return false;
    }
}
