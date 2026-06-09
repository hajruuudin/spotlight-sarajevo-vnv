package com.spotlightsarajevo.common.specifications;

import com.spotlightsarajevo.common.enums.SortingOptions;
import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideEntity;
import org.springframework.data.jpa.domain.Specification;

public class TouristGuideSpecification {
    public static Specification<TouristGuideEntity> hasSearchTerms(String searchTerm){
        return (root, query, criteriaBuilder) -> {
          if (searchTerm == null || searchTerm.toString().isEmpty()){
              return criteriaBuilder.conjunction();
          }

          String pattern = "%" + searchTerm.toLowerCase() + "%";

          return criteriaBuilder.or(
                  criteriaBuilder.like(criteriaBuilder.lower(root.get("guideTitleBs")), pattern),
                  criteriaBuilder.like(criteriaBuilder.lower(root.get("guideTitleEn")), pattern)
          );
        };
    }

    public static Specification<TouristGuideEntity> withDynamicSorting(String sortOption){
        return (root, query, criteriaBuilder) -> {
            SortingOptions selectedSortOption;

            try {
                selectedSortOption = SortingOptions.valueOf(sortOption.toUpperCase());
            } catch (IllegalArgumentException e){
                selectedSortOption = SortingOptions.ALPHABETICAL;
            }

            switch (selectedSortOption) {
                default -> query.orderBy(criteriaBuilder.asc(root.get("guideTitleEn")));
            }

            return criteriaBuilder.conjunction();
        };
    }
}
