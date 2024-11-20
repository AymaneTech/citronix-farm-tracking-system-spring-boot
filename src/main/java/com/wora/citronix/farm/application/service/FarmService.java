package com.wora.citronix.farm.application.service;

import com.wora.citronix.common.application.service.CrudService;
import com.wora.citronix.farm.application.dto.request.FarmRequestDto;
import com.wora.citronix.farm.application.dto.request.FarmSearchRequest;
import com.wora.citronix.farm.application.dto.response.FarmResponseDto;
import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.vo.FarmId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FarmService extends CrudService<FarmId, FarmRequestDto, FarmResponseDto> {
    Page<FarmResponseDto> findAllWithSpecification(Pageable pageRequest, FarmSearchRequest request);

    Farm findEntityById(FarmId id);
}
