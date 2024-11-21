package com.wora.citronix.sales.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SaleEmbeddedDto(@NotNull Long id,
                              @NotNull LocalDate date,
                              @NotNull Double unitPrice,
                              @NotNull Double income) {
}
