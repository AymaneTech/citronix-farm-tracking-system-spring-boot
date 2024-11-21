package com.wora.citronix.sales.domain;

import com.wora.citronix.harvest.domain.vo.HarvestId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, SaleId> {
    boolean existsByHarvestId(HarvestId harvestId);
}
