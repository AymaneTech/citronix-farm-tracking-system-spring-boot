package com.wora.citronix.harvest.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record HarvestId(
        @GeneratedValue
        Long value) {
}
