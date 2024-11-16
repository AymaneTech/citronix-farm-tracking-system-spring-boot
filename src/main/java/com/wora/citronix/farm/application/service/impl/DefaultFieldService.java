package com.wora.citronix.farm.application.service.impl;

import com.wora.citronix.common.application.service.ApplicationService;
import com.wora.citronix.common.domain.exception.AlreadyExists;
import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.farm.application.dto.request.FieldRequestDto;
import com.wora.citronix.farm.application.dto.response.FieldResponseDto;
import com.wora.citronix.farm.application.mapper.FieldMapper;
import com.wora.citronix.farm.application.service.FieldService;
import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.entity.Field;
import com.wora.citronix.farm.domain.repository.FarmRepository;
import com.wora.citronix.farm.domain.repository.FieldRepository;
import com.wora.citronix.farm.domain.vo.FarmId;
import com.wora.citronix.farm.domain.vo.FieldId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationService
@RequiredArgsConstructor
public class DefaultFieldService implements FieldService {
    private final FieldRepository repository;
    private final FieldMapper mapper;
    private final FarmRepository farmRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Page<FieldResponseDto> findAll(int pageNum, int pageSize) {
        return repository.findAll(PageRequest.of(pageNum, pageSize))
                .map(mapper::toResponseDto);
    }

    @Override
    public FieldResponseDto findById(com.wora.citronix.farm.domain.vo.FieldId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("field", id.value()));
    }

    @Override
    public FieldResponseDto create(FieldRequestDto dto) {
        Farm farm = farmRepository.findById(new FarmId(dto.farmId()))
                .orElseThrow(() -> new EntityNotFoundException("farm", dto.farmId()))
                .ensureFieldCountWithinLimit()
                .ensureFieldAreaWithinFarmCapacity(dto.area());

        if (repository.existsByName(dto.name()))
            throw new AlreadyExists("Field Name Already Taken in Farm: " + farm.getName());

        Field field = mapper.toEntity(dto)
                .setFarm(farm);
        Field savedField = repository.save(field);
        return mapper.toResponseDto(savedField);
    }

    @Override
    public List<Field> saveFarmFields(Farm farm) {
        validateDuplicatedFieldNames(farm);

        farm.ensureFieldCountWithinLimit();
        farm.getFields().stream()
                .peek(field -> field.setFarm(farm))
                .map(Field::getArea)
                .forEach(farm::ensureFieldAreaWithinFarmCapacity);

        return repository.saveAll(farm.getFields());
    }

    @Override
    public FieldResponseDto update(FieldId id, FieldRequestDto dto) {
        Field field = findEntityById(id)
                .setName(dto.name());
        if (!field.getArea().equals(dto.area())) {
            field.getFarm().ensureFieldAreaWithinFarmCapacity(dto.area());
            field.setArea(dto.area());
        }
        return mapper.toResponseDto(field);
    }

    @Override
    public void delete(FieldId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("field", id.value());

        repository.deleteById(id);
    }

    @Override
    public Field findEntityById(FieldId id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("field", id.value()));
    }

    private void validateDuplicatedFieldNames(Farm farm) {
        List<String> duplicateNames = farm.getFields().stream()
                .map(Field::getName)
                .collect(Collectors.groupingBy(name -> name, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();

        if (!duplicateNames.isEmpty()) {
            throw new IllegalArgumentException("Duplicate field names found: " + duplicateNames);
        }
    }
}
