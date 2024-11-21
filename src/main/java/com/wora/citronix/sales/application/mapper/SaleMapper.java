package com.wora.citronix.sales.application.mapper;

import com.wora.citronix.common.application.mapper.BaseMapper;
import com.wora.citronix.sales.application.dto.SaleRequestDto;
import com.wora.citronix.sales.application.dto.SaleResponseDto;
import com.wora.citronix.sales.domain.Sale;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface SaleMapper extends BaseMapper<Sale, SaleRequestDto, SaleResponseDto> {
}
