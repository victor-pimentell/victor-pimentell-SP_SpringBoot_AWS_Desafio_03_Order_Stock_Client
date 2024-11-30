package com.github.victor.orderms.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientms", url = "http://localhost:8080", path = "/clients")
public interface ClientResourse {

    @GetMapping("/email/{email}")
    ResponseEntity<?> getClientByEmail(@PathVariable String email);
}
