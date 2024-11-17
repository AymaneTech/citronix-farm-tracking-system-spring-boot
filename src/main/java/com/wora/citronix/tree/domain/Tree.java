package com.wora.citronix.tree.domain;

import com.wora.citronix.farm.domain.entity.Field;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "trees")

@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class Tree {

    @Getter
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private TreeId id;

    @Getter
    private LocalDate plantingDate;

    @Transient
    private Level level;

    @Transient
    private Double age;

    @Getter
    @ManyToOne
    private Field field;

    public Tree(LocalDate plantingDate, Field field) {
        this.plantingDate = plantingDate;
        this.field = field;
    }

    public Level getLevel() {
        return level != null ? level : Level.fromAge(calculateAge());
    }

    public Double getAge() {
        return age != null ? age : calculateAge();
    }

    private Double calculateAge() {
        LocalDate currentDate = LocalDate.now();
        assert plantingDate != null;
        if (plantingDate.isAfter(currentDate)) {
            return 0.0;
        }
        Period period = Period.between(plantingDate, currentDate);
        int years = period.getYears();
        int months = period.getMonths();
        return years + (months / 12.0);
    }
}
