package com.wora.citronix.sales.application.service;

import com.wora.citronix.common.domain.exception.BusinessValidationException;
import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.harvest.application.service.HarvestService;
import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.harvest.domain.vo.Season;
import com.wora.citronix.sales.application.dto.SaleRequestDto;
import com.wora.citronix.sales.application.dto.SaleResponseDto;
import com.wora.citronix.sales.application.mapper.SaleMapper;
import com.wora.citronix.sales.domain.Sale;
import com.wora.citronix.sales.domain.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class SaleServiceUnitTest {
    @Mock
    private SaleRepository repository;
    @Mock
    private SaleMapper mapper;
    @Mock
    private HarvestService harvestService;
    @InjectMocks
    private DefaultSaleService underTest;

    private HarvestId harvestId;
    private SaleRequestDto request;

    @BeforeEach
    void setup() {
        harvestId = new HarvestId(2L);
        request = new SaleRequestDto(LocalDate.now(), 40.0, 2L);
    }

    @Test
    void givenHarvestAlreadySold_whenBuyHarvest_thenThrowBusinessValidationException() {
        given(repository.existsByHarvestId(any(HarvestId.class))).willReturn(true);

        assertThatExceptionOfType(BusinessValidationException.class)
                .isThrownBy(() -> underTest.buyHarvest(request))
                .withMessage("This harvest Already sold");
    }

    @Test
    void givenNotExistentId_whenBuyHarvest_thenThrowEntityNotFoundException() {
        given(repository.existsByHarvestId(harvestId)).willReturn(false);
        given(harvestService.findEntityById(harvestId)).willThrow(new EntityNotFoundException("harvest", harvestId.value()));

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> underTest.buyHarvest(request))
                .withMessage("harvest with id 2 not found");
    }

    @Test
    void givenValidRequest_whenBuyHarvest_thenSuccess() {
        Harvest harvest = new Harvest(request.date(), Season.fromDate(request.date()));
        harvest.setTotalQuantity(400.0);
        Sale sale = new Sale(2L, LocalDate.now(), 13.3, harvest);
        Double expectedIncome = harvest.getTotalQuantity() * sale.getUnitPrice();

        given(repository.existsByHarvestId(harvestId)).willReturn(false);
        given(harvestService.findEntityById(harvestId)).willReturn(harvest);
        given(mapper.toEntity(request)).willReturn(sale);
        given(repository.save(sale)).willReturn(sale);
        given(mapper.toResponseDto(sale)).willReturn(new SaleResponseDto(sale.getId().value(), sale.getDate(), sale.getUnitPrice(), sale.getIncome(), null));

        SaleResponseDto actual = underTest.buyHarvest(request);

        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(sale.getId().value());
        assertThat(actual.income()).isEqualTo(expectedIncome);
    }
}