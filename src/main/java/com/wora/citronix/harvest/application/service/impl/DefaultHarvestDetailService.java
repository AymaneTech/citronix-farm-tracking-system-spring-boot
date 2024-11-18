package com.wora.citronix.harvest.application.service.impl;

import com.wora.citronix.common.application.service.ApplicationService;
import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.harvest.application.dto.request.HarvestDetailRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import com.wora.citronix.harvest.application.mapper.HarvestDetailMapper;
import com.wora.citronix.harvest.application.service.HarvestDetailService;
import com.wora.citronix.harvest.application.service.HarvestDetailValidator;
import com.wora.citronix.harvest.application.service.HarvestService;
import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.entity.HarvestDetail;
import com.wora.citronix.harvest.domain.repository.HarvestDetailRepository;
import com.wora.citronix.harvest.domain.vo.HarvestDetailId;
import com.wora.citronix.tree.application.service.TreeService;
import com.wora.citronix.tree.domain.Tree;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class DefaultHarvestDetailService implements HarvestDetailService {
    private final HarvestDetailRepository repository;
    private final HarvestDetailMapper mapper;
    private final TreeService treeService;
    private final HarvestService harvestService;
    private final HarvestDetailValidator validator;

    @Override
    public HarvestDetailResponseDto findById(HarvestDetailId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> throwEntityNotFound(id));
    }

    @Override
    public HarvestDetailResponseDto add(HarvestDetailId id, HarvestDetailRequestDto dto) {
        Harvest harvest = harvestService.findEntityById(id.harvestId());
        Tree tree = treeService.findEntityById(id.treeId());

        validator.validateHarvestDetail(tree, harvest, dto.date());

        HarvestDetail savedDetail = repository.save(
                new HarvestDetail(harvest, tree, dto.date(), dto.quantity())
        );
        harvest.addNewHarvestDetail(savedDetail);
        return mapper.toResponseDto(savedDetail);
    }

    @Override
    public HarvestDetailResponseDto update(HarvestDetailId id, HarvestDetailRequestDto dto) {
        HarvestDetail harvestDetail = repository.findById(id)
                .orElseThrow(() -> throwEntityNotFound(id));

        // todo: handle quantity change here
        harvestDetail.setDate(dto.date())
                .setQuantity(dto.quantity());

        return mapper.toResponseDto(harvestDetail);
    }

    @Override
    public void delete(HarvestDetailId id) {
        if (!repository.existsById(id))
            throwEntityNotFound(id);

        repository.deleteById(id);
    }

    private EntityNotFoundException throwEntityNotFound(HarvestDetailId id) {
        throw new EntityNotFoundException(String.format("harvest detail with tree id %s and harvest id %s not found", id.treeId().value(), id.harvestId().value()));
    }
}
