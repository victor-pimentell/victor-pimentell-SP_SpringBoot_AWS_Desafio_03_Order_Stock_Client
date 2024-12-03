package com.github.victor.orderms.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientms", url = "http://ec2-3-141-42-56.us-east-2.compute.amazonaws.com:8080", path = "/api/v1/clients")
public interface ClientResourse {

    @GetMapping("/email/{email}")
    ResponseEntity<?> getClientByEmail(@PathVariable String email);
}
