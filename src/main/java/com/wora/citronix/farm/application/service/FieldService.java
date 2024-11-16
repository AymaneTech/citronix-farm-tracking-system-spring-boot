package com.wora.citronix.farm.application.service;

import com.wora.citronix.common.application.service.CrudService;
import com.wora.citronix.farm.application.dto.request.FieldRequestDto;
import com.wora.citronix.farm.application.dto.response.FieldResponseDto;
import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.entity.Field;
import com.wora.citronix.farm.domain.vo.FieldId;

import java.util.List;

public interface FieldService extends CrudService<FieldId, FieldRequestDto, FieldResponseDto> {
    List<Field> saveFarmFields(Farm farm);

    Field findEntityById(FieldId id);
}
