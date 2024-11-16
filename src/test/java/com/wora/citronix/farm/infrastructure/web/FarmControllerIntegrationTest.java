package com.wora.citronix.farm.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.citronix.Application;
import com.wora.citronix.farm.application.dto.request.FarmRequestDto;
import com.wora.citronix.farm.application.dto.response.FarmResponseDto;
import com.wora.citronix.farm.application.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.wora.citronix.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static com.wora.citronix.common.infrastructure.web.GlobalExceptionHandler.VALIDATION_FAILED_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FarmControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final FarmService farmService;

    private FarmResponseDto farm;
    @Autowired
    private Application application;

    @BeforeEach
    void setup() {
        farm = farmService.create(new FarmRequestDto("happy farm", "marrakech", 339393.0, null));
    }

    @Test
    void givenFarmsExists_whenFindAll_thenReturnFarmPage() throws Exception {
        mockMvc.perform(get("/api/v1/farms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Nested
    class FindByIdTests {
        @Test
        void givenNotExistentFarmId_whenFindById_thenReturnNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/farms/{id}", 22929292L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        void givenValidFarmId_whenFindById_thenReturnFoundFarm() throws Exception {
            mockMvc.perform(get("/api/v1/farms/{id}", farm.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(farm.id()))
                    .andExpect(jsonPath("$.name").value(farm.name()));
        }
    }

    @Nested
    class CreateTests {
        @Test
        void givenInvalidRequest_whenCreate_thenReturnBadRequest() throws Exception {
            FarmRequestDto request = new FarmRequestDto("happy farm", "marrakech", 393.0, null);

            mockMvc.perform(post("/api/v1/farms")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andExpect(jsonPath("$.errors.area").isNotEmpty())
                    .andExpect(jsonPath("$.errors.name").value("farm name already taken"))
                    .andDo(print());
        }

        @Test
        void givenValidRequest_whenCreate_thenReturnCreatedFarm() throws Exception {
            FarmRequestDto request = new FarmRequestDto("happy farm 2", "marrakech", 3932.0, null);

            mockMvc.perform(post("/api/v1/farms")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value(request.name()));
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void givenNotExistentId_whenUpdate_thenReturnNotFound() throws Exception {
            FarmRequestDto request = new FarmRequestDto("happy farm udpate", "marrakech update", 3932.0, null);

            mockMvc.perform(put("/api/v1/farms/{id}", 83838383L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        void givenExistentIdAndValidRequest_whenUpdate_thenReturnUpdatedFarm() throws Exception {
            FarmRequestDto request = new FarmRequestDto("happy farm udpate", "marrakech update", 3932.0, null);

            mockMvc.perform(put("/api/v1/farms/{id}", farm.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(farm.id()));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void givenNotExistentId_whenDelete_thenReturnNotFound() throws Exception {
            mockMvc.perform(delete("/api/v1/farms/{id}", 33933L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        void givenExistentId_whenDelete_thenDeleteFarm() throws Exception {
            mockMvc.perform(delete("/api/v1/farms/{id}", farm.id()))
                    .andExpect(status().isNoContent());
        }
    }
}