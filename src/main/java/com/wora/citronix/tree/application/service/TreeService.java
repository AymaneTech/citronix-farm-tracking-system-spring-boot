package com.wora.citronix.tree.application.service;

import com.wora.citronix.tree.application.dto.TreeRequestDto;
import com.wora.citronix.tree.application.dto.TreeResponseDto;
import com.wora.citronix.tree.domain.TreeId;

public interface TreeService {
    TreeResponseDto plant(TreeRequestDto dto);

    TreeResponseDto update(TreeId id, TreeRequestDto dto);

    TreeResponseDto findById(TreeId id);

    void delete(TreeId id);
}
