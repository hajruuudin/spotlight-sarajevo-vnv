import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from '@angular/router';
import { SpotShorthandModel } from '../../shared/models/spot.model';
import { EventShorthandModel } from '../../shared/models/event.model';
import { inject } from '@angular/core';
import { SpotService } from '../../services/spot.service';
import { EventService } from '../../services/event.service';
import { catchError, forkJoin, map, of } from 'rxjs';
import { SortOptions } from '../../shared/constants/SortOptions';
import { HttpErrorResponse } from '@angular/common/http';
import { HotToastService } from '@ngxpert/hot-toast';

export interface DiscoverPageData {
  recentlyAddedSpots: SpotShorthandModel[];
  landmarkSpots: SpotShorthandModel[];
  popularSpots: SpotShorthandModel[];
  upcomingEvents: EventShorthandModel[];
  favouriteSpots: SpotShorthandModel[];
}

export const discoverResolver: ResolveFn<DiscoverPageData> = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const spotService = inject(SpotService);
  const eventService = inject(EventService);
  const toastr = inject(HotToastService);

  return forkJoin({
    recentlyAddedSpots: spotService.findSpotsPaginated(
      0,
      10,
      '',
      SortOptions.ALPHABETICAL.toString(),
      []
    ),
    landmarkSpots: spotService.findSpotsPaginated(
      0,
      10,
      '',
      SortOptions.ALPHABETICAL.toString(),
      []
    ),
    popularSpots: spotService.findSpotsPaginated(
      0,
      10,
      '',
      SortOptions.ALPHABETICAL.toString(),
      []
    ),
    upcomingEvents: eventService.findEventsPaginated(
      0,
      10,
      '',
      SortOptions.ALPHABETICAL.toString(),
      []
    ),
    favouriteSpots: spotService.findSpotsPaginated(
      0,
      10,
      '',
      SortOptions.ALPHABETICAL.toString(),
      []
    ),
  }).pipe(
    map((result) => ({
      recentlyAddedSpots: result.recentlyAddedSpots.content,
      landmarkSpots: result.landmarkSpots.content,
      popularSpots: result.popularSpots.content,
      upcomingEvents: result.upcomingEvents.content,
      favouriteSpots: result.favouriteSpots.content,
    })),
    catchError((error: HttpErrorResponse) => {
      toastr.error('Failed to load discovery data');
      return of({
        recentlyAddedSpots: [],
        landmarkSpots: [],
        popularSpots: [],
        upcomingEvents: [],
        favouriteSpots: [],
      });
    })
  );
};
