package com.wora.citronix.farm.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record FarmId(@GeneratedValue Long value) {
}
