package com.wora.citronix.harvest.application.mapper;

import com.wora.citronix.common.application.mapper.BaseMapper;
import com.wora.citronix.harvest.application.dto.request.HarvestDetailRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import com.wora.citronix.harvest.application.dto.response.HarvestResponseDto;
import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.entity.HarvestDetail;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface HarvestDetailMapper {

    HarvestDetailResponseDto toResponseDto(HarvestDetail harvest);
}
