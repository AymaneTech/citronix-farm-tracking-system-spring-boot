package com.wora.citronix.tree.application.service;

import com.wora.citronix.common.domain.exception.BusinessValidationException;
import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.farm.application.service.FieldService;
import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.entity.Field;
import com.wora.citronix.farm.domain.vo.FieldId;
import com.wora.citronix.tree.application.dto.TreeRequestDto;
import com.wora.citronix.tree.application.dto.TreeResponseDto;
import com.wora.citronix.tree.application.mapper.TreeMapper;
import com.wora.citronix.tree.domain.Level;
import com.wora.citronix.tree.domain.Tree;
import com.wora.citronix.tree.domain.TreeId;
import com.wora.citronix.tree.domain.TreeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class TreeServiceUnitTest {
    @Mock
    private TreeRepository repository;
    @Mock
    private FieldService fieldService;
    @Mock
    private TreeMapper mapper;
    private TreeService underTest;

    private Tree tree;
    private Field field;
    private LocalDate plantingDate;

    @BeforeEach
    void setup() {
        underTest = new DefaultTreeService(repository, fieldService, mapper);
        plantingDate = LocalDate.of(2023, 3, 27);
        field = new Field(1L, "happy field", 1000.0, new Farm());
        tree = new Tree(plantingDate, field).setId(new TreeId(2L));
    }

    @Nested
    class PlantTests {
        @Test
        void givenDateOutOfPlantingPeriod_whenPlant_thenThrowEntityCreationException() {
            TreeRequestDto request = new TreeRequestDto(LocalDate.now(), 8L);

            assertThatExceptionOfType(BusinessValidationException.class)
                    .isThrownBy(() -> underTest.plant(request))
                    .withMessage("You can create tree only in (March, April, May)");
        }

        @Test
        void givenNotExistentFieldId_whenPlant_thenThrowEntityCreationException() {
            TreeRequestDto request = new TreeRequestDto(LocalDate.of(2024, 3, 27), 8L);
            given(fieldService.findEntityById(any(FieldId.class))).willThrow(EntityNotFoundException.class);

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.plant(request));
        }

        @Test
        void givenTreeAndMaxTreeIsReached_whenPlant_thenThrowEntityCreationException() {
            TreeRequestDto request = new TreeRequestDto(LocalDate.of(2024, 3, 27), 8L);
            given(fieldService.findEntityById(any(FieldId.class))).willReturn(field);
            given(repository.countByFieldId(field.getId())).willReturn(10);

            assertThatExceptionOfType(BusinessValidationException.class)
                    .isThrownBy(() -> underTest.plant(request))
                    .withMessage("Field has reached maximum capacity of 10 trees (current: 10)");
        }

        @Test
        void givenValidRequest_whenPlant_thenSuccess() {
            TreeRequestDto request = new TreeRequestDto(LocalDate.of(2022, 3, 27), 8L);
            given(fieldService.findEntityById(any(FieldId.class))).willReturn(field);
            given(repository.countByFieldId(any(FieldId.class))).willReturn(9);
            given(repository.save(any(Tree.class))).willReturn(tree);
            given(mapper.toResponseDto(tree)).willReturn(new TreeResponseDto(1L, tree.getPlantingDate(), tree.getLevel(), tree.getAge(), null));

            TreeResponseDto actual = underTest.plant(request);

            assertThat(actual).isNotNull();
            assertThat(actual.level()).isEqualTo(Level.YOUNG);
            assertThat(actual.age()).isEqualTo(1);
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void givenDateOutOfPlantingPeriod_whenUpdate_thenThrowEntityCreationException() {
            TreeId treeId = new TreeId(8L);
            TreeRequestDto request = new TreeRequestDto(LocalDate.now(), 8L);
            given(repository.findById(treeId)).willReturn(Optional.of(tree));

            assertThatExceptionOfType(BusinessValidationException.class)
                    .isThrownBy(() -> underTest.update(treeId, request))
                    .withMessage("You can create tree only in (March, April, May)");
        }

        @Test
        void givenNotExistentTreeId_whenUpdate_thenThrowEntityCreationException() {
            TreeId treeId = new TreeId(8L);
            TreeRequestDto request = new TreeRequestDto(LocalDate.of(2024, 3, 27), 8L);
            given(repository.findById(treeId)).willReturn(Optional.empty());


            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.update(treeId, request))
                    .withMessage("tree with id 8 not found");
        }

        @Test
        void givenNotExistentFieldId_whenUpdate_thenThrowEntityCreationException() {
            TreeId treeId = new TreeId(8L);
            TreeRequestDto request = new TreeRequestDto(LocalDate.of(2024, 3, 27), 8L);
            given(repository.findById(treeId)).willReturn(Optional.of(tree));
            given(fieldService.findEntityById(any(FieldId.class))).willThrow(new EntityNotFoundException("field", 8L));


            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.update(treeId, request))
                    .withMessage("field with id 8 not found");
        }

        @Test
        void givenValidRequest_whenPlant_thenSuccess() {
            TreeId treeId = new TreeId(2L);
            TreeRequestDto request = new TreeRequestDto(LocalDate.of(2022, 3, 27), 8L);
            given(repository.findById(treeId)).willReturn(Optional.of(tree));
            given(fieldService.findEntityById(any(FieldId.class))).willReturn(field);
            given(mapper.toResponseDto(tree)).willReturn(new TreeResponseDto(1L, tree.getPlantingDate(), tree.getLevel(), tree.getAge(), null));

            TreeResponseDto actual = underTest.update(treeId, request);

            assertThat(actual).isNotNull();
            assertThat(actual.level()).isEqualTo(Level.YOUNG);
            assertThat(actual.age()).isEqualTo(1);
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void givenExistentId_whenDelete_thenDeleteFarm() {
            given(repository.existsById(tree.getId())).willReturn(true);

            underTest.delete(tree.getId());

            verify(repository).deleteById(tree.getId());
        }

        @Test
        void givenNotExistentId_whenDelete_thenThrowEntityNotFoundException() {
            given(repository.existsById(tree.getId())).willReturn(false);
            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.delete(tree.getId()))
                    .withMessage("tree with id 2 not found");

        }
    }
}