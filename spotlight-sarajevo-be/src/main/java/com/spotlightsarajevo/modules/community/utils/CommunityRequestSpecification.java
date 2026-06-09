package com.spotlightsarajevo.modules.community.utils;

import com.spotlightsarajevo.common.enums.FilterOptions;
import com.spotlightsarajevo.common.enums.RequestStatus;
import com.spotlightsarajevo.modules.community.domain.entity.CommunityRequestEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification class for building dynamic queries for CommunityRequestEntity based on filter options.
 * This class provides Specification builders to filter community requests by their status.
 */
public class CommunityRequestSpecification {

    /**
     * Builds a Specification based on the provided FilterOptions enum.
     *
     * @param filterOption The filter option to apply (ALL, PENDING, REJECTED, APPROVED)
     * @return A Specification<CommunityRequestEntity> that can be used with JpaSpecificationExecutor
     */
    public static Specification<CommunityRequestEntity> getByFilterOption(FilterOptions filterOption) {
        return switch (filterOption) {
            case ALL -> null;
            case PENDING -> byStatus(RequestStatus.PENDING);
            case REJECTED -> byStatus(RequestStatus.REJECTED);
            case APPROVED -> byStatus(RequestStatus.APPROVED);
        };
    }

    /**
     * Builds a Specification to filter by request status.
     *
     * @param status The RequestStatus to filter by
     * @return A Specification<CommunityRequestEntity> for the given status
     */
    private static Specification<CommunityRequestEntity> byStatus(RequestStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    /**
     * Builds a Specification to retrieve recently added community requests.
     * Orders results by creation date in descending order.
     *
     * @return A Specification<CommunityRequestEntity> that orders by created date descending
     */
    public static Specification<CommunityRequestEntity> orderByRecentlyAdded() {
        return (root, query, criteriaBuilder) -> {
            if (query != null) {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            }
            return criteriaBuilder.conjunction();
        };
    }
}

