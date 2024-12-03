package com.github.victor.orderms.infra;

import com.github.victor.orderms.web.dto.ClientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientms", url = "http://clientms:8080", path = "/api/v1/clients")
public interface ClientResourse {

    @GetMapping("/email/{email}")
    ResponseEntity<ClientResponseDto> getClientByEmail(@PathVariable String email);
}
