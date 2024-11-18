package com.wora.citronix.harvest.application.dto.response;

import com.wora.citronix.harvest.domain.vo.Season;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HarvestResponseDto(@NotNull LocalDate date,
                                 @NotNull Season season,
                                 Double totalQuantity) {
    // todo: add here the embeddable dto of the harvest details that contain informations about the tree
}
