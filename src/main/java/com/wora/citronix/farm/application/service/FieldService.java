package com.wora.citronix.farm.application.service;

import com.wora.citronix.farm.application.dto.request.FieldRequestDto;
import com.wora.citronix.farm.application.dto.response.FieldResponseDto;
import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.entity.Field;
import com.wora.citronix.farm.domain.vo.FieldId;

import java.util.List;

public interface FieldService {

    FieldResponseDto findById(FieldId id);

    FieldResponseDto create(FieldRequestDto dto);

    void validateFields(Farm farm);

    FieldResponseDto update(FieldId id, FieldRequestDto dto);

    void delete(FieldId id);

    Field findEntityById(FieldId id);
}
