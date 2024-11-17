package com.wora.citronix.farm.application.dto.response;

import com.wora.citronix.farm.application.dto.embeddable.FarmEmbeddableDto;
import com.wora.citronix.tree.application.dto.TreeEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record FieldResponseDto(@NotNull Long id,
                               @NotBlank String name,
                               @NotNull @Positive Double area,
                               FarmEmbeddableDto farm,
                               List<TreeEmbeddableDto> trees
) {
}
