package com.wora.citronix.sales.application.service;

import com.wora.citronix.sales.application.dto.SaleRequestDto;
import com.wora.citronix.sales.application.dto.SaleResponseDto;

public interface SaleService {
    SaleResponseDto buyHarvest(SaleRequestDto dto);

}
