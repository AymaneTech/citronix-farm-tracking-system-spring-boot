package com.wora.citronix.harvest.application.mapper;

import com.wora.citronix.common.application.mapper.BaseMapper;
import com.wora.citronix.harvest.application.dto.response.HarvestResponseDto;
import com.wora.citronix.harvest.domain.entity.Harvest;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface HarvestMapper {

    HarvestResponseDto toResponseDto(Harvest harvest);
}
