package com.wora.citronix.farm.application.service;

import com.wora.citronix.common.application.service.CrudService;
import com.wora.citronix.farm.application.dto.request.FieldRequestDto;
import com.wora.citronix.farm.application.dto.response.FieldResponseDto;
import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.vo.FieldId;

public interface FieldService extends CrudService<FieldId, FieldRequestDto, FieldResponseDto> {
    void saveFarmFields(Farm farm);
}
