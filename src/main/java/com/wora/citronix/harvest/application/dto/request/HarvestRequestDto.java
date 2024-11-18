package com.wora.citronix.harvest.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

// todo : add a list of harvest details requests
public record HarvestRequestDto(@NotNull LocalDate date) {
}
