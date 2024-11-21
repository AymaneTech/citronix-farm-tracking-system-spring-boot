package com.wora.citronix.sales.application.service;

import com.wora.citronix.common.application.service.ApplicationService;
import com.wora.citronix.common.domain.exception.BusinessValidationException;
import com.wora.citronix.harvest.application.service.HarvestService;
import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.sales.application.mapper.SaleMapper;
import com.wora.citronix.sales.application.dto.SaleRequestDto;
import com.wora.citronix.sales.application.dto.SaleResponseDto;
import com.wora.citronix.sales.domain.Sale;
import com.wora.citronix.sales.domain.SaleRepository;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class DefaultSaleService implements SaleService {
    private final SaleRepository repository;
    private final HarvestService harvestService;
    private final SaleMapper mapper;

    @Override
    public SaleResponseDto buyHarvest(SaleRequestDto dto) {
        HarvestId harvestId = new HarvestId(dto.harvestId());
        if (repository.existsByHarvestId(harvestId))
            throw new BusinessValidationException("This harvest Already sold");

        Harvest harvest = harvestService.findEntityById(harvestId);
        Sale sale = mapper.toEntity(dto)
                .setHarvest(harvest);
        Sale savedSale = repository.save(sale);
        return mapper.toResponseDto(savedSale);
    }
}
