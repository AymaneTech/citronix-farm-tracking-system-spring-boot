package com.wora.citronix.harvest.application.dto.response;

import com.wora.citronix.harvest.application.dto.embeddable.HarvestEmbeddableDto;
import com.wora.citronix.tree.application.dto.TreeEmbeddableDto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HarvestDetailResponseDto(@NotNull Double quantity,
                                       @NotNull LocalDate date,
                                       @NotNull TreeEmbeddableDto tree,
                                       @NotNull HarvestEmbeddableDto harvest) {

}
