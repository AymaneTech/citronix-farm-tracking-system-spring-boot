package com.wora.citronix.harvest.infrastructure.web;

import com.wora.citronix.harvest.application.dto.request.HarvestRequestDto;
import com.wora.citronix.harvest.application.dto.response.HarvestResponseDto;
import com.wora.citronix.harvest.application.service.HarvestService;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/harvests")
@RequiredArgsConstructor
public class HarvestController {
    private final HarvestService service;

    @GetMapping
    public ResponseEntity<Page<HarvestResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<HarvestResponseDto> harvests = service.findAll(pageNum, pageSize);
        return ResponseEntity.ok(harvests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HarvestResponseDto> findById(@PathVariable Long id) {
        HarvestResponseDto harvest = service.findById(new HarvestId(id));
        return ResponseEntity.ok(harvest);
    }

    @PostMapping
    public ResponseEntity<HarvestResponseDto> create(@RequestBody @Valid HarvestRequestDto request) {
        HarvestResponseDto harvest = service.create(request);
        return new ResponseEntity<>(harvest, HttpStatus.CREATED);
    }
}
