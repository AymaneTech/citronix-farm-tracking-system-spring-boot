package com.wora.citronix.farm.domain.repository;

import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.vo.FarmId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<Farm, FarmId> {
}
