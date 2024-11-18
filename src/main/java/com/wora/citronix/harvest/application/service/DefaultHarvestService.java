package com.wora.citronix.harvest.application.service;

import com.wora.citronix.common.application.service.ApplicationService;
import com.wora.citronix.common.domain.exception.AlreadyExistsException;
import com.wora.citronix.common.domain.exception.EntityNotFoundException;
import com.wora.citronix.harvest.application.dto.request.HarvestRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestResponseDto;
import com.wora.citronix.harvest.application.mapper.HarvestMapper;
import com.wora.citronix.harvest.domain.entity.Harvest;
import com.wora.citronix.harvest.domain.repository.HarvestRepository;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.harvest.domain.vo.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@ApplicationService
@RequiredArgsConstructor
public class DefaultHarvestService implements HarvestService {
    private final HarvestRepository repository;
    private final HarvestMapper mapper;

    @Override
    public Page<HarvestResponseDto> findAll(int pageNum, int pageSize) {
        return repository.findAll(PageRequest.of(pageNum, pageSize))
                .map(mapper::toResponseDto);
    }

    @Override
    public HarvestResponseDto findById(HarvestId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("harvest", id.value()));
    }

    @Override
    public HarvestResponseDto create(HarvestRequestDto dto) {
        Season season = Season.fromMonth(dto.date());
        if (repository.existsBySeason(season))
            throw new AlreadyExistsException(String.format("Already Exists A harvest in this season: %s, in date %s", season, dto.date()));

        Harvest savedHarvest = repository.save(new Harvest(dto.date(), season));
        return mapper.toResponseDto(savedHarvest);
    }
}
