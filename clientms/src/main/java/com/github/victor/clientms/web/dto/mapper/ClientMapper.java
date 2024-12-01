package com.github.victor.clientms.web.dto.mapper;

import com.github.victor.clientms.entities.Client;
import com.github.victor.clientms.web.dto.ClientCreateDto;
import com.github.victor.clientms.web.dto.ClientResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;

public class ClientMapper {

    public static Client toClient(ClientCreateDto dto) {
        return new ModelMapper().map(dto, Client.class);
    }

    public static ClientResponseDto toDto(Client client) {
        return new ModelMapper().map(client, ClientResponseDto.class);
    }
}
