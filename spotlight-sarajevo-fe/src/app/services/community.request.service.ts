import { Inject, Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { CommunityRequestCreateModel, CommunityRequestModel, CommunityRequestOverviewModel, CommunityRequestStatusUpdate } from "../shared/models/community.request.model";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CommunityRequestService {
  private apiUrl = environment.API_URL + '/community-request';

  constructor(private http: HttpClient) {}

  /** Fetch all the community requests made by a specific user
   * @return An observable containing an array of CommunityRequestModel objects
   */
  getUserRequests() {
    return this.http.get<CommunityRequestModel[]>(`${this.apiUrl}/get-user-requests`, {
        withCredentials: true
    });
  }

  /**
   * Create a new community request based on the users provided data.
   * In case the request is successfully created, the backend will return the created CommunityRequestModel object.
   * In case the request fails, an error will be thrown.
   * 
   * Optionally, additional data related to the request can be provided in the requestBody field of the CommunityRequestCreateModel.
   *
   * @param request 
   * @return An observable containing the created CommunityRequestModel object
   */
  createCommunityRequest(request: CommunityRequestCreateModel) {
    return this.http.post<CommunityRequestModel>(`${this.apiUrl}/create-request`, request, {
        withCredentials: true
    });
  }
  
  /**
   * Fetch all the community requests made by all users. This endpoint is intended for admin use only.
   * In case the request is successfully processed, the backend will return an array of CommunityRequestModel objects.
   * In case the request fails, an error will be thrown.
   * 
   * @returns An observable containing an array of CommunityRequestModel objects
   */
  getCommunityRequests(filter: string){
    return this.http.get<CommunityRequestModel[]>(`${this.apiUrl}/admin/get-all-requests?filterOption=${filter}`, {
      withCredentials: true
    })
  }
  
  /**
   * Update the status of a community request. This endpoint is intended for admin use only.
   * In case the request is successfully processed, the backend will return the updated CommunityRequestModel object.
   * In case the request fails, an error will be thrown.
   * 
   * @param statusUpdate The CommunityRequestStatusUpdate object containing the request ID and new status
   * @returns An observable containing the updated CommunityRequestModel object
   */
  updateCommunityRequestStatus(request: CommunityRequestStatusUpdate) {
    return this.http.put<CommunityRequestModel>(
      `${this.apiUrl}/admin/update-request-status`,
      request,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Fetch the full overview information for a specific community request. This endpoint is intended for admin use only.
   * In case the request is successfully processed, the backend will return a CommunityRequestOverviewModel object containing the full information about the request.
   * In case the request fails, an error will be thrown.
   * 
   * @param requestId The ID of the community request for which the overview information is requested
   * @returns An observable containing a CommunityRequestOverviewModel object with the full information about the request
   */
  getRequestOverview(requestId: number) {
    return this.http.get<CommunityRequestOverviewModel>(
      `${this.apiUrl}/admin/get-request/${requestId}`,
      {
        withCredentials: true,
      }
    );
  }

  /**
   * Fetch recently added community requests for admin dashboard. This endpoint is intended for admin use only.
   * In case the request is successfully processed, the backend will return an array of CommunityRequestModel objects.
   * In case the request fails, an error will be thrown.
   * 
   * @param limit The maximum number of recent requests to retrieve (default: 10)
   * @returns An observable containing an array of recently added CommunityRequestModel objects
   */
  getRecentlyAddedCommunityRequests(limit: number = 10) {
    return this.http.get<CommunityRequestModel[]>(
      `${this.apiUrl}/admin/get-recently-added?limit=${limit}`,
      {
        withCredentials: true,
      }
    );
  }
}