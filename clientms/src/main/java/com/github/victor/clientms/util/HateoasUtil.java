package com.github.victor.clientms.util;

import com.github.victor.clientms.entities.Client;
import com.github.victor.clientms.web.controllers.ClientController;
import com.github.victor.clientms.web.dto.ClientResponseDto;
import com.github.victor.clientms.web.dto.mapper.ClientMapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasUtil {

    public static ClientResponseDto hateoasIdAndEmailOrders(Client input) {
        ClientResponseDto output = ClientMapper.toDto(input);
        output.add(linkTo(methodOn(ClientController.class).getClientById(output.getId())).withSelfRel());
        output.add(linkTo(methodOn(ClientController.class).getOrdersByEmail(output.getEmail())).withRel("client_orders"));
        return output;
    }

    public static ClientResponseDto hateoasEmailAndEmailOrders(Client input) {
        ClientResponseDto output = ClientMapper.toDto(input);
        output.add(linkTo(methodOn(ClientController.class).getClientByEmail(output.getEmail())).withSelfRel());
        output.add(linkTo(methodOn(ClientController.class).getOrdersByEmail(output.getEmail())).withRel("client_orders"));
        return output;
    }

    public static ClientResponseDto hateoasIdEmailAndEmailOrders(Client input) {
        ClientResponseDto output = ClientMapper.toDto(input);
        output.add(linkTo(methodOn(ClientController.class).getClientById(output.getId())).withSelfRel());
        output.add(linkTo(methodOn(ClientController.class).getClientByEmail(output.getEmail())).withSelfRel());
        output.add(linkTo(methodOn(ClientController.class).getOrdersByEmail(output.getEmail())).withRel("client_orders"));
        return output;
    }
}
