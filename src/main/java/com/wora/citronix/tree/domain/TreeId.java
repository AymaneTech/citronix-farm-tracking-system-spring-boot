package com.wora.citronix.tree.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record TreeId(@GeneratedValue Long value) {
}
