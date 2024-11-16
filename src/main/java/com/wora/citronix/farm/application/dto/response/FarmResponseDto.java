package com.wora.citronix.farm.application.dto.response;

import com.wora.citronix.farm.application.dto.embeddable.FieldEmbeddableDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record FarmResponseDto(@NotNull Long id,
                              @NotBlank String name,
                              @NotBlank String location,
                              @NotNull @Positive Double area,
                              @NotNull List<FieldEmbeddableDto> fields
) {
}
