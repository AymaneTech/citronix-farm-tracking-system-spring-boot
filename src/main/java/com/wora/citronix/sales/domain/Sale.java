package com.wora.citronix.sales.domain;

import com.wora.citronix.harvest.domain.entity.Harvest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Entity
@Table(name = "sales")

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class Sale {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private SaleId id;

    private LocalDate date;

    private Double unitPrice;

    @Transient
    private Double income;

    @OneToOne
    private Harvest harvest;

    public Double getIncome(){
        return calculateIncome();
    }

    public Double calculateIncome() {
        return unitPrice * harvest.getTotalQuantity();
    }
}
