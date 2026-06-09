import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import {
  SpotMapModel,
  SpotOverviewModel,
  SpotShorthandModel,
  SpotUpdateModel,
  SpotCreateModel,
} from '../shared/models/spot.model';
import { PageResponseModel } from '../shared/models/shared.model';

/**
 * SpotService handles all available backend endpoints related to the spot object.
 * This includes (not limited to) basic CRUD operations
 * - Paginated spot GET method with custom search criteria
 * - POST method to create a spot, available only to admin users
 * - PUT method to update a spot, available only to admin users
 * - DELETE method to delete a spot from the database along with its information, available only to admin users
 * ... among other methods.
 *
 * Models and entities incorporated in the method: SpotShorthandModel, SpotMapModel, SpotOverviewModel
 *
 * All HTTP requests include credentials for cookie/session management.
 *
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Injectable({
  providedIn: 'root',
})
export class SpotService {
  private apiUrl = environment.API_URL;

  constructor(private http: HttpClient) {}

  /**
   * Method to retrieve paginated spot shorthands from the database based on specific search, sort and filter criteria.
   *
   * @param pageNumber The number of the page that is being retrieved (0-indexed, so the first page starts at 0)
   * @param pageSize The size of the page (dependable on the use of the method but should not exceed 25)
   * @param searchTerm The search query specified by the user in the search bar (Spots are searched against their Official Name as specified on Google Maps / any reliable online resource)
   * @param sortOption The sorting option specified by the user
   * @param categoryIds The category Ids by which the user filters the result. If left empty, all spot categories will be taken into account.
   * @param userLatitude The user's current latitude for proximity sorting (optional)
   * @param userLongitude The user's current longitude for proximity sorting (optional)
   * @param excludeVisited Filter to show only non-visited spots (optional)
   * @returns An sorted page of Spot Shothand results
   *
   */
  findSpotsPaginated(
    pageNumber: number,
    pageSize: number,
    searchTerm: string,
    sortOption: string,
    categoryIds: number[],
    userLatitude: number | null = null,
    userLongitude: number | null = null,
    excludeVisited: boolean | null = null
  ) {
    let url = this.apiUrl +
      `/spot/find-spots?pageNumber=${pageNumber}&pageSize=${pageSize}&searchTerm=${searchTerm}&sortOption=${sortOption}&categoryIds=${categoryIds}`;
    
    // Add location parameters if provided
    if (userLatitude !== null && userLongitude !== null) {
      url += `&userLatitude=${userLatitude}&userLongitude=${userLongitude}`;
    }
    
    // Add excludeVisited parameter if provided
    if (excludeVisited !== null) {
      url += `&excludeVisited=${excludeVisited}`;
    }
    
    return this.http.get<PageResponseModel<SpotShorthandModel>>(
      url,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to retrieve spot map pin data for displaying spots on a fullscreen map.
   * Returns lightweight spot data including coordinates, suitable for map markers.
   *
   * @param searchTerm The search query to filter spots by name
   * @param categoryIds The category Ids by which the user filters the result. If left empty, all spot categories will be taken into account.
   * @returns An array of SpotMapPinModel objects representing spots with their location data
   */
  findSpotsForMap(searchTerm: string, categoryIds: number[]) {
    return this.http.get<SpotMapModel[]>(
      this.apiUrl +
        `/spot/find-spots-map?searchTerm=${searchTerm}&categoryIds=${categoryIds}`,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to retrieve detailed overview information about a specific spot by its slug.
   *
   * @param spotSlug The unique string identifier for the spot
   * @returns The detailed spot overview information
   */
  findSpotOverview(spotSlug: string) {
    return this.http.get<SpotOverviewModel>(
      this.apiUrl + `/spot/find-spot-overview?spotSlug=${spotSlug}`,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to check if a spot is marked as visited by the current user.
   * @param spotId The ID of the spot to check
   * @returns A boolean indicating if the spot is marked as visited
   */
  checkIfSpotIsVisited(spotId: number) {
    return this.http.get<Boolean>(
      this.apiUrl + `/spot/visited-spot/check?spotId=${spotId}`,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to add a spot to the user's visited spots list.
   * 
   * @param spotId The ID of the spot to be added as visited
   * @returns An observable representing the result of the add operation
   */
  addSpotAsVisited(spotId: number) {
    return this.http.post(
      this.apiUrl + `/user/visited-spot/add`,
      {
        spotId: spotId
      },
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to remove a spot from the user's visited spots list.
   * @param spotId The ID of the spot to be removed from visited
   * @returns An observable representing the result of the remove operation
   */
  removeSpotFromVisited(spotId: number) {
    return this.http.delete(
      this.apiUrl + `/user/visited-spot/remove?spotId=${spotId}`,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to update an existing spot with new information.
   * This is an admin-only operation.
   * 
   * @param spotUpdateModel The SpotUpdateModel containing all updated spot information
   * @returns An observable representing the result of the update operation
   */
  updateSpot(spotUpdateModel: SpotUpdateModel) {
    return this.http.put(
      this.apiUrl + `/spot`,
      spotUpdateModel,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to create a new spot with provided information.
   * This is an admin-only operation.
   * 
   * @param spotCreateModel The SpotCreateModel containing all spot information for the new spot
   * @returns An observable representing the result of the create operation
   */
  createSpot(spotCreateModel: SpotCreateModel) {
    return this.http.post(
      this.apiUrl + `/spot`,
      spotCreateModel,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to delete a spot from the database.
   * This is an admin-only operation.
   * 
   * @param spotId The ID of the spot to be deleted
   * @returns An observable representing the result of the delete operation
   */
  deleteSpot(spotId: number) {
    return this.http.delete(
      this.apiUrl + `/spot/${spotId}`,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to retrieve recently added spots for admin dashboard.
   * This is an admin-only operation.
   * 
   * @param limit The maximum number of recent spots to retrieve (default: 5)
   * @returns An observable containing an array of recently added SpotShorthandModel objects
   */
  getRecentlyAddedSpots(limit: number = 5) {
    return this.http.get<SpotShorthandModel[]>(
      this.apiUrl + `/spot/admin/recently-added?limit=${limit}`,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Method to retrieve the total count of spots in the system.
   * This is an admin-only operation.
   * 
   * @returns An observable containing the total count of spots
   */
  getSpotsTotalCount() {
    return this.http.get<number>(
      this.apiUrl + `/spot/admin/count`,
      {
        withCredentials: true,
      }
    );
  }
}
