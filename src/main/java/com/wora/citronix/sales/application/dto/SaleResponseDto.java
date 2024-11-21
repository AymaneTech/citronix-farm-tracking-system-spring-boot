package com.wora.citronix.sales.application.dto;

import com.wora.citronix.harvest.application.dto.embeddable.HarvestEmbeddableDto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SaleResponseDto(@NotNull Long id,
                              @NotNull LocalDate date,
                              @NotNull Double unitPrice,
                              @NotNull Double income,
                              @NotNull HarvestEmbeddableDto harvest
) {
}
