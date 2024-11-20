package com.wora.citronix.harvest.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record HarvestDetailRequestDto(@NotNull Double quantity,
                                      @NotNull @PastOrPresent LocalDate date
) {

}
