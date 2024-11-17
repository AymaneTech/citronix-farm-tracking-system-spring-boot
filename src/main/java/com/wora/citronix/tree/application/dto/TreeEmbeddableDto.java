package com.wora.citronix.tree.application.dto;

import com.wora.citronix.tree.domain.Level;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TreeEmbeddableDto(@NotNull Long id,
                                @NotNull LocalDate plantingDate,
                                @NotNull Level level) {
}
