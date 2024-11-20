package com.wora.citronix.harvest.application.service;

import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.farm.domain.entity.Field;
import com.wora.citronix.harvest.application.dto.embeddable.HarvestEmbeddableDto;
import com.wora.citronix.harvest.application.dto.request.HarvestDetailRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import com.wora.citronix.harvest.application.mapper.HarvestDetailMapper;
import com.wora.citronix.harvest.application.service.impl.DefaultHarvestDetailService;
import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.entity.HarvestDetail;
import com.wora.citronix.harvest.domain.repository.HarvestDetailRepository;
import com.wora.citronix.harvest.domain.vo.HarvestDetailId;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.harvest.domain.vo.Season;
import com.wora.citronix.tree.application.dto.TreeEmbeddableDto;
import com.wora.citronix.tree.application.service.TreeService;
import com.wora.citronix.tree.domain.Level;
import com.wora.citronix.tree.domain.Tree;
import com.wora.citronix.tree.domain.TreeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class HarvestDetailServiceUnitTest {
    @Mock
    private HarvestDetailRepository repository;
    @Mock
    private HarvestDetailMapper mapper;
    @Mock
    private TreeService treeService;
    @Mock
    private HarvestService harvestService;
    @Mock
    private HarvestDetailValidator validator;
    private HarvestDetailService underTest;

    private HarvestDetail harvestDetail;
    private HarvestDetailResponseDto harvestDetailResponse;
    private HarvestDetailId id;
    private Harvest harvest;
    private Tree tree;


    @BeforeEach
    void setup() {
        underTest = new DefaultHarvestDetailService(repository, mapper, treeService, harvestService, validator);
        id = new HarvestDetailId(new HarvestId(2L), new TreeId(2L));
        LocalDate date = LocalDate.of(2024, 2, 3);
        Field field = new Field(2L, "fieldname", 2292.0, null);
        tree = new Tree(date, field).setId(new TreeId(2L));
        harvest = new Harvest(date, Season.fromDate(date)).setId(new HarvestId(2L));
        harvest.setHarvestDetails(new ArrayList<>());

        harvestDetail = new HarvestDetail(harvest, tree, date, 939.0);
        harvestDetailResponse = new HarvestDetailResponseDto(harvestDetail.getQuantity(), harvestDetail.getDate(),
                new TreeEmbeddableDto(tree.getId().value(), date, Level.fromAge(tree.getAge())),
                new HarvestEmbeddableDto(harvest.getId().value(), date, harvest.getSeason(), 883.0));
    }

    @Nested
    class FindByIdTests {
        @Test
        void givenExistentId_whenFindById_thenSuccess() {
            given(repository.findById(id)).willReturn(Optional.of(harvestDetail));
            given(mapper.toResponseDto(harvestDetail)).willReturn(harvestDetailResponse);

            HarvestDetailResponseDto actual = underTest.findById(id);

            assertThat(actual).isNotNull();
            assertThat(actual.date()).isEqualTo(harvestDetail.getDate());
        }

        @Test
        void givenNotExistentId_whenFindById_thenThrowEntityNotFoundException() {
            given(repository.findById(id)).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.findById(id))
                    .withMessage(String.format("harvest detail with tree id %s and harvest id %s not found", id.treeId().value(), id.harvestId().value()));
        }
    }

    @Nested
    class AddHarvestDetailTests {
        @Test
        void givenNotExistentHarvestId_whenAdd_thenThrowEntityNotFoundException() {
            HarvestDetailRequestDto request = new HarvestDetailRequestDto(303.0, harvest.getDate());
            given(harvestService.findEntityById(harvest.getId())).willThrow(new EntityNotFoundException("harvest", 2L));

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.add(id, request))
                    .withMessage("harvest with id 2 not found");
        }

        @Test
        void givenNotExistentTreeId_whenAdd_thenThrowEntityNotFoundException() {
            HarvestDetailRequestDto request = new HarvestDetailRequestDto(303.0, harvest.getDate());
            given(harvestService.findEntityById(harvest.getId())).willReturn(harvest);
            given(treeService.findEntityById(tree.getId())).willThrow(new EntityNotFoundException("tree", 2L));

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.add(id, request))
                    .withMessage("tree with id 2 not found");
        }

        @Test
        void givenValidRequest_whenAdd_thenSuccess() {
            HarvestDetailRequestDto request = new HarvestDetailRequestDto(303.0, harvest.getDate());
            given(harvestService.findEntityById(harvest.getId())).willReturn(harvest);
            given(treeService.findEntityById(tree.getId())).willReturn(tree);
            given(repository.save(any(HarvestDetail.class))).willReturn(harvestDetail);
            given(mapper.toResponseDto(harvestDetail)).willReturn(harvestDetailResponse);

            HarvestDetailResponseDto actual = underTest.add(id, request);

            assertThat(actual).isNotNull();
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void givenNotExistentHarvestDetailId_whenUpdate_thenThrowEntityNotFoundException() {
            HarvestDetailRequestDto request = new HarvestDetailRequestDto(303.0, harvest.getDate());
            given(repository.findById(id)).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.update(id, request))
                    .withMessage(String.format("harvest detail with tree id %s and harvest id %s not found", id.treeId().value(), id.harvestId().value()));
        }

        @Test
        void givenExistentHarvestDetailId_whenUpdate_thenSuccess() {
            HarvestDetailRequestDto request = new HarvestDetailRequestDto(303.0, harvest.getDate());
            given(repository.findById(id)).willReturn(Optional.of(harvestDetail));
            given(mapper.toResponseDto(harvestDetail)).willReturn(harvestDetailResponse);

            HarvestDetailResponseDto actual = underTest.update(id, request);

            assertThat(actual).isNotNull();
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void givenNotExistentHarvestDetailId_whenDelete_thenThrowEntityNotFoundException() {
            given(repository.existsById(id)).willReturn(false);

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.delete(id))
                    .withMessage(String.format("harvest detail with tree id %s and harvest id %s not found", id.treeId().value(), id.harvestId().value()));
            verify(repository, never()).deleteById(id);
        }

        @Test
        void givenExistentId_whenDelete_thenSuccess() {
            given(repository.existsById(id)).willReturn(true);

            underTest.delete(id);

            verify(repository).deleteById(id);
        }
    }
}
