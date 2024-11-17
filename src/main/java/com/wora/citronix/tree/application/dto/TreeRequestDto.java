package com.wora.citronix.tree.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TreeRequestDto(@NotNull LocalDate plantingDate,
                             @NotNull Long fieldId) {
}
