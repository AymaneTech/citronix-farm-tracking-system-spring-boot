package com.wora.citronix.sales.infrastructure.web;

import com.wora.citronix.sales.application.dto.SaleRequestDto;
import com.wora.citronix.sales.application.dto.SaleResponseDto;
import com.wora.citronix.sales.application.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService service;

    @PostMapping
    public ResponseEntity<SaleResponseDto> buyHarvest(@RequestBody @Valid SaleRequestDto request) {
        SaleResponseDto sale = service.buyHarvest(request);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }
}
