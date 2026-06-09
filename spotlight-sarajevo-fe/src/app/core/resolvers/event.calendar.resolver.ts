import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from "@angular/router";
import { inject } from "@angular/core";
import { forkJoin, map } from "rxjs";
import { EventService } from "../../services/event.service";
import { EventDateCheckModel, EventShorthandModel } from "../../shared/models/event.model";

export interface EventCalendarResolverData {
  eventDatesMap: EventDateCheckModel;
  eventsForDay: EventShorthandModel[];
  selectedDate: string;
}

export const eventCalendarResolver: ResolveFn<EventCalendarResolverData> = (
  route: ActivatedRouteSnapshot, 
  state: RouterStateSnapshot
) => {
  const eventService = inject(EventService);

  const today = new Date();
  const year = today.getFullYear();
  const month = today.getMonth() + 1;
  const day = today.getDate();
  const selectedDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;

  return forkJoin({
    eventDatesMap: eventService.findEventDatesCheck(year, month),
    eventsForDay: eventService.findEventsOnDay(selectedDate)
  }).pipe(
    map((data) => ({
      eventDatesMap: data.eventDatesMap,
      eventsForDay: data.eventsForDay,
      selectedDate: selectedDate
    }))
  );
};
