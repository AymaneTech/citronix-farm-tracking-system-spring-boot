package com.wora.citronix.harvest.domain.vo;

import com.wora.citronix.tree.domain.TreeId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record HarvestDetailId(@AttributeOverride(name = "value", column = @Column(name = "harvest_id"))
                              HarvestId harvestId,

                              @AttributeOverride(name = "value", column = @Column(name = "tree_id"))
                              TreeId treeId) {
}