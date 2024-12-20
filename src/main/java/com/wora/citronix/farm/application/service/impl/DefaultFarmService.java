package com.wora.citronix.farm.application.service.impl;

import com.wora.citronix.common.application.service.ApplicationService;
import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.farm.application.dto.request.FarmRequestDto;
import com.wora.citronix.farm.application.dto.request.FarmSearchRequest;
import com.wora.citronix.farm.application.dto.response.FarmResponseDto;
import com.wora.citronix.farm.application.mapper.FarmMapper;
import com.wora.citronix.farm.application.service.FarmService;
import com.wora.citronix.farm.application.service.FieldService;
import com.wora.citronix.farm.domain.entity.Farm;
import com.wora.citronix.farm.domain.repository.FarmRepository;
import com.wora.citronix.farm.domain.vo.FarmId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.wora.citronix.farm.domain.specification.FarmSpecification.*;

@ApplicationService
@RequiredArgsConstructor
public class DefaultFarmService implements FarmService {
    private final FarmRepository repository;
    private final FieldService fieldService;
    private final FarmMapper mapper;

    @Override
    public Page<FarmResponseDto> findAll(int pageNum, int pageSize) {
        return repository.findAll(PageRequest.of(pageNum, pageSize))
                .map(mapper::toResponseDto);
    }

    @Override
    public Page<FarmResponseDto> findAllWithSpecification(Pageable pageRequest, FarmSearchRequest request) {
        Specification<Farm> specification = Specification.where(hasName(request.name()))
                .and(hasLocation(request.location()))
                .and(hasAreaBetween(request.minArea(), request.maxArea()));

        return repository.findAll(specification, pageRequest)
                .map(mapper::toResponseDto);
    }

    @Override
    public FarmResponseDto findById(FarmId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("farm", id.value()));
    }

    @Override
    public FarmResponseDto create(FarmRequestDto dto) {
        Farm farm = mapper.toEntity(dto);
        if (dto.fields() != null) fieldService.validateFields(farm);
        Farm savedFarm = repository.save(farm);

        return mapper.toResponseDto(savedFarm);
    }

    @Override
    public FarmResponseDto update(FarmId id, FarmRequestDto dto) {
        Farm farm = findEntityById(id)
                .setName(dto.name())
                .setLocation(dto.location())
                .setArea(dto.area()); // todo: verify that the new area is enough for existing fields

        return mapper.toResponseDto(farm);
    }

    @Override
    public void delete(FarmId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("farm", id.value());

        repository.deleteById(id);
    }

    public Farm findEntityById(FarmId id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("farm", id.value()));
    }
}
