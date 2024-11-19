package com.wora.citronix.harvest.infrastructure.web;

import com.wora.citronix.harvest.application.dto.request.HarvestDetailRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import com.wora.citronix.harvest.application.service.HarvestDetailService;
import com.wora.citronix.harvest.domain.vo.HarvestDetailId;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.tree.domain.TreeId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/harvest-details")
@RequiredArgsConstructor
public class HarvestDetailController {
    private final HarvestDetailService service;

    @PostMapping("/{harvestId}/{treeId}")
    public ResponseEntity<HarvestDetailResponseDto> add(@PathVariable Long harvestId,
                                                        @PathVariable Long treeId,
                                                        @RequestBody @Valid HarvestDetailRequestDto request) {
        HarvestDetailId harvestDetailId = new HarvestDetailId(
                new HarvestId(harvestId),
                new TreeId(treeId)
        );
        HarvestDetailResponseDto response = service.add(harvestDetailId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{harvestId}/{treeId}")
    public ResponseEntity<HarvestDetailResponseDto> findById(@PathVariable Long harvestId,
                                                             @PathVariable Long treeId) {

        HarvestDetailId harvestDetailId = new HarvestDetailId(
                new HarvestId(harvestId),
                new TreeId(treeId)
        );
        HarvestDetailResponseDto response = service.findById(harvestDetailId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{harvestId}/{treeId}")
    public ResponseEntity<HarvestDetailResponseDto> update(@PathVariable Long harvestId,
                                                           @PathVariable Long treeId,
                                                           @RequestBody @Valid HarvestDetailRequestDto request) {
        HarvestDetailId harvestDetailId = new HarvestDetailId(
                new HarvestId(harvestId),
                new TreeId(treeId)
        );
        HarvestDetailResponseDto response = service.update(harvestDetailId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{harvestId}/{treeId}")
    public ResponseEntity<Void> delete(@PathVariable Long harvestId,
                                       @PathVariable Long treeId) {

        HarvestDetailId harvestDetailId = new HarvestDetailId(
                new HarvestId(harvestId),
                new TreeId(treeId)
        );
        service.delete(harvestDetailId);
        return ResponseEntity.noContent().build();
    }
}
