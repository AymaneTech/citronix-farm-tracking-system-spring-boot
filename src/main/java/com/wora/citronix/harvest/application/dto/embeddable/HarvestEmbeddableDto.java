package com.wora.citronix.harvest.application.dto.embeddable;

import com.wora.citronix.harvest.domain.vo.Season;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HarvestEmbeddableDto(@NotNull LocalDate date,
                                   @NotNull Season season,
                                   @NotNull Double totalQuantity) {
}
