package com.wora.citronix.tree.domain;

import com.wora.citronix.farm.domain.entity.Field;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Entity
@Table(name = "trees")

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class Tree {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private TreeId id;

    private LocalDate plantingDate;

    private Productivity productivity;

    @Transient
    private Double age;

    @ManyToOne
    private Field field;
}
