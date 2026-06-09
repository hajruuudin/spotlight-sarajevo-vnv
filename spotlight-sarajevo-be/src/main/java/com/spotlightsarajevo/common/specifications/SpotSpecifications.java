package com.spotlightsarajevo.common.specifications;

import com.spotlightsarajevo.modules.spot.domain.entity.SpotEntity;
import com.spotlightsarajevo.common.enums.SortingOptions;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class SpotSpecifications {
    public static Specification<SpotEntity> hasSearchTerm(String searchTerm){
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.toString().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + searchTerm.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("officialNameEn")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("officialNameBs")), pattern)
            );
        };
    }

    public static Specification<SpotEntity> hasCategories(List<Integer> categoryIds){
        return (root, query, criteriaBuilder) -> {
            if (categoryIds.isEmpty()){
                return criteriaBuilder.conjunction();
            }

            return root.get("categoryId").in(categoryIds);
        };
    }

    public static Specification<SpotEntity> withDynamicSorting(String sortOption) {
        return withDynamicSorting(sortOption, null, null);
    }

    public static Specification<SpotEntity> withDynamicSorting(String sortOption, BigDecimal userLatitude, BigDecimal userLongitude) {
        return (root, query, criteriaBuilder) -> {

            SortingOptions sorting;
            try {
                sorting = SortingOptions.valueOf(sortOption.toUpperCase());
            } catch (IllegalArgumentException e) {
                sorting = SortingOptions.ALPHABETICAL;
            }

            // If PROXIMITY is selected but coordinates are not provided, fall back to ALPHABETICAL
            if (sorting == SortingOptions.PROXIMITY && (userLatitude == null || userLongitude == null)) {
                sorting = SortingOptions.ALPHABETICAL;
            }

            switch (sorting) {
                case ALPHABETICAL -> query.orderBy(criteriaBuilder.asc(root.get("officialNameEn")));
                case RATING -> {
                    Join<Object, Object> reviewStatsJoin = root.join("reviewStats", JoinType.LEFT);
                    query.orderBy(criteriaBuilder.desc(reviewStatsJoin.get("combinedRating")));
                }
                case PROXIMITY -> {
                    Expression<BigDecimal> spotLat = root.get("latitude");
                    Expression<BigDecimal> spotLon = root.get("longitude");

                    Expression<Number> latDiff = criteriaBuilder.diff(spotLat, userLatitude);
                    Expression<Number> latDiffSquared = criteriaBuilder.prod(latDiff, latDiff);

                    Expression<Number> lonDiff = criteriaBuilder.diff(spotLon, userLongitude);
                    Expression<Number> lonDiffSquared = criteriaBuilder.prod(lonDiff, lonDiff);

                    Expression<Number> distanceSquared = criteriaBuilder.sum(latDiffSquared, lonDiffSquared);

                    query.orderBy(criteriaBuilder.asc(distanceSquared));
                }
            }

            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<SpotEntity> excludeVisitedSpots(List<Integer> visitedSpotIds) {
        return (root, query, criteriaBuilder) -> {
            if (visitedSpotIds == null || visitedSpotIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.not(root.get("id").in(visitedSpotIds));
        };
    }

}
