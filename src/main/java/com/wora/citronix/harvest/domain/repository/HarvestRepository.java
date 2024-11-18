package com.wora.citronix.harvest.domain.repository;

import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.harvest.domain.vo.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarvestRepository extends JpaRepository<Harvest, HarvestId> {
    boolean existsBySeason(Season season);
}
