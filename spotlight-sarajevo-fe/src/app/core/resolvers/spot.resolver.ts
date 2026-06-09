import { ActivatedRouteSnapshot, Resolve, ResolveFn, RouterStateSnapshot } from "@angular/router";
import { SpotOverviewModel } from "../../shared/models/spot.model";
import { inject } from "@angular/core";
import { SpotService } from "../../services/spot.service";
import { map, switchMap } from "rxjs";
import { SessionService } from "../services/session.service";

export const spotResolver: ResolveFn<{ spot: SpotOverviewModel, isVisited: Boolean }> = (
  route: ActivatedRouteSnapshot, 
  state: RouterStateSnapshot
) => {
  const spotService = inject(SpotService);
  const session = inject(SessionService);
  
  const spotSlug = route.paramMap.get('spotSlug')!;

  if(session.getUser() == null){
    return spotService.findSpotOverview(spotSlug).pipe(
      map((spotOverview) => ({
        spot: spotOverview,
        isVisited: false
      }))
    );
  } else {
    return spotService.findSpotOverview(spotSlug).pipe(
    switchMap((spotOverview) => {
      return spotService.checkIfSpotIsVisited(spotOverview.id).pipe(
        map((isVisited) => ({
          spot: spotOverview,
          isVisited: isVisited
        }))
      );
    })
  );
  }
};