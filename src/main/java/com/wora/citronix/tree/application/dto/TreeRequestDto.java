package com.wora.citronix.tree.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record TreeRequestDto(@NotNull @Past LocalDate plantingDate,
                             @NotNull Long fieldId) {
}
