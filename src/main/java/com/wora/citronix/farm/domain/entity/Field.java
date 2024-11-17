package com.wora.citronix.farm.domain.entity;

import com.wora.citronix.farm.domain.vo.FieldId;
import com.wora.citronix.tree.domain.Tree;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "fields")

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
@Builder
public class Field {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private FieldId id;

    private String name;

    private Double area;

    @ManyToOne
    private Farm farm;

    @OneToMany(mappedBy = "field")
    private List<Tree> trees;

    public Field(FieldId id, String name, Double area, Farm farm) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.farm = farm;
    }

    public Field(Long id, String name, Double area, Farm farm) {
        this.id = new FieldId(id);
        this.name = name;
        this.area = area;
        this.farm = farm;
    }

    public int getMaxTreesForField() {
        return (int) (area / 10_000) * 100;
    }

    public boolean hasCapacityForNewTree(int currentTreeCount) {
        return currentTreeCount < getMaxTreesForField();
    }
}
