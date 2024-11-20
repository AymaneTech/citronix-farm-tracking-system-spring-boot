package com.wora.citronix.harvest.infrastructure.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.citronix.farm.config.IntegrationTest;
import com.wora.citronix.harvest.application.dto.request.HarvestRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestResponseDto;
import com.wora.citronix.harvest.application.service.HarvestService;
import com.wora.citronix.harvest.domain.vo.Season;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.wora.citronix.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class HarvestControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final HarvestService harvestService;

    private HarvestResponseDto harvest;

    @BeforeEach
    void setup() {
        harvest = harvestService.create(new HarvestRequestDto(LocalDate.now()));
    }

    @Test
    void givenHarvestExists_whenFindAll_thenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/harvests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void givenNotExistentId_whenFindById_thenReturnEntityNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/harvests/{id}", 333838L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
    }

    @Test
    void givenExistentId_whenFindById_thenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/harvests/{id}", harvest.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(harvest.id()));
    }

    @Test
    void givenSeasonAlreadyExists_whenCreate_thenReturnAlreadyExists() throws Exception {
        HarvestRequestDto request = new HarvestRequestDto(harvest.date());
        mockMvc.perform(post("/api/v1/harvests")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.errors")
                        .value(String.format("Already Exists A harvest in this season: %s, in date %s", Season.fromDate(request.date()), request.date())));
    }

    @Test
    void givenValidRequest_whenCreate_thenSuccess() throws Exception {
        HarvestRequestDto request = new HarvestRequestDto(LocalDate.now().minusMonths(3));

        mockMvc.perform(post("/api/v1/harvests")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.date").value(request.date().toString()))
                .andExpect(jsonPath("$.season").value(Season.fromDate(request.date()).toString()));
    }

}