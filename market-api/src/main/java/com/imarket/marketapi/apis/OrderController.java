package com.imarket.marketapi.apis;

import com.imarket.marketapi.apis.dto.OrderDto;
import com.imarket.marketapi.apis.response.SingleResponse;
import com.imarket.marketdomain.domain.Order;
import com.imarket.marketdomain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/${api.version}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<SingleResponse<OrderDto>> postOrder(@Valid @RequestBody OrderDto.Post body) {
        OrderDto orderDto = new OrderDto();
        Order order = orderService.saveOrder(orderDto.toOrder(body));
        return ResponseEntity.ok(new SingleResponse<>(HttpStatus.OK, orderDto.toOrderDto(order)));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{order-id}")
    public ResponseEntity<SingleResponse<OrderDto>> getOrder(@PathVariable("order-id") long orderId) {
        OrderDto orderDto = new OrderDto();
        Order order = orderService.findOrderById(orderId);
        return ResponseEntity.ok(new SingleResponse<>(HttpStatus.OK, orderDto.toOrderDto(order)));
    }
}
