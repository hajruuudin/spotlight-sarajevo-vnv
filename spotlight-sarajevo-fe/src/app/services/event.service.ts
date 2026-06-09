import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { PageResponseModel } from '../shared/models/shared.model';
import {
  EventCreateModel,
  EventDateCheckModel,
  EventOrganiserCreateModel,
  EventOrganiserModel,
  EventOrganiserUpdateModel,
  EventOverviewModel,
  EventShorthandModel,
  EventUpdateModel,
} from '../shared/models/event.model';
import { EventOrganiserModal } from '../components/modals/event-organiser-modal/event-organiser-modal';

/**
 * EventService handles all available backend endpoints related to events.
 * This includes (not limited to) basic retrieval operations
 * - Paginated event GET methods with custom search, sort and filter criteria
 * - GET method to retrieve event overview by slug
 *
 * Models and entities incorporated in the method: EventShorthandModel, EventOverviewModel
 *
 * All HTTP requests include credentials for cookie/session management.
 *
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Injectable({
  providedIn: 'root',
})
export class EventService {
  private apiUrl = environment.API_URL;

  constructor(private http: HttpClient) {}

  /**
   * Method to retrieve paginated event shorthands from the database based on specific search, sort and filter criteria.
   *
   * @param pageNumber The number of the page that is being retrieved (0-indexed, so the first page starts at 0)
   * @param pageSize The size of the page (dependable on the use of the method but should not exceed 25)
   * @param searchTerm The search query specified by the user in the search bar (Events are searched against their Official Name as specified on any reliable online resource)
   * @param sortOption The sorting option specified by the user
   * @param categoryIds The category Ids by which the user filters the result. If left empty, all event categories will be taken into account.
   * @returns A sorted page of Event Shorthand results
   *
   */
  findEventsPaginated(
    pageNumber: number,
    pageSize: number,
    searchTerm: string,
    sortOption: string,
    categoryIds: number[],
  ) {
    return this.http.get<PageResponseModel<EventShorthandModel>>(
      this.apiUrl +
        `/event/find-events?pageNumber=${pageNumber}&pageSize=${pageSize}&searchTerm=${searchTerm}&sortOption=${sortOption}&categoryIds=${categoryIds}`,
      {
        withCredentials: true,
      },
    );
  }

  /**
   * Method to find an event overview based on the slug. Used in Event Overview Page.
   * Data for Event Reviews is not contained within this default response.
   *
   * @param eventSlug
   * @returns {EventOverviewModel}
   */
  findEventOverview(eventSlug: string) {
    return this.http.get<EventOverviewModel>(
      this.apiUrl + `/event/find-event-overview?eventSlug=${eventSlug}`,
      {
        withCredentials: true,
      },
    );
  }

  /**
   * Method to check if the logged-in user has marked a specific event as attended.
   *
   * @param eventId The unique identifier of the event to be checked.
   * @returns An observable containing a boolean indicating attendance status.
   */
  checkIfEventIsAttended(eventId: number) {
    return this.http.get<Boolean>(this.apiUrl + `/event/attended-event/check?eventId=${eventId}`, {
      withCredentials: true,
    });
  }

  /**
   * Method to add an event to the list of attended events for the logged-in user.
   *
   * @param eventId The unique identifier of the event to be marked as attended.
   * @returns An observable representing the completion of the operation.
   */
  addEventAsAttended(eventId: number) {
    return this.http.post(
      this.apiUrl + `/user/attended-event/add`,
      {
        eventId: eventId,
      },
      {
        withCredentials: true,
      },
    );
  }

  /**
   * Method to remove an event from the list of attended events for the logged-in user.
   *
   * @param eventId The unique identifier of the event to be removed from attended list.
   * @returns An observable representing the completion of the operation.
   */
  removeEventFromAttended(eventId: number) {
    return this.http.delete(this.apiUrl + `/user/attended-event/remove?eventId=${eventId}`, {
      withCredentials: true,
    });
  }

  /**
   * Method to retrieve a map of dates to booleans indicating whether events exist on each day
   * of the specified month. Re-called each time the user navigates to a different month.
   *
   * @param year The year of the month to check
   * @param month The month to check (1-indexed, e.g. 1 = January)
   * @returns A map of date strings (YYYY-MM-DD) to booleans
   */
  findEventDatesCheck(year: number, month: number) {
    return this.http.get<EventDateCheckModel>(
      this.apiUrl + `/event/event-dates-check?year=${year}&month=${month}`,
      {
        withCredentials: true,
      },
    );
  }

  /**
   * Method to retrieve all events occurring on a specific date.
   *
   * @param date The date in YYYY-MM-DD format
   * @returns An array of EventShorthandModel objects for that date
   */
  findEventsOnDay(date: string) {
    return this.http.get<EventShorthandModel[]>(this.apiUrl + `/event/events-on-day/${date}`, {
      withCredentials: true,
    });
  }

  /**
   * Method to retrieve all event organisers.
   * Used in Add Event and Edit Event functionality on the FE.
   *
   * @returns An array of all EventOrganiserModel objects
   */
  findEventOrganisers() {
    return this.http.get<EventOrganiserModel[]>(this.apiUrl + `/event/organisers/all`, {
      withCredentials: true,
    });
  }

  /**
   * Method to create a new event organiser.
   * This is an admin-only operation.
   *
   * @param organiserCreateModel The EventOrganiserCreateModel containing organiser information
   * @returns An observable containing the created EventOrganiserModel
   */
  addNewOrganiser(organiserCreateModel: EventOrganiserCreateModel) {
    return this.http.post<EventOrganiserModel>(
      this.apiUrl + `/event/organiser`,
      organiserCreateModel,
      {
        withCredentials: true,
      },
    );
  }

  /**
   * Method to create a new event.
   * This is an admin-only operation.
   *
   * @param eventCreateModel The EventCreateModel containing all event information for creation
   * @returns An observable representing the result of the create operation
   */
  createEvent(eventCreateModel: EventCreateModel) {
    return this.http.post(this.apiUrl + `/event`, eventCreateModel, {
      withCredentials: true,
    });
  }

  /**
   * Method to update an existing event with new information.
   * This is an admin-only operation.
   *
   * @param eventUpdateModel The EventUpdateModel containing all updated event information
   * @returns An observable representing the result of the update operation
   */
  updateEvent(eventUpdateModel: EventUpdateModel) {
    return this.http.put(this.apiUrl + `/event`, eventUpdateModel, {
      withCredentials: true,
    });
  }

  /**
   * Method to update existing event-organisers with new information.
   * This is an admin-only operation.
   * 
   * @param eventOrganiserUpdateModel The EventOrganiserUpdateModel includes the data being updated
   * @returns The updated event organiser model
   */
  updateEventOrganiser(eventOrganiserUpdateModel: EventOrganiserUpdateModel) {
    return this.http.put<EventOrganiserModel>(this.apiUrl + `/event/organiser`, eventOrganiserUpdateModel, {
      withCredentials: true,
    });
  }

  /**
   * Method to delete an event from the database.
   * This is an admin-only operation.
   *
   * @param eventId The ID of the event to be deleted
   * @returns An observable representing the result of the delete operation
   */
  deleteEvent(eventId: number) {
    return this.http.delete(this.apiUrl + `/event/${eventId}`, {
      withCredentials: true,
    });
  }

  /**
   * Method to retrieve recently added events for admin dashboard.
   * This is an admin-only operation.
   * 
   * @param limit The maximum number of recent events to retrieve (default: 5)
   * @returns An observable containing an array of recently added EventShorthandModel objects
   */
  getRecentlyAddedEvents(limit: number = 5) {
    return this.http.get<EventShorthandModel[]>(
      this.apiUrl + `/event/admin/recently-added?limit=${limit}`,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to retrieve the total count of events in the system.
   * This is an admin-only operation.
   * 
   * @returns An observable containing the total count of events
   */
  getEventsTotalCount() {
    return this.http.get<number>(
      this.apiUrl + `/event/admin/count`,
      {
        withCredentials: true,
      }
    );
  }
}
