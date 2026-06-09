import { Injectable } from '@angular/core';
import { PageResponseModel } from '../shared/models/shared.model';
import {
  SpotReviewCreateModel,
  SpotReviewModel,
  SpotReviewUpdateModel,
} from '../shared/models/spot.model';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { EventOrganiserReviewCreateModel, EventOrganiserReviewModel, EventOrganiserReviewUpdateModel } from '../shared/models/event.model';

/**
 * ReviewService handles all available backend endpoints related to reviews for spots and event organisers.
 * This includes (not limited to) basic CRUD operations
 * - Paginated review GET methods with custom sort criteria
 * - POST methods to create reviews
 * - PUT methods to update reviews
 * - DELETE methods to delete reviews from the database
 *
 * Models and entities incorporated in the method: SpotReviewModel, EventOrganiserReviewModel
 *
 * All HTTP requests include credentials for cookie/session management.
 *
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  private apiUrl = environment.API_URL;

  constructor(private http: HttpClient) {}

  /* ============================================================= */
  /* ==================== SPOT REVIEWS CRUD ====================== */
  /* ============================================================= */

  /**
   * Method to retrieve paginated spot reviews from the database based on specific sort criteria.
   * @param pageNumber The number of the page that is being retrieved (0-indexed, so the first page starts at 0)
   * @param pageSize The size of the page (dependable on the use of the method but should not exceed 25)
   * @param spotId The ID of the spot for which reviews are being retrieved
   * @param sortOption The sorting option specified by the user
   * @returns An sorted page of Spot Review results
   */
  findAllSpotReviews(pageNumber: number, pageSize: number, spotId: number, sortOption: string) {
    return this.http.get<PageResponseModel<SpotReviewModel>>(
      this.apiUrl +
        `/review/spot/find-spot-reviews?pageNumber=${pageNumber}&pageSize=${pageSize}&spotId=${spotId}&sortOption=${sortOption}`,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to retrieve the current user's review for a specific spot.
   * @param spotId The ID of the spot for which the user's review is being retrieved
   * @returns The user's Spot Review for the specified spot
   */
  findUserSpotReview(spotId: number) {
    return this.http.get<SpotReviewModel>(this.apiUrl + `/review/spot/find-user-review?spotId=${spotId}`, {
      withCredentials: true,
    });
  }

  /**
   * Method to add a new review for a specific spot.
   * @param spotReviewCreate The review creation data
   * @returns The created Spot Review
   */
  addSpotReview(spotReviewCreate: SpotReviewCreateModel) {
    return this.http.post<SpotReviewModel>(this.apiUrl + `/review/spot/add-review`, spotReviewCreate, {
      withCredentials: true,
    });
  }

  /**
   * Method to update an existing review for a specific spot.
   * @param spotReviewUpdate The review update data
   * @returns The updated Spot Review
   */
  updateSpotReview(spotReviewUpdate: SpotReviewUpdateModel) {
    return this.http.put<SpotReviewModel>(this.apiUrl + `/review/spot/update-review`, spotReviewUpdate, {
      withCredentials: true,
    });
  }

  /**
   * Method to delete an existing review for a specific spot.
   * @param spotId The ID of the spot for which the review is being deleted
   * @param reviewId The ID of the review to be deleted
   * @returns The deleted Spot Review
   */
  deleteSpotReview(spotId: number, reviewId: number) {
    return this.http.delete<SpotReviewModel>(
      this.apiUrl + `/review/spot/remove-review?spotId=${spotId}&reviewId=${reviewId}`,
      {
        withCredentials: true,
      }
    );
  }

  /* ======================================================================== */
  /* ==================== EVENT ORGANISER REVIEWS CRUD ====================== */
  /* ======================================================================== */

  /**
   * Method to retrieve paginated event organiser reviews from the database based on specific sort criteria.
   * @param pageNumber The number of the page that is being retrieved (0-indexed, so the first page starts at 0)
   * @param pageSize The size of the page (dependable on the use of the method but should not exceed 25)
   * @param organiserId The ID of the event organiser for which reviews are being retrieved
   * @param sortOption The sorting option specified by the user
   * @returns An sorted page of Event Organiser Review results
   */
  findAllEventOrganiserReviews(pageNumber: number, pageSize: number, organiserId: number, sortOption: string) {
    return this.http.get<PageResponseModel<EventOrganiserReviewModel>>(
      this.apiUrl +
        `/review/organiser/find-organiser-reviews?pageNumber=${pageNumber}&pageSize=${pageSize}&organiserId=${organiserId}&sortOption=${sortOption}`,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to retrieve the current user's review for a specific event organiser.
   * @param organiserId The ID of the event organiser for which the user's review is being retrieved
   * @returns The user's Event Organiser Review for the specified organiser
   */
  findUserEventOrganiserReview(organiserId: number) {
    return this.http.get<EventOrganiserReviewModel>(
      this.apiUrl + `/review/organiser/find-user-review?organiserId=${organiserId}`, 
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to add a new review for a specific event organiser.
   * @param reviewCreate The review creation data
   * @returns The created Event Organiser Review
   */
  addEventOrganiserReview(reviewCreate: EventOrganiserReviewCreateModel) {
    return this.http.post<EventOrganiserReviewModel>(
      this.apiUrl + `/review/organiser/add-review`, 
      reviewCreate, 
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to update an existing review for a specific event organiser.
   * @param reviewUpdate The review update data
   * @returns The updated Event Organiser Review
   */
  updateEventOrganiserReview(reviewUpdate: EventOrganiserReviewUpdateModel) {
    return this.http.put<EventOrganiserReviewModel>(
      this.apiUrl + `/review/organiser/update-review`, 
      reviewUpdate, 
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to delete an existing review for a specific event organiser.
   * @param organiserId The ID of the event organiser for which the review is being deleted
   * @param reviewId The ID of the review to be deleted
   * @returns The deleted Event Organiser Review
   */
  deleteEventOrganiserReview(organiserId: number, reviewId: number) {
    return this.http.delete<EventOrganiserReviewModel>(
      this.apiUrl + `/review/organiser/remove-review?organiserId=${organiserId}&reviewId=${reviewId}`,
      {
        withCredentials: true,
      }
    );
  }
}
