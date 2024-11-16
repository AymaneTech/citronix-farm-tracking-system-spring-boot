package com.wora.citronix.farm.application.dto.embeddable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FarmEmbeddableDto(@NotNull Long id,
                                @NotBlank String name,
                                @NotBlank String location,
                                @NotNull Double area) {
}
