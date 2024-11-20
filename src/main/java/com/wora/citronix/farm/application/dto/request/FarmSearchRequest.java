package com.wora.citronix.farm.application.dto.request;

public record FarmSearchRequest(String name,
                                String location,
                                Double minArea,
                                Double maxArea) {
}
