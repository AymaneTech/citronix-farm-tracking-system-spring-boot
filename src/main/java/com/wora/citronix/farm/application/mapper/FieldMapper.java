package com.wora.citronix.farm.application.mapper;

import com.wora.citronix.common.application.mapper.BaseMapper;
import com.wora.citronix.farm.application.dto.request.FieldRequestDto;
import com.wora.citronix.farm.application.dto.response.FieldResponseDto;
import com.wora.citronix.farm.domain.entity.Field;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface FieldMapper extends BaseMapper<Field, FieldRequestDto, FieldResponseDto> {
}
