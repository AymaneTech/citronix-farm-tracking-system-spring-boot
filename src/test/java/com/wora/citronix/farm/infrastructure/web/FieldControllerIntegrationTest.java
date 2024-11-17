package com.wora.citronix.farm.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.citronix.farm.application.dto.request.FarmRequestDto;
import com.wora.citronix.farm.application.dto.request.FieldRequestDto;
import com.wora.citronix.farm.application.dto.response.FarmResponseDto;
import com.wora.citronix.farm.application.dto.response.FieldResponseDto;
import com.wora.citronix.farm.application.service.FarmService;
import com.wora.citronix.farm.application.service.FieldService;
import com.wora.citronix.farm.config.IntegrationTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.wora.citronix.common.infrastructure.web.GlobalExceptionHandler.ENTITY_CREATION_MESSAGE;
import static com.wora.citronix.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FieldControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final FieldService fieldService;
    private final FarmService farmService;

    private FarmResponseDto farm;
    private FieldResponseDto field;

    @BeforeEach
    void setup() {
        List<FieldRequestDto> fieldRequests = List.of(
                new FieldRequestDto("field1", 1000.0, null),
                new FieldRequestDto("field2", 1000.0, null),
                new FieldRequestDto("field3", 1000.0, null)
        );
        farm = farmService.create(new FarmRequestDto("happy farm", "marrakech", 5000.0, fieldRequests));

    }

    @Nested
    class FindByIdTests {
        @Test
        void givenFieldIdExists_whenFindById_thenReturnFoundField() throws Exception {
            Long id = farm.fields().getFirst().id();
            mockMvc.perform(get("/api/v1/fields/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id));
        }

        @Test
        void givenNotExistentId_whenFindById_thenReturnNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/fields/{id}", 383883L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }
    }

    @Nested
    class CreateTests {
        @Test
        void givenValidRequest_whenCreate_thenCreateField() throws Exception {
            FieldRequestDto request = new FieldRequestDto("fiedl 1", 1000.0, farm.id());
            mockMvc.perform(post("/api/v1/fields")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        void givenInvalidRequest_whenCreate_thenReturnError() throws Exception {
            FieldRequestDto request = new FieldRequestDto("fiedl 1", 393939.0, farm.id());
            mockMvc.perform(post("/api/v1/fields")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(ENTITY_CREATION_MESSAGE));
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void givenValidRequest_whenUpdate_thenUpdateField() throws Exception {
            Long fieldId = farm.fields().getFirst().id();
            FieldRequestDto request = new FieldRequestDto("updated field", 1500.0, farm.id());

            mockMvc.perform(put("/api/v1/fields/{id}", fieldId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("updated field"))
                    .andExpect(jsonPath("$.area").value(1500.0));
        }

        @Test
        void givenInvalidRequest_whenUpdate_thenReturnError() throws Exception {
            Long fieldId = farm.fields().getFirst().id();
            FieldRequestDto request = new FieldRequestDto("updated field", 9999999.0, farm.id());

            mockMvc.perform(put("/api/v1/fields/{id}", fieldId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(ENTITY_CREATION_MESSAGE));
        }

        @Test
        void givenNonExistentId_whenUpdate_thenReturnNotFound() throws Exception {
            FieldRequestDto request = new FieldRequestDto("updated field", 1500.0, farm.id());

            mockMvc.perform(put("/api/v1/fields/{id}", 99999L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void givenExistingField_whenDelete_thenDeleteField() throws Exception {
            Long fieldId = farm.fields().getFirst().id();

            mockMvc.perform(delete("/api/v1/fields/{id}", fieldId))
                    .andExpect(status().isNoContent());
        }

        @Test
        void givenNonExistentId_whenDelete_thenReturnNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/fields/{id}", 99999L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }
    }
}