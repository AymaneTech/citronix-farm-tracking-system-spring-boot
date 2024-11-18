package com.wora.citronix.harvest.domain.entity;

import com.wora.citronix.harvest.domain.vo.HarvestDetailId;
import com.wora.citronix.tree.domain.Tree;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Entity
@Table(name = "harvest_details")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class HarvestDetail {

    @EmbeddedId
    private HarvestDetailId id;

    private Double quantity;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "harvest_id", insertable = false, updatable = false)
    private Harvest harvest;

    @ManyToOne
    @JoinColumn(name = "tree_id", insertable = false, updatable = false)
    private Tree tree;

    public HarvestDetail(Harvest harvest, Tree tree, LocalDate date, Double quantity) {
        this.harvest = harvest;
        this.tree = tree;
        this.date = date;
        this.quantity = quantity;
    }
}
