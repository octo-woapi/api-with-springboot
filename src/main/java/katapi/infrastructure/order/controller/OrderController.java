package katapi.infrastructure.order.controller;

import katapi.domain.order.Order;
import katapi.infrastructure.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping(value = "", produces =  "application/json", params = {})
    @ResponseStatus(HttpStatus.OK)
    public List<Order> listAllOrders() {
        return orderService.getAllOrders();
    }
}
