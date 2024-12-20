package com.wora.citronix.farm.domain.entity;

import com.wora.citronix.common.domain.exception.BusinessValidationException;
import com.wora.citronix.common.domain.vo.Timestamp;
import com.wora.citronix.farm.domain.vo.FarmId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "farms")

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Farm {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private FarmId id;

    private String name;

    private String location;

    private Double area;

    @Embedded
    private Timestamp timestamp;

    @OneToMany(mappedBy = "farm", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Field> fields = new ArrayList<>();

    public Farm(Long id, String name, String location, Double area) {
        this.id = new FarmId(id);
        this.name = name;
        this.location = location;
        this.area = area;
    }

    public Farm ensureFieldCountWithinLimit() {
        if (fields != null && fields.size() >= 10)
            throw new BusinessValidationException("maximum fields of a farm is 10");
        return this;
    }

    public Farm ensureFieldAreaWithinFarmCapacity(Double fieldArea) {
        if (fieldArea > (area / 2))
            throw new BusinessValidationException("field area should not be greater than 50% of farm area");

        if (fields != null) {

            double existingFieldsArea = fields.stream()
                    .mapToDouble(Field::getArea)
                    .sum();

            final double remainingArea = (area - existingFieldsArea);
            if (fieldArea > remainingArea)
                throw new BusinessValidationException("farm doesn't have enough space for this field");
        }
        return this;
    }
}
