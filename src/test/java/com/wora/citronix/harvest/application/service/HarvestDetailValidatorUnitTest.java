package com.wora.citronix.harvest.application.service;

import com.wora.citronix.common.domain.exception.BusinessValidationException;
import com.wora.citronix.farm.domain.entity.Field;
import com.wora.citronix.harvest.application.dto.embeddable.HarvestEmbeddableDto;
import com.wora.citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import com.wora.citronix.harvest.application.service.impl.DefaultHarvestDetailValidator;
import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.entity.HarvestDetail;
import com.wora.citronix.harvest.domain.repository.HarvestDetailRepository;
import com.wora.citronix.harvest.domain.vo.HarvestDetailId;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.harvest.domain.vo.Season;
import com.wora.citronix.tree.application.dto.TreeEmbeddableDto;
import com.wora.citronix.tree.domain.Level;
import com.wora.citronix.tree.domain.Tree;
import com.wora.citronix.tree.domain.TreeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class HarvestDetailValidatorUnitTest {
    @Mock
    private HarvestDetailRepository repository;
    @InjectMocks
    private DefaultHarvestDetailValidator underTest;

    private HarvestDetail harvestDetail;
    private HarvestDetailResponseDto harvestDetailResponse;
    private HarvestDetailId id;
    private Harvest harvest;
    private Tree tree;
    private LocalDate date;


    @BeforeEach
    void setup() {
        id = new HarvestDetailId(new HarvestId(2L), new TreeId(2L));
        date = LocalDate.of(2024, 2, 3);
        Field field = new Field(2L, "fieldname", 2292.0, null);
        tree = new Tree(date, field).setId(new TreeId(2L));
        harvest = new Harvest(date, Season.fromDate(date)).setId(new HarvestId(2L));
        harvest.setHarvestDetails(new ArrayList<>());

        harvestDetail = new HarvestDetail(harvest, tree, date, 939.0);
        harvestDetailResponse = new HarvestDetailResponseDto(harvestDetail.getQuantity(), harvestDetail.getDate(),
                new TreeEmbeddableDto(tree.getId().value(), date, Level.fromAge(tree.getAge())),
                new HarvestEmbeddableDto(harvest.getId().value(), date, harvest.getSeason(), 883.0));
    }

    @Test
    void givenNotEligibleTree_whenValidateHarvestDetail_thenBusinessValidationException() {
        tree.setAge(22);
        assertThatExceptionOfType(BusinessValidationException.class)
                .isThrownBy(() -> underTest.validateHarvestDetail(tree, harvest, date))
                .withMessage("Tree is not eligible for harvest");
    }

    @Test
    void givenInvalidHarvestSeason_whenValidateDetail_thenThrowBusinessValidationException() {
        LocalDate invalidDate = LocalDate.now().minusMonths(39);
        assertThatExceptionOfType(BusinessValidationException.class)
                .isThrownBy(() -> underTest.validateHarvestDetail(tree, harvest, invalidDate))
                .withMessage("Harvest detail date doesn't match harvest season");
    }

    @Test
    void givenAlreadyHarvestedTree_whenValidateDetail_thenThrowBusinessValidationException() {
        given(repository.existsByTreeIdAndHarvestIdAndHarvestSeason(tree.getId(), harvest.getId(), harvest.getSeason()))
                .willReturn(true);

        assertThatExceptionOfType(BusinessValidationException.class)
                .isThrownBy(() -> underTest.validateHarvestDetail(tree, harvest, date))
                .withMessage("Tree already harvested in the same season");
    }

    // todo: still need to check that the trees of a harvest should not belong to multiple fields

    @Test
    void givenValidHarvestDetail_whenValidateHarvestDetail_thenSuccess() {
        given(repository.existsByTreeIdAndHarvestIdAndHarvestSeason(tree.getId(), harvest.getId(), harvest.getSeason()))
                .willReturn(false);

        underTest.validateHarvestDetail(tree, harvest, date);

        verify(repository).existsByTreeIdAndHarvestIdAndHarvestSeason(tree.getId(), harvest.getId(), harvest.getSeason());
    }

}