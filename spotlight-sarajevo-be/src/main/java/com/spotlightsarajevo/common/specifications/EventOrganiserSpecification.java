package com.spotlightsarajevo.common.specifications;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.SortingOptions;
import com.spotlightsarajevo.common.exceptions.EventExceptions;
import com.spotlightsarajevo.common.exceptions.SpotExceptions;
import com.spotlightsarajevo.modules.review.domain.entity.EventOrganiserReviewEntity;
import org.springframework.data.jpa.domain.Specification;

public class EventOrganiserSpecification {
    public static Specification<EventOrganiserReviewEntity> hasOrganiserId(Integer organiserId){
        return (root, query, criteriaBuilder) -> {
            if (organiserId == null){
                throw new EventExceptions.EventNotFoundException(ExceptionCodes.EventExceptionCodes.EVENT_ORGANISER_REVIEW_NOT_FOUND);
            }

            return criteriaBuilder.equal(root.get("organiserId"), organiserId);
        };
    }

    public static Specification<EventOrganiserReviewEntity> withDynamicSorting(String sortOption){
        return (root, query, criteriaBuilder) -> {
            if(sortOption.isBlank()){
                throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);
            }

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
