package com.wora.citronix.farm.application.mapper;

import com.wora.citronix.common.application.mapper.BaseMapper;
import com.wora.citronix.farm.application.dto.request.FarmRequestDto;
import com.wora.citronix.farm.application.dto.response.FarmResponseDto;
import com.wora.citronix.farm.domain.entity.Farm;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface FarmMapper extends BaseMapper<Farm, FarmRequestDto, FarmResponseDto> {
}
