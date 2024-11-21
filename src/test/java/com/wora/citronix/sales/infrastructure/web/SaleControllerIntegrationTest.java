package com.wora.citronix.sales.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.citronix.farm.application.dto.request.FarmRequestDto;
import com.wora.citronix.farm.application.dto.request.FieldRequestDto;
import com.wora.citronix.farm.application.dto.response.FarmResponseDto;
import com.wora.citronix.farm.application.dto.response.FieldResponseDto;
import com.wora.citronix.farm.application.service.FarmService;
import com.wora.citronix.farm.application.service.FieldService;
import com.wora.citronix.farm.config.IntegrationTest;
import com.wora.citronix.harvest.application.dto.request.HarvestDetailRequestDto;
import com.wora.citronix.harvest.application.dto.request.HarvestRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import com.wora.citronix.harvest.application.dto.response.HarvestResponseDto;
import com.wora.citronix.harvest.application.service.HarvestDetailService;
import com.wora.citronix.harvest.application.service.HarvestService;
import com.wora.citronix.harvest.domain.vo.HarvestDetailId;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.sales.application.dto.SaleRequestDto;
import com.wora.citronix.tree.application.dto.TreeRequestDto;
import com.wora.citronix.tree.application.dto.TreeResponseDto;
import com.wora.citronix.tree.application.service.TreeService;
import com.wora.citronix.tree.domain.TreeId;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class SaleControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final FarmService farmService;
    private final FieldService fieldService;
    private final TreeService treeService;
    private final HarvestService harvestService;
    private final HarvestDetailService harvestDetailService;

    private HarvestResponseDto harvest;

    @BeforeEach
    void setup() {
        FarmResponseDto farm = farmService.create(new FarmRequestDto("citrus farm", "agadir", 10000.0, null));
        FieldResponseDto field = fieldService.create(new FieldRequestDto("field1", 2000.0, farm.id()));

        LocalDate plantingDate = LocalDate.of(2021, 3, 21);
        TreeResponseDto tree = treeService.plant(new TreeRequestDto(plantingDate, field.id()));

        LocalDate harvestDate = LocalDate.now();
        harvest = harvestService.create(new HarvestRequestDto(harvestDate));

        HarvestDetailId harvestDetailId = new HarvestDetailId(new HarvestId(harvest.id()), new TreeId(tree.id()));
        harvestDetailService.add(harvestDetailId, new HarvestDetailRequestDto(292.3, harvestDate));
    }

    @Test
    void givenValidRequest_whenAdd_thenSuccess() throws Exception {
        SaleRequestDto request = new SaleRequestDto(LocalDate.now(), 883.0, harvest.id());

        mockMvc.perform(post("/api/v1/sales")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}