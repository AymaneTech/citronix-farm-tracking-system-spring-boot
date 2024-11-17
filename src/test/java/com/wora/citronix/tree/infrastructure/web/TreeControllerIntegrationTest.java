package com.wora.citronix.tree.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.citronix.farm.application.dto.request.FarmRequestDto;
import com.wora.citronix.farm.application.dto.request.FieldRequestDto;
import com.wora.citronix.farm.application.dto.response.FarmResponseDto;
import com.wora.citronix.farm.application.dto.response.FieldResponseDto;
import com.wora.citronix.farm.application.service.FarmService;
import com.wora.citronix.farm.application.service.FieldService;
import com.wora.citronix.farm.config.IntegrationTest;
import com.wora.citronix.tree.application.dto.TreeRequestDto;
import com.wora.citronix.tree.application.dto.TreeResponseDto;
import com.wora.citronix.tree.application.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.wora.citronix.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static com.wora.citronix.common.infrastructure.web.GlobalExceptionHandler.VALIDATION_FAILED_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class TreeControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final FarmService farmService;
    private final FieldService fieldService;
    private final TreeService treeService;

    private FieldResponseDto field;
    private TreeResponseDto tree;
    private LocalDate plantingDate;

    @BeforeEach
    void setup() {
        plantingDate = LocalDate.of(2023, 3, 27);
        FarmResponseDto farm = farmService.create(new FarmRequestDto("happy farm", "marrakech", 5000.0, null));
        field = fieldService.create(new FieldRequestDto("field1", 1000.0, farm.id()));
        tree = treeService.plant(new TreeRequestDto(plantingDate, field.id()));
    }

    @Nested
    class PlantTests {
        @Test
        void givenValidRequest_whenPlant_thenSuccess() throws Exception {
            TreeRequestDto request = new TreeRequestDto(plantingDate, field.id());
            mockMvc.perform(post("/api/v1/trees")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.plantingDate").value("2023-03-27"))
                    .andExpect(jsonPath("$.field.id").value(field.id()))
                    .andDo(print());
        }

        @Test
        void givenInvalidFieldId_whenPlant_thenReturnError() throws Exception {
            TreeRequestDto request = new TreeRequestDto(plantingDate, 9999L);
            mockMvc.perform(post("/api/v1/trees")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE))
                    .andDo(print());
        }
    }

    @Nested
    class UpdateTreeTests {

        @Test
        void givenValidRequest_whenUpdate_thenSuccess() throws Exception {
            LocalDate updatedPlantingDate = LocalDate.of(2023, 5, 15);
            TreeRequestDto updateRequest = new TreeRequestDto(updatedPlantingDate, field.id());

            mockMvc.perform(put("/api/v1/trees/{id}", tree.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.plantingDate").value("2023-05-15"))
                    .andExpect(jsonPath("$.field.id").value(field.id()))
                    .andDo(print());
        }

        @Test
        void givenInvalidRequest_whenUpdate_thenReturnError() throws Exception {
            LocalDate invalidPlantingDate = LocalDate.of(9999, 12, 31);
            TreeRequestDto updateRequest = new TreeRequestDto(invalidPlantingDate, field.id());

            mockMvc.perform(put("/api/v1/trees/{id}", tree.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andDo(print());
        }

        @Test
        void givenNonExistentId_whenUpdate_thenReturnNotFound() throws Exception {
            TreeRequestDto updateRequest = new TreeRequestDto(LocalDate.of(2023, 6, 15), field.id());

            mockMvc.perform(put("/api/v1/trees/{id}", 999999L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE))
                    .andDo(print());
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void givenTreeExists_whenGetById_thenReturnTree() throws Exception {
            mockMvc.perform(get("/api/v1/trees/{id}", tree.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(tree.id()))
                    .andExpect(jsonPath("$.plantingDate").value("2023-03-27"))
                    .andExpect(jsonPath("$.field.id").value(field.id()))
                    .andDo(print());
        }

        @Test
        void givenTreeDoesNotExist_whenGetById_thenReturnNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/trees/{id}", 9999L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE))
                    .andDo(print());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void givenTreeExists_whenDelete_thenSuccess() throws Exception {
            mockMvc.perform(delete("/api/v1/trees/{id}", tree.id()))
                    .andExpect(status().isNoContent())
                    .andDo(print());
        }

        @Test
        void givenTreeDoesNotExist_whenDelete_thenReturnNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/trees/{id}", 9999L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE))
                    .andDo(print());
        }
    }
}
