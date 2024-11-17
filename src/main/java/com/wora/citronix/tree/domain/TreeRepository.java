package com.wora.citronix.tree.domain;

import com.wora.citronix.farm.domain.vo.FieldId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreeRepository extends JpaRepository<Tree, TreeId> {
    int countByFieldId(FieldId id);
}
