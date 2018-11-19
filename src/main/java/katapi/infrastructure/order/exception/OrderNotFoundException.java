package katapi.infrastructure.order.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long id) {
        super("could not find order with id : "+id);
    }

}
