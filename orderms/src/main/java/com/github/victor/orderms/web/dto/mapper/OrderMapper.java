package com.github.victor.orderms.web.dto.mapper;

import com.github.victor.orderms.entities.Order;
import com.github.victor.orderms.web.dto.OrderCreateDto;
import com.github.victor.orderms.web.dto.OrderResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;

public class OrderMapper {

    public static Order toOrder(OrderCreateDto dto) {
        return new ModelMapper().map(dto, Order.class);
    }

    public static OrderResponseDto toDto(Order order) {
        return new ModelMapper().map(order, OrderResponseDto.class);
    }

    public static List<OrderResponseDto> toListDto(List<Order> orders){
        return orders.stream().map(OrderMapper::toDto).toList();
    }
}
