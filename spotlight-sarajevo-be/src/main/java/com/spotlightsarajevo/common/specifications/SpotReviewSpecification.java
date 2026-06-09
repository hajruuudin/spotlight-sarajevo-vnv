package com.spotlightsarajevo.common.specifications;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.SortingOptions;
import com.spotlightsarajevo.common.exceptions.SpotExceptions;
import com.spotlightsarajevo.modules.review.domain.entity.SpotReviewEntity;
import org.springframework.data.jpa.domain.Specification;

public class SpotReviewSpecification {
    public static Specification<SpotReviewEntity> hasSpotId(Integer spotId){
        return (root, query, criteriaBuilder) -> {
            if (spotId == null){
                throw new SpotExceptions.SpotReviewException(ExceptionCodes.SpotExceptionCodes.SPOT_REVIEW_NOT_FOUND);
            }

            return criteriaBuilder.equal(root.get("spotId"), spotId);
        };
    }

    public static Specification<SpotReviewEntity> withDynamicSorting(String sortOption){
        return (root, query, criteriaBuilder) -> {

            SortingOptions sortingOptions;

            try {
                sortingOptions = SortingOptions.valueOf(sortOption.toUpperCase());
            } catch (IllegalArgumentException e){
                throw new IllegalArgumentException("NO_SORT_OPTION");
            }

            switch (sortingOptions){
                case ALPHABETICAL -> query.orderBy(criteriaBuilder.asc(root.get("header")));
                case ALPHABETICAL_DESC -> query.orderBy(criteriaBuilder.desc(root.get("header")));
                case RATING -> query.orderBy(criteriaBuilder.asc(root.get("userOverallRating")));
                case RATING_DESC -> query.orderBy(criteriaBuilder.desc(root.get("userOverallRating")));
            }

            return criteriaBuilder.conjunction();
        };
    }
}
