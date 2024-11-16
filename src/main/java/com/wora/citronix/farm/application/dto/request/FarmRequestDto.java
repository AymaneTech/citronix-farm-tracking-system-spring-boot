package com.wora.citronix.farm.application.dto.request;

import com.wora.citronix.common.application.validation.UniqueField;
import com.wora.citronix.farm.domain.entity.Farm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FarmRequestDto(
        @NotBlank @UniqueField(entityClass = Farm.class, fieldName = "name", message = "farm name already taken")
        String name,

        @NotBlank
        String location,

        @NotNull @Positive
        Double area
) {
}
