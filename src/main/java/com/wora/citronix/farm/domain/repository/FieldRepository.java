package com.wora.citronix.farm.domain.repository;

import com.wora.citronix.farm.domain.entity.Field;
import com.wora.citronix.farm.domain.vo.FieldId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, FieldId> {
}
