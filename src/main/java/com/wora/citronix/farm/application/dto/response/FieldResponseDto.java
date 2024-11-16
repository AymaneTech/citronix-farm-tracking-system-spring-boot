package com.wora.citronix.farm.application.dto.response;

import com.wora.citronix.farm.application.dto.embeddable.FarmEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FieldResponseDto(@NotNull Long id,
                               @NotBlank String name,
                               @NotNull @Positive Double area,
                               FarmEmbeddableDto farm
) {
}
