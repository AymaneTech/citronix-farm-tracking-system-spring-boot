package com.wora.citronix.tree.application.mapper;

import com.wora.citronix.common.application.mapper.BaseMapper;
import com.wora.citronix.tree.application.dto.TreeRequestDto;
import com.wora.citronix.tree.application.dto.TreeResponseDto;
import com.wora.citronix.tree.domain.Tree;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface TreeMapper extends BaseMapper<Tree, TreeRequestDto, TreeResponseDto> {
}
