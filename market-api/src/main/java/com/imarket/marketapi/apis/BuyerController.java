package com.imarket.marketapi.apis;

import com.imarket.marketapi.apis.dto.OrderDto;
import com.imarket.marketapi.apis.response.MultiResponse;
import com.imarket.marketapi.apis.response.SingleResponse;
import com.imarket.marketdomain.domain.Order;
import com.imarket.marketdomain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/${api.version}/buyers", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class BuyerController {
    private OrderService orderService;

    @Autowired
    public BuyerController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Requirement: 단일 회원(구매자)의 주문 목록을 조회할 수 있어야 한다.
    @GetMapping(path = "/{buyer-id}/orders")
    public ResponseEntity<MultiResponse<OrderDto>> getAllOrdersByBuyer(@PathVariable("buyer-id") long buyerId,
                                                                       int page,
                                                                       int size) {
        Page<Order> orderPage = orderService.findOrdersByBuyer(buyerId, page, size);
        List<OrderDto> orderDtoList = OrderDto.toOrderDtoList(orderPage);
        return ResponseEntity.ok(new MultiResponse<>(HttpStatus.OK, orderDtoList, orderPage));
    }
}
