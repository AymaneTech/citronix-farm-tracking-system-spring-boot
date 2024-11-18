package com.wora.citronix.harvest.application.service;

import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.tree.domain.Tree;

import java.time.LocalDate;

public interface HarvestDetailValidator {
    void validateHarvestDetail(Tree tree, Harvest harvest, LocalDate harvestDate);
}
