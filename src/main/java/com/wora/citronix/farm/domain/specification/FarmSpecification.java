package com.wora.citronix.farm.domain.specification;

import com.wora.citronix.farm.domain.entity.Farm;
import org.springframework.data.jpa.domain.Specification;

public class FarmSpecification {
    private FarmSpecification() {
    }

    public static Specification<Farm> hasName(String name) {
        return ((root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%"));
    }

    public static Specification<Farm> hasLocation(String location) {
        return ((root, query, criteriaBuilder) ->
                location == null ? null : criteriaBuilder.like(root.get("location"), "%" + location + "%"));
    }

    public static Specification<Farm> hasAreaBetween(Double minArea, Double maxArea) {
        return ((root, query, criteriaBuilder) -> {
            if (minArea == null && maxArea == null) return null;
            if (minArea != null && maxArea != null)
                return criteriaBuilder.between(root.get("area"), minArea, maxArea);
            if (minArea != null)
                return criteriaBuilder.greaterThanOrEqualTo(root.get("area"), maxArea);
            return criteriaBuilder.lessThanOrEqualTo(root.get("area"), maxArea);
        });
    }
}
