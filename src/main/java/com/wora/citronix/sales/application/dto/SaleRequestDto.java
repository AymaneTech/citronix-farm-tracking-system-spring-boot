package com.wora.citronix.sales.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SaleRequestDto(@NotNull LocalDate date,
                             @NotNull Double unitPrice,
                             @NotNull Long harvestId
) {
}
