package com.wora.citronix.farm.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FieldRequestDto(@NotBlank String name,
                              @NotNull @Positive Double area) {
}
