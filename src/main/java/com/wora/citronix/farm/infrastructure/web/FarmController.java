package com.wora.citronix.farm.infrastructure.web;

import com.wora.citronix.farm.application.dto.request.FarmRequestDto;
import com.wora.citronix.farm.application.dto.request.FarmSearchRequest;
import com.wora.citronix.farm.application.dto.response.FarmResponseDto;
import com.wora.citronix.farm.application.service.FarmService;
import com.wora.citronix.farm.domain.vo.FarmId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/farms")
@RequiredArgsConstructor
public class FarmController {
    private final FarmService service;

    @GetMapping
    public ResponseEntity<Page<FarmResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @ModelAttribute FarmSearchRequest searchCriteria) {
        Page<FarmResponseDto> farms = service.findAllWithSpecification(PageRequest.of(pageNum, pageSize), searchCriteria);
        return ResponseEntity.ok(farms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmResponseDto> findById(@PathVariable Long id) {
        FarmResponseDto farm = service.findById(new FarmId(id));
        return ResponseEntity.ok(farm);
    }

    @PostMapping
    public ResponseEntity<FarmResponseDto> create(@RequestBody @Valid FarmRequestDto request) {
        FarmResponseDto farm = service.create(request);
        return new ResponseEntity<>(farm, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FarmResponseDto> update(@PathVariable Long id, @RequestBody @Valid FarmRequestDto request) {
        FarmResponseDto farm = service.update(new FarmId(id), request);
        return ResponseEntity.ok(farm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(new FarmId(id));
        return ResponseEntity.noContent().build();
    }
}
