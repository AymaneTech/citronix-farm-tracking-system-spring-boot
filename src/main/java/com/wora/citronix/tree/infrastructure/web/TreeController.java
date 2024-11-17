package com.wora.citronix.tree.infrastructure.web;

import com.wora.citronix.tree.application.dto.TreeRequestDto;
import com.wora.citronix.tree.application.dto.TreeResponseDto;
import com.wora.citronix.tree.application.service.TreeService;
import com.wora.citronix.tree.domain.TreeId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trees")
@RequiredArgsConstructor
public class TreeController {
    private final TreeService service;

    @PostMapping
    public ResponseEntity<TreeResponseDto> plant(@RequestBody @Valid TreeRequestDto request) {
        TreeResponseDto plant = service.plant(request);
        return new ResponseEntity<>(plant, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreeResponseDto> findById(@PathVariable Long id) {
        TreeResponseDto tree = service.findById(new TreeId(id));
        return ResponseEntity.ok(tree);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreeResponseDto> update(@PathVariable Long id, @RequestBody @Valid TreeRequestDto request){
        TreeResponseDto tree = service.update(new TreeId(id), request);
        return ResponseEntity.ok(tree);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(new TreeId(id));
        return ResponseEntity.noContent().build();
    }
}
