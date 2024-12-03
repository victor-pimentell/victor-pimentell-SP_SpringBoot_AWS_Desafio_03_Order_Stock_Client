package com.github.victor.clientms.infra;

import com.github.victor.clientms.web.dto.OrderResponseDto;
import com.github.victor.clientms.web.dto.UpdateEmailDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "orderms", url = "http://orderms:8081", path = "/api/v1/orders")
public interface OrderResourse {

    @PutMapping("/email")
    ResponseEntity<?> updateOrderEmail(@Valid @RequestBody UpdateEmailDto orderUpdateEmailDto);

    @GetMapping("/email/{email}")
    ResponseEntity<List<OrderResponseDto>> getOrdersByEmail(@PathVariable String email);
}
