package com.wora.citronix.harvest.application.service;

import com.wora.citronix.common.domain.exception.AlreadyExistsException;
import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.harvest.application.dto.request.HarvestRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestResponseDto;
import com.wora.citronix.harvest.application.mapper.HarvestMapper;
import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.repository.HarvestRepository;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.harvest.domain.vo.Season;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class HarvestServiceUnitTest {
    @Mock
    private HarvestRepository repository;
    @Mock
    private HarvestMapper mapper;

    private HarvestService underTest;
    private Harvest harvest;

    @BeforeEach
    void setup() {
        underTest = new DefaultHarvestService(repository, mapper);
        harvest = new Harvest(LocalDate.of(2024, 11, 21), Season.FALL).setId(new HarvestId(28L));
    }

    @Nested
    class FindAllTests {
        @Test
        void givenHarvestsExists_whenFindAll_thenSuccess() {
            given(repository.findAll(any(PageRequest.class)))
                    .willReturn(new PageImpl<>(List.of(harvest)));
            given(mapper.toResponseDto(harvest)).willReturn(new HarvestResponseDto(harvest.getId().value(), harvest.getDate(), harvest.getSeason(), null));

            Page<HarvestResponseDto> actual = underTest.findAll(0, 10);
            assertThat(actual).isNotNull();
            assertThat(actual.getSize()).isEqualTo(1);
        }

        @Test
        void givenHarvestsNotExists_whenFindAll_thenReturnEmptyList() {
            given(repository.findAll(any(PageRequest.class))).willReturn(new PageImpl<>(List.of()));

            Page<HarvestResponseDto> actual = underTest.findAll(0, 10);
            assertThat(actual).isNotNull();
            assertThat(actual.getSize()).isZero();
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void givenNotExistentId_whenFindById_thenThrowEntityNotFound() {
            HarvestId harvestId = new HarvestId(2L);
            given(repository.findById(harvestId)).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.findById(harvestId))
                    .withMessage("harvest with id 2 not found");
        }

        @Test
        void givenExistentId_whenFindById_thenSuccess() {
            HarvestId harvestId = new HarvestId(2L);
            given(repository.findById(harvestId)).willReturn(Optional.of(harvest));
            given(mapper.toResponseDto(harvest)).willReturn(new HarvestResponseDto(harvest.getId().value(), harvest.getDate(), harvest.getSeason(), null));

            HarvestResponseDto actual = underTest.findById(harvestId);

            assertThat(actual).isNotNull();
            assertThat(actual.date()).isEqualTo(harvest.getDate());
        }
    }

    @Nested
    class CreateTests {
        @Test
        void givenHarvestAlreadyExistsInSameSeason_whenCreate_thenThrowAlreadyExists() {
            HarvestRequestDto request = new HarvestRequestDto(harvest.getDate());
            Season season = Season.fromMonth(harvest.getDate());
            given(repository.existsBySeason(season)).willReturn(true);

            assertThatExceptionOfType(AlreadyExistsException.class)
                    .isThrownBy(() -> underTest.create(request))
                    .withMessage(String.format("Already Exists A harvest in this season: %s, in date %s", season, request.date()));
        }

        @Test
        void givenValidRequest_whenCreate_thenSuccess() {
            HarvestRequestDto request = new HarvestRequestDto(harvest.getDate());
            Season season = Season.fromMonth(harvest.getDate());
            given(repository.existsBySeason(season)).willReturn(false);
            given(repository.save(any(Harvest.class))).willReturn(harvest);
            given(mapper.toResponseDto(harvest)).willReturn(new HarvestResponseDto(harvest.getId().value(), harvest.getDate(), harvest.getSeason(), null));

            HarvestResponseDto actual = underTest.create(request);

            assertThat(actual).isNotNull();
            assertThat(actual.season()).isEqualTo(season);
        }
    }

}