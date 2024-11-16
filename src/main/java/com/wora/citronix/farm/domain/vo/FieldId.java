package com.wora.citronix.farm.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record FieldId(@GeneratedValue Long value) {
}
