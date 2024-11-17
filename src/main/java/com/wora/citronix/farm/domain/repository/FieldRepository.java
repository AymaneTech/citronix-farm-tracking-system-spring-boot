package com.wora.citronix.farm.domain.repository;

import com.wora.citronix.farm.domain.entity.Field;
import com.wora.citronix.farm.domain.vo.FieldId;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, FieldId> {
    boolean existsByName(@NotBlank String name);
}
