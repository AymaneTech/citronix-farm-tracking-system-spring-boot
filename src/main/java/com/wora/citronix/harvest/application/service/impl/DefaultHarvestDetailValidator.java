package com.wora.citronix.harvest.application.service.impl;

import com.wora.citronix.common.domain.exception.BusinessValidationException;
import com.wora.citronix.harvest.application.service.HarvestDetailValidator;
import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.repository.HarvestDetailRepository;
import com.wora.citronix.tree.domain.Tree;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DefaultHarvestDetailValidator implements HarvestDetailValidator {
    private final HarvestDetailRepository repository;

    @Override
    public void validateHarvestDetail(Tree tree, Harvest harvest, LocalDate date) {
        validateTreeEligibility(tree);
        validateHarvestSeason(harvest, date);
        validateTreeNotAlreadyHarvested(tree, harvest);
        validateFieldHarvestLimit(tree, harvest);
    }

    private void validateTreeEligibility(Tree tree) {
        if (!tree.isEligible()) {
            throw new BusinessValidationException("Tree is not eligible for harvest");
        }
    }

    private void validateHarvestSeason(Harvest harvest, LocalDate harvestDate) {
        if (!harvest.isInSeason(harvestDate)) {
            throw new BusinessValidationException("Harvest detail date doesn't match harvest season");
        }
    }

    private void validateTreeNotAlreadyHarvested(Tree tree, Harvest harvest) {
        if (repository.existsByTreeIdAndHarvestIdAndHarvestSeason(
                tree.getId(),
                harvest.getId(),
                harvest.getSeason())
        ) {
            throw new BusinessValidationException("Tree already harvested in the same season");
        }
    }

    private void validateFieldHarvestLimit(Tree tree, Harvest harvest) {
        if (repository.existsByFieldIdAndSeasonExcludingHarvest(
                tree.getField().getId(),
                harvest.getSeason(),
                harvest.getId())
        ) {
            throw new BusinessValidationException("Field already has a harvest for this season");
        }
    }
}
