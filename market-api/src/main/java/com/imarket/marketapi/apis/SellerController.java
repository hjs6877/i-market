package com.imarket.marketapi.apis;

import com.imarket.marketapi.apis.dto.MemberDto;
import com.imarket.marketapi.apis.dto.OrderDto;
import com.imarket.marketapi.apis.response.MultiResponse;
import com.imarket.marketdomain.domain.Member;
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
@RequestMapping(path = "/api/${api.version}/sellers", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class SellerController {
    private OrderService orderService;

    @Autowired
    public SellerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{seller-id}/orders")
    public ResponseEntity<MultiResponse<OrderDto>> getOrdersBySeller(@PathVariable("seller-id") long sellerId,
                                                                         @RequestParam("page") int page,
                                                                         @RequestParam("size") int size) {
        Page<Order> orderPage = orderService.findOrdersBySeller(sellerId, page, size);
        List<OrderDto> orderDtoList = OrderDto.toOrderDtoList(orderPage);
        return ResponseEntity.ok(new MultiResponse<>(HttpStatus.OK, orderDtoList, orderPage));
    }
}
