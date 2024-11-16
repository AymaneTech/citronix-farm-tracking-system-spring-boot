package com.wora.citronix.common.application.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IdValueObjectMapper.class}
)
public interface BaseMapper<E, R, S> {
    E toEntity(R dto);

    S toResponseDto(E entity);
}
