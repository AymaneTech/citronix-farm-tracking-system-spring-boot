package com.wora.citronix.farm.application.service;

import com.wora.citronix.common.application.service.CrudService;
import com.wora.citronix.farm.application.dto.request.FarmRequestDto;
import com.wora.citronix.farm.application.dto.response.FarmResponseDto;
import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.vo.FarmId;

public interface FarmService extends CrudService<FarmId, FarmRequestDto, FarmResponseDto> {
    Farm findEntityById(FarmId id);
}
