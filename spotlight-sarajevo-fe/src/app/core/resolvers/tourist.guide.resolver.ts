import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from '@angular/router';
import { TouristGuideShorthandModel } from '../../shared/models/tourist.guide.model';
import { inject } from '@angular/core';
import { TouristGuideService } from '../../services/tourist.guide.service';
import { catchError, forkJoin, map, of } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { HotToastService } from '@ngxpert/hot-toast';

export interface TouristGuidePageData {
  mainGuides: TouristGuideShorthandModel[];
  // newGuides: TouristGuideShorthandModel[];
  // touristRelatedGuides: TouristGuideShorthandModel[];
  // allGuides: TouristGuideShorthandModel[];
}

export const touristGuideResolver: ResolveFn<TouristGuidePageData> = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const guideService = inject(TouristGuideService);
  const toastr = inject(HotToastService);

  return forkJoin({
    mainGuides: guideService.findAllGuides(),
    newGuides: guideService.findAllGuides(),
    touristRelatedGuides: guideService.findAllGuides(),
    allGuides: guideService.findAllGuides(),
  }).pipe(
    map((result) => ({
      mainGuides: result.mainGuides,
      newGuides: result.newGuides,
      touristRelatedGuides: result.touristRelatedGuides,
      allGuides: result.allGuides,
    })),
    catchError((error: HttpErrorResponse) => {
      toastr.error('Failed to load tourist guides');
      return of({
        mainGuides: [],
        newGuides: [],
        touristRelatedGuides: [],
        allGuides: [],
      });
    })
  );
};