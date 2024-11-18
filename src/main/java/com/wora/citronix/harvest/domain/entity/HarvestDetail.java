package com.wora.citronix.harvest.domain.entity;

import com.wora.citronix.harvest.domain.vo.HarvestDetailId;
import com.wora.citronix.tree.domain.Tree;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "harvest_details")

@Getter
@Setter
@Accessors(chain = true)
public class HarvestDetail {

    @EmbeddedId
    private HarvestDetailId id;

    private Double quantity;

    @ManyToOne
    @JoinColumn(name = "harvest_id", insertable = false, updatable = false)
    private Harvest harvest;

    @ManyToOne
    @JoinColumn(name = "tree_id", insertable = false, updatable = false)
    private Tree tree;
}
