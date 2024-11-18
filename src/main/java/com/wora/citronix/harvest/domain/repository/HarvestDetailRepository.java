package com.wora.citronix.harvest.domain.repository;

import com.wora.citronix.harvest.domain.entity.HarvestDetail;
import com.wora.citronix.harvest.domain.vo.HarvestDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, HarvestDetailId> {
}
