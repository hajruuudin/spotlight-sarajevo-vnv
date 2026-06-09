package com.spotlightsarajevo.common.specifications;

import com.spotlightsarajevo.modules.event.domain.entity.EventEntity;
import com.spotlightsarajevo.common.enums.SortingOptions;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EventSpecification {
    public static Specification<EventEntity> hasSearchTerms(String searchTerm){
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

    public static Specification<EventEntity> hasCategories(List<Integer> categoryIds){
        return (root, query, criteriaBuilder) -> {
            if(categoryIds.isEmpty()){
                return criteriaBuilder.conjunction();
            }

            return root.get("categoryId").in(categoryIds);
        };
    }

    public static Specification<EventEntity> withDynamicSorting(String sortOption){
        return (root, query, criteriaBuilder) -> {

            SortingOptions sort;

            try {
                sort = SortingOptions.valueOf(sortOption.toUpperCase());
            } catch (IllegalArgumentException e){
                sort = SortingOptions.ALPHABETICAL;
            }

            LocalDateTime now = LocalDateTime.now();

            switch (sort) {
                case ALPHABETICAL -> query.orderBy(criteriaBuilder.asc(root.get("officialNameEn")));
                case DATE_UPCOMING -> {
                    query.orderBy(criteriaBuilder.asc(root.get("startDate")));
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), now);
                }
                case DATE_PAST -> {
                    query.orderBy(criteriaBuilder.desc(root.get("endDate")));
                    return criteriaBuilder.lessThan(root.get("endDate"), now);
                }
            }

            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<EventEntity> hasDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }

            // Events that span over the specified date (start_date <= end_of_day AND end_date >= start_of_day)
            return criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), date.plusDays(1).atStartOfDay()),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), date.atStartOfDay())
            );
        };
    }

    public static Specification<EventEntity> hasDateRange(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction();
            }

            // Events that overlap with the date range (event starts before range ends AND event ends after range starts)
            return criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), endDate.plusDays(1).atStartOfDay()),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), startDate.atStartOfDay())
            );
        };
    }
}
