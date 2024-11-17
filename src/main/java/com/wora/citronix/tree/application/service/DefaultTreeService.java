package com.wora.citronix.tree.application.service;

import com.wora.citronix.common.application.service.ApplicationService;
import com.wora.citronix.common.domain.exception.EntityCreationException;
import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.farm.application.service.FieldService;
import com.wora.citronix.farm.domain.entity.Field;
import com.wora.citronix.farm.domain.vo.FieldId;
import com.wora.citronix.tree.application.dto.TreeRequestDto;
import com.wora.citronix.tree.application.dto.TreeResponseDto;
import com.wora.citronix.tree.application.mapper.TreeMapper;
import com.wora.citronix.tree.domain.Tree;
import com.wora.citronix.tree.domain.TreeId;
import com.wora.citronix.tree.domain.TreeRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Month;

@ApplicationService
@RequiredArgsConstructor
public class DefaultTreeService implements TreeService {
    private final TreeRepository repository;
    private final FieldService fieldService;
    private final TreeMapper mapper;

    @Override
    public TreeResponseDto plant(TreeRequestDto dto) {
        if (isPlantingPeriod(dto.plantingDate()))
            throw new EntityCreationException("You can create tree only in (March, April, May)");

        Field field = fieldService.findEntityById(new FieldId(dto.fieldId()));
        int currentTreeCount = repository.countByFieldId(field.getId());
        if (!field.hasCapacityForNewTree(currentTreeCount)) {
            int maxTrees = field.getMaxTreesForField();
            throw new EntityCreationException(
                    String.format("Field has reached maximum capacity of %d trees (current: %d)",
                            maxTrees, currentTreeCount)
            );
        }

        Tree savedTree = repository.save(new Tree(dto.plantingDate(), field));

        return mapper.toResponseDto(savedTree);
    }

    @Override
    public TreeResponseDto update(TreeId id, TreeRequestDto dto) {
        Tree tree = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("tree", id.value()));
        if (isPlantingPeriod(dto.plantingDate()))
            throw new EntityCreationException("You can create tree only in (March, April, May)");

        Field field = fieldService.findEntityById(new FieldId(dto.fieldId()));
        tree.setPlantingDate(dto.plantingDate())
                .setField(field);

        return mapper.toResponseDto(tree);
    }

    @Override
    public TreeResponseDto findById(TreeId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("tree", id.value()));
    }

    @Override
    public void delete(TreeId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("tree", id.value());
        repository.deleteById(id);
    }

    private boolean isPlantingPeriod(LocalDate plantingDate) {
        Month month = plantingDate.getMonth();
        return month != Month.MARCH && month != Month.APRIL && month != Month.MAY;
    }
}
