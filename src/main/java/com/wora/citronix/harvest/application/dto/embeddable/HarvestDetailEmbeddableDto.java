package com.wora.citronix.harvest.application.dto.embeddable;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HarvestDetailEmbeddableDto(@NotNull Double quantity,
                                         @NotNull LocalDate date
) {
}
