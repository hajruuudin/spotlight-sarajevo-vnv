import { ResolveFn } from '@angular/router';
import { SpotShorthandModel } from '../../shared/models/spot.model';
import { EventShorthandModel } from '../../shared/models/event.model';
import { EventCategoryModel, SpotCategoryModel } from '../../shared/models/category.model';
import { inject } from '@angular/core';
import { SpotService } from '../../services/spot.service';
import { EventService } from '../../services/event.service';
import { CategoryService } from '../../services/category.service';
import { catchError, forkJoin, map, of } from 'rxjs';
import { SortOptions } from '../../shared/constants/SortOptions';
import { HttpErrorResponse } from '@angular/common/http';

export interface HomepagePageData {
  headlineSpot: SpotShorthandModel | null;
  headlineEvent: EventShorthandModel | null;
  favouriteSpots: SpotShorthandModel[];
  popularSpots: SpotShorthandModel[];
  upcomingEvents: EventShorthandModel[];
  landmarkSpots: SpotShorthandModel[];
  spotCategories: SpotCategoryModel[];
  eventCategories: EventCategoryModel[];
}

export const homepageResolver: ResolveFn<HomepagePageData> = (route, state) => {
  const spotService = inject(SpotService);
  const eventService = inject(EventService);
  const categoryService = inject(CategoryService);

  return forkJoin({
    headlineSpot: spotService.findSpotsPaginated(0, 1, '', SortOptions.ALPHABETICAL.toString(), []),
    headlineEvent: eventService.findEventsPaginated(
      0,
      1,
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
    landmarkSpots: spotService.findSpotsPaginated(
      0,
      10,
      '',
      SortOptions.ALPHABETICAL.toString(),
      []
    ),
    spotCategories: categoryService.getAllSpotCategories(),
    eventCategories: categoryService.getAllEventCategories(),
  }).pipe(
    map((response) => ({
      headlineSpot: response.headlineSpot.content[0],
      headlineEvent: response.headlineEvent.content[0],
      favouriteSpots: response.favouriteSpots.content,
      popularSpots: response.popularSpots.content,
      upcomingEvents: response.upcomingEvents.content,
      landmarkSpots: response.landmarkSpots.content,
      spotCategories: response.spotCategories,
      eventCategories: response.eventCategories,
    })),
    catchError((error: HttpErrorResponse) => {
      return of({
        headlineSpot: null,
        headlineEvent: null,
        favouriteSpots: [],
        popularSpots: [],
        upcomingEvents: [],
        landmarkSpots: [],
        spotCategories: [],
        eventCategories: [],
      });
    })
  );
};
