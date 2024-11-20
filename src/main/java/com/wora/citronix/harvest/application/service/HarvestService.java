package com.wora.citronix.harvest.application.service;

import com.wora.citronix.harvest.application.dto.request.HarvestRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestResponseDto;
import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import org.springframework.data.domain.Page;

public interface HarvestService {
    Page<HarvestResponseDto> findAll(int pageNum, int pageSize);

    HarvestResponseDto findById(HarvestId id);

    HarvestResponseDto create(HarvestRequestDto dto);

    Harvest findEntityById(HarvestId id);
}
