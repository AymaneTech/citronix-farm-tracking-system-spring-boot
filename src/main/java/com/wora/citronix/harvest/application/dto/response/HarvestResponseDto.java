package com.wora.citronix.harvest.application.dto.response;

import com.wora.citronix.harvest.application.dto.embeddable.HarvestDetailEmbeddableDto;
import com.wora.citronix.harvest.domain.vo.Season;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record HarvestResponseDto(@NotNull Long id,
                                 @NotNull LocalDate date,
                                 @NotNull Season season,
                                 Double totalQuantity,
                                 List<HarvestDetailEmbeddableDto> harvestDetails) {
}
