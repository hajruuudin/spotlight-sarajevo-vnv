import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TouristGuideCreateModel, TouristGuideModel, TouristGuideOverviewModel, TouristGuideShorthandModel, TouristGuideUpdateModel } from '../shared/models/tourist.guide.model';
import { environment } from '../../environments/environment';
import { PageResponseModel } from '../shared/models/shared.model';

/**
 * Service for interacting with the Tourist Guide API.
 * Provides methods to fetch tourist guides and create new ones.
 * All requests include credentials for authentication.
 * 
 * Models and entities incorporated in the methods: TouristGuideShorthandModel, TouristGuideOverviewModel, TouristGuideCreateModel
 * 
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Injectable({
  providedIn: 'root'
})
export class TouristGuideService {
  private API_URL = environment.API_URL + '/guide';

  constructor(private http: HttpClient) {}

  /**
   * Fetches all tourist guides in shorthand format
   */
  findAllGuides(): Observable<TouristGuideShorthandModel[]> {
    return this.http.get<TouristGuideShorthandModel[]>(`${this.API_URL}/all`, {
      withCredentials: true
    });
  }

  findGuidesPaginated(
    pageNumber: number, pageSize: number, searchTerm: string, sortOption: string
  ) {
    return this.http.get<PageResponseModel<TouristGuideShorthandModel>>(`${this.API_URL}?pageNumber=${pageNumber}&pageSize=${pageSize}&searchTerm=${searchTerm}&sortOption=${sortOption}`, {
      withCredentials: true
    })
  }

  /**
   * Fetches guides based on a specific category ID
   * @param categoryId The ID of the category to filter guides by
   */
  findGuidesByCategory(categoryId: number): Observable<TouristGuideShorthandModel[]> {
    return this.http.get<TouristGuideShorthandModel[]>(`${this.API_URL}/category/${categoryId}`, {
      withCredentials: true
    });
  }

  /**
   * Fetches a specific guide overview by its slug
   * @param slug The unique string identifier for the guide
   */
  findGuideOverview(slug: string): Observable<TouristGuideOverviewModel> {
    return this.http.get<TouristGuideOverviewModel>(`${this.API_URL}/${slug}`, {
      withCredentials: true
    });
  }

  /**
   * Creates a new tourist guide
   * @param request The guide creation data
   */
  createGuide(request: TouristGuideCreateModel): Observable<TouristGuideOverviewModel> {
    return this.http.post<TouristGuideOverviewModel>(`${this.API_URL}/create`, request, {
      withCredentials: true
    });
  }

  /**
   * Updates an existing tourist guide
   * @param guideId The ID of the guide to update
   * @param request The updated guide data
   */
  updateGuide(request: TouristGuideUpdateModel) {
    return this.http.put<TouristGuideModel>(`${this.API_URL}`, request, {
      withCredentials: true
    });
   }

   /**
    * Deletes a tourist guide by its ID
    * @param guideId The ID of the guide to delete
    */
   deleteGuide(guideId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${guideId}`, {
      withCredentials: true
    });
   }
}