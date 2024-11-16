package com.wora.citronix.farm.domain.entity;

import com.wora.citronix.common.domain.vo.Timestamp;
import com.wora.citronix.farm.domain.vo.FarmId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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

    @OneToMany(mappedBy = "farm")
    private List<Field> fields;
}
