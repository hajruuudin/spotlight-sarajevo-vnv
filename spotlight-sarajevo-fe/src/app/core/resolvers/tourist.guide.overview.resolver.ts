import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from '@angular/router';
import { TouristGuideOverviewModel } from '../../shared/models/tourist.guide.model';
import { inject } from '@angular/core';
import { TouristGuideService } from '../../services/tourist.guide.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { catchError, map, of } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

export const touristGuideOverviewResolver: ResolveFn<TouristGuideOverviewModel> = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const guideService = inject(TouristGuideService);
  const slug = route.paramMap.get('slug')!;
  return guideService.findGuideOverview(slug);
};
