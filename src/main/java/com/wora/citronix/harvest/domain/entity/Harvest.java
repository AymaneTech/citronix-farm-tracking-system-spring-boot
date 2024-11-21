package com.wora.citronix.harvest.domain.entity;

import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.harvest.domain.vo.Season;
import com.wora.citronix.sales.domain.Sale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "harvests")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Harvest {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private HarvestId id;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Season season;

    private Double totalQuantity;

    @OneToMany(mappedBy = "harvest", fetch = FetchType.EAGER)
    private List<HarvestDetail> harvestDetails;

    @OneToOne(mappedBy = "harvest")
    private Sale sale;

    public Harvest(LocalDate date, Season season) {
        this.date = date;
        this.season = season;
    }

    public boolean isInSeason(LocalDate date) {
        return season.matches(date);
    }

    public void addNewHarvestDetail(HarvestDetail harvestDetail) {
        if (harvestDetails == null) harvestDetails = new ArrayList<>();

        this.harvestDetails.add(harvestDetail);
        this.totalQuantity = harvestDetails.stream()
                .mapToDouble(HarvestDetail::getQuantity)
                .sum();
    }
}
