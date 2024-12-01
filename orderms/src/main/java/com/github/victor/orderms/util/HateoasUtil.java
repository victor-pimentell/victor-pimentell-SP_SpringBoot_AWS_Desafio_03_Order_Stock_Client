package com.github.victor.orderms.util;

import com.github.victor.orderms.entities.Order;
import com.github.victor.orderms.web.controllers.OrderController;
import com.github.victor.orderms.web.dto.OrderResponseDto;
import com.github.victor.orderms.web.dto.mapper.OrderMapper;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasUtil {

    public static OrderResponseDto hateoasEmailOrders(Order input) {
        OrderResponseDto output = OrderMapper.toDto(input);
        output.add(linkTo(methodOn(OrderController.class).getOrdersByEmail(input.getEmail())).withRel("order_list_by_email"));
        return output;
    }

    public static List<OrderResponseDto> hateoasId(List<Order> input) {
        List<OrderResponseDto> output = OrderMapper.toListDto(input);
        return output.stream()
                .map(order ->
                        order.add(linkTo(methodOn(OrderController.class)
                                .getOrderById(order.getId())).withSelfRel())).toList();
    }

    public static OrderResponseDto hateoasIdAndEmailOrders(Order input) {
        OrderResponseDto output = OrderMapper.toDto(input);
        output.add(linkTo(methodOn(OrderController.class).getOrderById(input.getId())).withSelfRel());
        output.add(linkTo(methodOn(OrderController.class).getOrdersByEmail(output.getEmail())).withRel("order_list_by_email"));
        return output;
    }
}