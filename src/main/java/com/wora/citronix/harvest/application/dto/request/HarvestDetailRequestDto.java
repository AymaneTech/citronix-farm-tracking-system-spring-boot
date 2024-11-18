package com.wora.citronix.harvest.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HarvestDetailRequestDto(@NotNull Double quantity,
                                      @NotNull LocalDate date
) {

}
