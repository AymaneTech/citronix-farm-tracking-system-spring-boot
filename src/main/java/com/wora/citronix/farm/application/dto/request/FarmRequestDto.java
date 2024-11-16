package com.wora.citronix.farm.application.dto.request;

import com.wora.citronix.common.application.validation.UniqueField;
import com.wora.citronix.farm.domain.entity.Farm;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record FarmRequestDto(
        @NotBlank @UniqueField(entityClass = Farm.class, fieldName = "name", message = "farm name already taken")
        String name,

        @NotBlank
        String location,

        @NotNull @Positive @Min(2000)
        Double area,

        List<FieldRequestDto> fields
) {
}
