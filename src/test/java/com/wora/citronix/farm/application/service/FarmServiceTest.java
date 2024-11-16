package com.wora.citronix.farm.application.service;

import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.farm.application.dto.request.FarmRequestDto;
import com.wora.citronix.farm.application.dto.response.FarmResponseDto;
import com.wora.citronix.farm.application.mapper.FarmMapper;
import com.wora.citronix.farm.application.service.impl.DefaultFarmService;
import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.repository.FarmRepository;
import com.wora.citronix.farm.domain.vo.FarmId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class FarmServiceTest {
    @Mock
    private FarmRepository repository;
    @Mock
    private FarmMapper mapper;
    @Mock
    private FieldService fieldService;
    private FarmService underTest;

    private Farm farm;
    private FarmId farmId;

    @BeforeEach
    void setup() {
        underTest = new DefaultFarmService(repository, fieldService, mapper);
        farm = new Farm(1L, "happy farm", "marrakech", 2000.0);
        farmId = new FarmId(993L);

    }

    @Nested
    class FindAllTests {
        @Test
        void givenFarmDoesNotExists_whenFindAll_thenReturnEmptyList() {
            given(repository.findAll(any(PageRequest.class)))
                    .willReturn(new PageImpl<>(List.of()));

            Page<FarmResponseDto> actual = underTest.findAll(1, 10);
            assertThat(actual.getSize()).isZero();
        }

        @Test
        void givenFarmsListExists_whenFindAll_thenReturnFarmsList() {
            given(repository.findAll(any(PageRequest.class)))
                    .willReturn(new PageImpl<>(List.of(farm)));
            given(mapper.toResponseDto(farm)).willAnswer(invocation -> {
                Farm f = invocation.getArgument(0);
                return new FarmResponseDto(f.getId().value(), f.getName(), f.getLocation(), f.getArea(), List.of());
            });

            Page<FarmResponseDto> actual = underTest.findAll(1, 10);

            assertThat(actual.getSize()).isEqualTo(1);
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void givenNotExistentFarmId_whenFindById_thenThrowEntityNotFoundException() {
            given(repository.findById(farmId)).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.findById(farmId))
                    .withMessage("farm with id 993 not found");
        }

        @Test
        void givenExistentFarmId_whenFindById_thenReturnFarm() {
            given(repository.findById(farm.getId())).willReturn(Optional.of(farm));
            given(mapper.toResponseDto(farm)).willAnswer(invocation -> {
                Farm f = invocation.getArgument(0);
                return new FarmResponseDto(f.getId().value(), f.getName(), f.getLocation(), f.getArea(), List.of());
            });

            FarmResponseDto actual = underTest.findById(farm.getId());

            assertThat(actual.id()).isEqualTo(farm.getId().value());
            assertThat(actual.name()).isEqualTo(farm.getName());
        }
    }

    @Nested
    class CreateTests {
        @Test
        void givenValidFarm_whenCreate_thenReturnCreatedFarm() {
            FarmRequestDto request = new FarmRequestDto("happy farm", "marrakech", 2000.0, null);

            given(mapper.toEntity(request)).willReturn(farm);
            given(repository.save(any(Farm.class))).willReturn(farm);
            given(mapper.toResponseDto(farm)).willAnswer(invocation -> {
                Farm f = invocation.getArgument(0);
                return new FarmResponseDto(f.getId().value(), f.getName(), f.getLocation(), f.getArea(), List.of());
            });

            FarmResponseDto actual = underTest.create(request);

            assertThat(actual.name()).isEqualTo(request.name());
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void givenNotExistentId_whenUpdate_thenThrowEntityNotFoundException() {
            FarmRequestDto request = new FarmRequestDto("udpate farm", "farm city", 2000.0, null);
            given(repository.findById(farmId)).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.update(farmId, request))
                    .withMessage("farm with id 993 not found");
        }

        @Test
        void givenExistentIdAndValidRequest_whenUpdate_thenReturnFarm() {
            FarmRequestDto request = new FarmRequestDto("udpate farm", "farm city", 2000.0, null);

            given(repository.findById(farmId)).willReturn(Optional.of(farm));
            given(mapper.toResponseDto(farm)).willAnswer(invocation -> {
                Farm f = invocation.getArgument(0);
                return new FarmResponseDto(f.getId().value(), f.getName(), f.getLocation(), f.getArea(), List.of());
            });

            FarmResponseDto actual = underTest.update(farmId, request);

            assertThat(actual.name()).isEqualTo(request.name());
            assertThat(actual.area()).isEqualTo(request.area());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void givenExistentId_whenDelete_thenDeleteFarm() {
            given(repository.existsById(farmId)).willReturn(true);

            underTest.delete(farmId);

            verify(repository).deleteById(farmId);
        }

        @Test
        void givenNotExistentId_whenDelete_thenThrowEntityNotFoundException() {
            given(repository.existsById(farmId)).willReturn(false);
            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.delete(farmId))
                    .withMessage("farm with id 993 not found");

        }
    }
}