package com.wora.citronix.farm.application.service;

import com.wora.citronix.common.domain.exception.AlreadyExistsException;
import com.wora.citronix.common.domain.exception.EntityCreationException;
import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.farm.application.dto.embeddable.FarmEmbeddableDto;
import com.wora.citronix.farm.application.dto.request.FieldRequestDto;
import com.wora.citronix.farm.application.dto.response.FieldResponseDto;
import com.wora.citronix.farm.application.mapper.FieldMapper;
import com.wora.citronix.farm.application.service.impl.DefaultFieldService;
import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.entity.Field;
import com.wora.citronix.farm.domain.repository.FarmRepository;
import com.wora.citronix.farm.domain.repository.FieldRepository;
import com.wora.citronix.farm.domain.vo.FarmId;
import com.wora.citronix.farm.domain.vo.FieldId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class FieldServiceUnitTest {
    @Mock
    private FieldRepository repository;
    @Mock
    private FieldMapper mapper;
    @Mock
    private FarmRepository farmRepository;

    private FieldService underTest;
    private Field field;
    private Farm farm;

    @BeforeEach
    void setup() {
        this.underTest = new DefaultFieldService(repository, mapper, farmRepository);
        farm = new Farm(1L, "happy farm", "marrakech", 4000.0);
        field = new Field(1L, "happy field", 1400.3, farm);
    }

    @Nested
    class FindById {
        @Test
        void givenNotExistentFieldId_whenFindById_thenThrowEntityNotFoundException() {
            FieldId fieldId = new FieldId(333L);
            given(repository.findById(fieldId)).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.findById(fieldId))
                    .withMessage("field with id 333 not found");
        }

        @Test
        void givenFieldIdExists_whenFindById_thenReturnField() {
            given(repository.findById(field.getId())).willReturn(Optional.of(field));
            given(mapper.toResponseDto(field)).willReturn(new FieldResponseDto(
                    field.getId().value(), field.getName(), field.getArea(),
                    new FarmEmbeddableDto(farm.getId().value(), farm.getName(), farm.getLocation(), farm.getArea()), null));

            FieldResponseDto actual = underTest.findById(field.getId());
            assertThat(actual).isNotNull();
            assertThat(actual.id()).isEqualTo(field.getId().value());
        }
    }

    @Nested
    class CreateTests {
        @Test
        void givenRequestWithInvalidFarmId_whenCreate_thenThrowEntityNotFound() {
            FieldRequestDto request = new FieldRequestDto("new field motherfuckers", 1000.0, 292L);

            given(farmRepository.findById(any(FarmId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.create(request))
                    .withMessage("farm with id 292 not found");
        }

        @Test
        void givenFarmHasReachFieldsLimit_whenCreate_thenThrowEntityCreationException() {
            FieldRequestDto request = new FieldRequestDto("new field motherfuckers", 1000.0, 292L);
            farm.setFields(List.of(
                    new Field(1L, "field 1", 9292.0, farm),
                    new Field(2L, "field 2", 9292.0, farm),
                    new Field(3L, "field 3", 9292.0, farm),
                    new Field(4L, "field 4", 9292.0, farm),
                    new Field(5L, "field 5", 9292.0, farm),
                    new Field(6L, "field 6", 9292.0, farm),
                    new Field(7L, "field 7", 9292.0, farm),
                    new Field(8L, "field 8", 9292.0, farm),
                    new Field(9L, "field 9", 9292.0, farm),
                    new Field(10L, "field 10", 9292.0, farm)
            ));
            given(farmRepository.findById(any(FarmId.class))).willReturn(Optional.of(farm));

            assertThatExceptionOfType(EntityCreationException.class)
                    .isThrownBy(() -> underTest.create(request))
                    .withMessage("maximum fields of a farm is 10");
        }

        @Test
        void givenFieldAreaGreaterThanFarmAreaHalf_whenCreate_thenEntityCreationException() {
            FieldRequestDto request = new FieldRequestDto("new field motherfuckers", 3000.0, 292L);

            given(farmRepository.findById(any(FarmId.class))).willReturn(Optional.of(farm));

            assertThatExceptionOfType(EntityCreationException.class)
                    .isThrownBy(() -> underTest.create(request))
                    .withMessage("field area should not be greater than 50% of farm area");
        }

        @Test
        void givenRemainingAreaIsNotEnough_whenCreate_thenThrowEntityCreationException() {
            FieldRequestDto request = new FieldRequestDto("new field motherfuckers", 1400.0, 292L);
            farm.setFields(List.of(
                    new Field(1L, "field 1", 1000.0, farm),
                    new Field(2L, "field 2", 2000.0, farm)
            ));

            given(farmRepository.findById(any(FarmId.class))).willReturn(Optional.of(farm));
            assertThatExceptionOfType(EntityCreationException.class)
                    .isThrownBy(() -> underTest.create(request))
                    .withMessage("farm doesn't have enough space for tis field");
        }

        @Test
        void givenFieldNameAlreadyExists_whenCreate_thenThrowAlreadyExistsException() {
            FieldRequestDto request = new FieldRequestDto("new field motherfuckers", 1400.0, 292L);

            given(farmRepository.findById(any(FarmId.class))).willReturn(Optional.of(farm));
            given(repository.existsByName(request.name())).willReturn(true);

            assertThatExceptionOfType(AlreadyExistsException.class)
                    .isThrownBy(() -> underTest.create(request))
                    .withMessage("Field Name Already Taken in Farm: happy farm");
        }

        @Test
        void givenValidRequest_whenCreate_thenCreateField() {
            FieldRequestDto request = new FieldRequestDto("new field motherfuckers", 1400.0, 292L);
            Field expected = new Field(1L, request.name(), request.area(), farm);

            given(farmRepository.findById(any(FarmId.class))).willReturn(Optional.of(farm));
            given(repository.existsByName(request.name())).willReturn(false);
            given(mapper.toEntity(request)).willReturn(expected);
            given(repository.save(expected)).willReturn(expected);
            given(mapper.toResponseDto(expected)).willReturn(new FieldResponseDto(1L, request.name(), request.area(), null, null));

            FieldResponseDto actual = underTest.create(request);

            assertThat(actual).isNotNull();
            assertThat(actual.name()).isEqualTo(request.name());
        }
    }

    @Nested
    class SaveFarmFieldsTests {
        @Test
        void givenFieldsCountExceedsLimit_whenSaveFarmFields_thenThrowEntityCreationException() {
            farm.setFields(List.of(
                    new Field(1L, "field 1", 100.0, farm),
                    new Field(2L, "field 2", 100.0, farm),
                    new Field(3L, "field 3", 100.0, farm),
                    new Field(4L, "field 4", 100.0, farm),
                    new Field(5L, "field 5", 100.0, farm),
                    new Field(6L, "field 6", 100.0, farm),
                    new Field(7L, "field 7", 100.0, farm),
                    new Field(8L, "field 8", 100.0, farm),
                    new Field(9L, "field 9", 100.0, farm),
                    new Field(10L, "field 10", 100.0, farm),
                    new Field(11L, "field 11", 100.0, farm)
            ));

            assertThatExceptionOfType(EntityCreationException.class)
                    .isThrownBy(() -> underTest.saveFarmFields(farm))
                    .withMessage("maximum fields of a farm is 10");
        }

        @Test
        void givenFieldAreaGreaterThanFarmAreaHalf_whenSaveFarmFields_thenThrowEntityCreationException() {
            farm.setFields(List.of(
                    new Field(1L, "field 1", 3000.0, farm)
            ));

            assertThatExceptionOfType(EntityCreationException.class)
                    .isThrownBy(() -> underTest.saveFarmFields(farm))
                    .withMessage("field area should not be greater than 50% of farm area");
        }

        @Test
        void givenTotalFieldAreaExceedsFarmArea_whenSaveFarmFields_thenThrowEntityCreationException() {
            farm.setFields(List.of(
                    new Field(1L, "field 1", 2000.0, farm),
                    new Field(2L, "field 2", 2000.0, farm),
                    new Field(3L, "field 3", 1500.0, farm)
            ));

            assertThatExceptionOfType(EntityCreationException.class)
                    .isThrownBy(() -> underTest.saveFarmFields(farm))
                    .withMessage("farm doesn't have enough space for tis field");
        }

        @Test
        void givenFieldsWithSameName_whenSaveFarmFields_thenThrowIllegalArgumentException() {
            farm.setFields(List.of(
                    new Field(1L, "field 1", 100.0, farm),
                    new Field(2L, "field 1", 100.0, farm)
            ));

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> underTest.saveFarmFields(farm))
                    .withMessage("Duplicate field names found: [field 1]");
        }

        @Test
        void givenValidFields_whenSaveFarmFields_thenSaveAllFields() {
            List<Field> fields = List.of(
                    new Field(1L, "field 1", 1000.0, farm),
                    new Field(2L, "field 2", 1000.0, farm)
            );
            farm.setFields(fields);

            given(repository.saveAll(fields)).willReturn(fields);

            List<Field> savedFields = underTest.saveFarmFields(farm);

            assertThat(savedFields)
                    .isNotNull()
                    .hasSize(2)
                    .isEqualTo(fields);

            verify(repository).saveAll(fields);
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void givenInvalidFieldId_whenUpdate_thenThrowEntityNotFoundException() {
            FieldRequestDto request = new FieldRequestDto("updated field", 1000.0, 292L);
            FieldId fieldId = new FieldId(999L);

            given(repository.findById(fieldId)).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.update(fieldId, request))
                    .withMessage("field with id 999 not found");
        }

        @Test
        void givenNewAreaGreaterThanFarmAreaHalf_whenUpdate_thenThrowEntityCreationException() {
            FieldRequestDto request = new FieldRequestDto("updated field", 3000.0, 292L);
            FieldId fieldId = new FieldId(1L);
            Field existingField = new Field(1L, "old field", 1000.0, farm);

            given(repository.findById(fieldId)).willReturn(Optional.of(existingField));

            assertThatExceptionOfType(EntityCreationException.class)
                    .isThrownBy(() -> underTest.update(fieldId, request))
                    .withMessage("field area should not be greater than 50% of farm area");
        }

        @Test
        void givenNewAreaExceedsFarmRemainingSpace_whenUpdate_thenThrowEntityCreationException() {
            FieldId fieldId = new FieldId(1L);
            Field existingField = new Field(1L, "field 1", 1000.0, farm);
            farm.setFields(List.of(
                    existingField,
                    new Field(2L, "field 2", 2000.0, farm)
            ));

            FieldRequestDto request = new FieldRequestDto("updated field", 2000.0, 292L);

            given(repository.findById(fieldId)).willReturn(Optional.of(existingField));

            assertThatExceptionOfType(EntityCreationException.class)
                    .isThrownBy(() -> underTest.update(fieldId, request))
                    .withMessage("farm doesn't have enough space for tis field");
        }

        @Test
        void givenSameArea_whenUpdate_thenSkipAreaValidationAndUpdateName() {
            FieldId fieldId = new FieldId(1L);
            Field existingField = new Field(1L, "old field", 1000.0, farm);
            FieldRequestDto request = new FieldRequestDto("updated field", 1000.0, 292L);

            given(repository.findById(fieldId)).willReturn(Optional.of(existingField));
            given(mapper.toResponseDto(any(Field.class)))
                    .willReturn(new FieldResponseDto(1L, request.name(), request.area(), null, null));

            FieldResponseDto response = underTest.update(fieldId, request);

            assertThat(response)
                    .isNotNull()
                    .satisfies(dto -> {
                        assertThat(dto.name()).isEqualTo("updated field");
                        assertThat(dto.area()).isEqualTo(1000.0);
                    });

        }

        @Test
        void givenValidNewAreaAndName_whenUpdate_thenUpdateField() {
            FieldId fieldId = new FieldId(1L);
            Field existingField = new Field(1L, "old field", 1000.0, farm);
            FieldRequestDto request = new FieldRequestDto("updated field", 1500.0, 292L);

            given(repository.findById(fieldId)).willReturn(Optional.of(existingField));
            given(mapper.toResponseDto(any(Field.class)))
                    .willReturn(new FieldResponseDto(1L, request.name(), request.area(), null, null));

            FieldResponseDto response = underTest.update(fieldId, request);

            assertThat(response)
                    .isNotNull()
                    .satisfies(dto -> {
                        assertThat(dto.name()).isEqualTo("updated field");
                        assertThat(dto.area()).isEqualTo(1500.0);
                    });
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void givenExistentId_whenDelete_thenDeleteFarm() {
            given(repository.existsById(field.getId())).willReturn(true);

            underTest.delete(field.getId());

            verify(repository).deleteById(field.getId());
        }

        @Test
        void givenNotExistentId_whenDelete_thenThrowEntityNotFoundException() {
            given(repository.existsById(field.getId())).willReturn(false);
            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.delete(field.getId()))
                    .withMessage("field with id 1 not found");

        }
    }
}