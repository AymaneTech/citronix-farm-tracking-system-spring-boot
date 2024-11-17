package com.wora.citronix.farm.infrastructure.web;

import com.wora.citronix.farm.application.dto.request.FieldRequestDto;
import com.wora.citronix.farm.application.dto.response.FieldResponseDto;
import com.wora.citronix.farm.application.service.FieldService;
import com.wora.citronix.farm.domain.vo.FieldId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fields")
@RequiredArgsConstructor
public class FieldController {
    private final FieldService service;

    @GetMapping("/{id}")
    public ResponseEntity<FieldResponseDto> findById(@PathVariable Long id) {
        FieldResponseDto field = service.findById(new FieldId(id));
        return ResponseEntity.ok(field);
    }

    @PostMapping
    public ResponseEntity<FieldResponseDto> create(@RequestBody @Valid FieldRequestDto request) {
        FieldResponseDto field = service.create(request);
        return new ResponseEntity<>(field, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FieldResponseDto> update(@PathVariable Long id, @RequestBody @Valid FieldRequestDto request) {
        FieldResponseDto field = service.update(new FieldId(id), request);
        return ResponseEntity.ok(field);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(new FieldId(id));
        return ResponseEntity.noContent().build();
    }
}
