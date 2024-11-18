package com.wora.citronix.harvest.domain.entity;

import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.harvest.domain.vo.Season;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

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

   private Season season;

   private Double totalQuantity;
}
