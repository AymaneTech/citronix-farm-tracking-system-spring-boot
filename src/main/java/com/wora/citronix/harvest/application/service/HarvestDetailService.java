package com.wora.citronix.harvest.application.service;

import com.wora.citronix.harvest.application.dto.request.HarvestDetailRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import com.wora.citronix.harvest.domain.vo.HarvestDetailId;

public interface HarvestDetailService {
    HarvestDetailResponseDto findById(HarvestDetailId id);

    HarvestDetailResponseDto add(HarvestDetailId id, HarvestDetailRequestDto dto);

    HarvestDetailResponseDto update(HarvestDetailId id, HarvestDetailRequestDto dto);

    void delete(HarvestDetailId id);
}
