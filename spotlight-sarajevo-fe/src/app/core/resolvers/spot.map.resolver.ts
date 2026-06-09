import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from "@angular/router";
import { inject } from "@angular/core";
import { forkJoin, map } from "rxjs";
import { SpotService } from "../../services/spot.service";
import { CategoryService } from "../../services/category.service";
import { SpotMapModel } from "../../shared/models/spot.model";
import { SpotCategoryModel } from "../../shared/models/category.model";

export interface SpotMapResolverData {
  spotMapPins: SpotMapModel[];
  spotCategories: SpotCategoryModel[];
}

export const spotMapResolver: ResolveFn<SpotMapResolverData> = (
  route: ActivatedRouteSnapshot, 
  state: RouterStateSnapshot
) => {
  const spotService = inject(SpotService);
  const categoryService = inject(CategoryService);

  return forkJoin({
    spotMapPins: spotService.findSpotsForMap('', []),
    spotCategories: categoryService.getAllSpotCategories()
  });
};
