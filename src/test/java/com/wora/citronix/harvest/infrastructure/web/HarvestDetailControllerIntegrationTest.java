package com.wora.citronix.harvest.infrastructure.web;

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
import com.wora.citronix.tree.application.dto.TreeRequestDto;
import com.wora.citronix.tree.application.dto.TreeResponseDto;
import com.wora.citronix.tree.application.service.TreeService;
import com.wora.citronix.tree.domain.TreeId;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;

import static com.wora.citronix.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static com.wora.citronix.common.infrastructure.web.GlobalExceptionHandler.VALIDATION_FAILED_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class HarvestDetailControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final FarmService farmService;
    private final FieldService fieldService;
    private final TreeService treeService;
    private final HarvestService harvestService;
    private final HarvestDetailService harvestDetailService;

    private TreeResponseDto tree;
    private HarvestResponseDto harvest;
    private HarvestDetailResponseDto harvestDetail;
    private LocalDate harvestDate;
    private LocalDate plantingDate;

    @BeforeEach
    void setup() {
        FarmResponseDto farm = farmService.create(new FarmRequestDto("citrus farm", "agadir", 10000.0, null));
        FieldResponseDto field = fieldService.create(new FieldRequestDto("field1", 2000.0, farm.id()));

        plantingDate = LocalDate.of(2021, 3, 21);
        tree = treeService.plant(new TreeRequestDto(plantingDate, field.id()));

        harvestDate = LocalDate.now();
        harvest = harvestService.create(new HarvestRequestDto(harvestDate));

        HarvestDetailId harvestDetailId = new HarvestDetailId(new HarvestId(harvest.id()), new TreeId(tree.id()));
        harvestDetail = harvestDetailService.add(harvestDetailId, new HarvestDetailRequestDto(292.3, harvestDate));
    }

    @Nested
    class AddTests {
        @Test
        void givenValidRequest_whenAdd_thenSuccess() throws Exception {
            TreeResponseDto newTree = treeService.plant(new TreeRequestDto(plantingDate, tree.field().id()));

            HarvestDetailRequestDto request = new HarvestDetailRequestDto(75.0, harvestDate);

            mockMvc.perform(post("/api/v1/harvest-details/{harvestId}/{treeId}", harvest.id(), newTree.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.quantity").value(75.0))
                    .andExpect(jsonPath("$.date").value(harvestDate.toString()))
                    .andDo(print());
        }

        @Test
        void givenInvalidTreeId_whenAdd_thenReturnError() throws Exception {
            HarvestDetailRequestDto request = new HarvestDetailRequestDto(75.0, harvestDate);

            mockMvc.perform(post("/api/v1/harvest-details/{harvestId}/{treeId}", harvest.id(), 99999L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE))
                    .andDo(print());
        }

        @Test
        void givenInvalidHarvestId_whenAdd_thenReturnError() throws Exception {
            HarvestDetailRequestDto request = new HarvestDetailRequestDto(75.0, harvestDate);

            mockMvc.perform(post("/api/v1/harvest-details/{harvestId}/{treeId}", 99999L, tree.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE))
                    .andDo(print());
        }

        @Test
        void givenInvalidDate_whenAdd_thenReturnError() throws Exception {
            HarvestDetailRequestDto request = new HarvestDetailRequestDto(75.0, LocalDate.now().plusYears(1));

            mockMvc.perform(post("/api/v1/harvest-details/{harvestId}/{treeId}", harvest.id(), tree.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andDo(print());
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void givenHarvestDetailExists_whenGetById_thenReturnHarvestDetail() throws Exception {
            mockMvc.perform(get("/api/v1/harvest-details/{harvestId}/{treeId}", harvest.id(), tree.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.quantity").value(harvestDetail.quantity()))
                    .andExpect(jsonPath("$.date").value(harvestDetail.date().toString()))
                    .andDo(print());
        }

        @Test
        void givenHarvestDetailDoesNotExist_whenGetById_thenReturnNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/harvest-details/{harvestId}/{treeId}", 99999L, 99999L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE))
                    .andDo(print());
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void givenValidRequest_whenUpdate_thenSuccess() throws Exception {
            HarvestDetailRequestDto updateRequest = new HarvestDetailRequestDto(100.0, harvestDate);

            mockMvc.perform(put("/api/v1/harvest-details/{harvestId}/{treeId}", harvest.id(), tree.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.quantity").value(100.0))
                    .andExpect(jsonPath("$.date").value(harvestDate.toString()))
                    .andDo(print());
        }

        @Test
        void givenNonExistentIds_whenUpdate_thenReturnNotFound() throws Exception {
            HarvestDetailRequestDto updateRequest = new HarvestDetailRequestDto(100.0, LocalDate.now());

            mockMvc.perform(put("/api/v1/harvest-details/{harvestId}/{treeId}", 99999L, 99999L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE))
                    .andDo(print());
        }

        @Test
        void givenInvalidDate_whenUpdate_thenReturnError() throws Exception {
            HarvestDetailRequestDto updateRequest = new HarvestDetailRequestDto(100.0, LocalDate.now().plusYears(1));

            mockMvc.perform(put("/api/v1/harvest-details/{harvestId}/{treeId}", harvest.id(), tree.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andDo(print());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void givenHarvestDetailExists_whenDelete_thenSuccess() throws Exception {
            mockMvc.perform(delete("/api/v1/harvest-details/{harvestId}/{treeId}", harvest.id(), tree.id()))
                    .andExpect(status().isNoContent())
                    .andDo(print());
        }

        @Test
        void givenHarvestDetailDoesNotExist_whenDelete_thenReturnNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/harvest-details/{harvestId}/{treeId}", 99999L, 99999L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE))
                    .andDo(print());
        }
    }
}