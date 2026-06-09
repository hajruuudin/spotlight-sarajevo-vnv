import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { LoggedUserModel, LoginModel, PreferencesModel, SystemUserModel } from '../../shared/models/auth.model';

/**
 * AuthService handles all authentication-related operations in the application.
 * 
 * Provides methods for:
 * - Google OAuth registration and login
 * - System (email/password) registration and login
 * - Logging out
 * 
 * All HTTP requests include credentials for cookie/session management.
 * 
 * @version 1.0.0
 * @author hajrudin.imamovic
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  /** Google OAuth client ID from environment configuration */
  public googleClientId: string = environment.GOOGLE_CLIENT_ID;

  /** Base API URL from environment configuration */
  private apiUrl: string = environment.API_URL;

  constructor(
    private http: HttpClient
  ) {}

  /**
   * Registers a user using Google OAuth credentials.
   * @param idToken - The Google ID token obtained after OAuth login.
   * @returns Observable<any> - The response from the API.
   */
  storeGoogleCredentials(idToken: string) {
    return this.http.post(`${this.apiUrl}/auth/google/register`, JSON.stringify({ idToken }), {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true
    });
  }

  /**
   * Registers a user in the system with a standard email/password account.
   * @param request - The user's credentials and data as a SystemUserModel.
   * @returns Observable<any> - The response from the API.
   */
  storeSystemCredentials(request: SystemUserModel) {
    return this.http.post(`${this.apiUrl}/auth/system/register`, request, {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true
    });
  }

  /**
   * Registers user preferences in the system.
   * @param request - PreferencesModel containing user's settings.
   * @returns Observable<any> - The response from the API.
   */
  registerToSystem(request: PreferencesModel) {
    return this.http.post<LoggedUserModel>(`${this.apiUrl}/auth/register`, request, {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true
    });
  }

  /**
   * Logs in a user using a Google ID token.
   * @param idToken - The Google ID token obtained after OAuth login.
   * @returns Observable<any> - The response from the API.
   */
  loginWithGoogle(idToken: string) {
    return this.http.post<LoggedUserModel>(`${this.apiUrl}/auth/login/google`, JSON.stringify({ idToken }), {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true
    });
  }

  /**
   * Logs in a user using system credentials.
   * @param request - LoginModel containing username and password.
   * @returns Observable<any> - The response from the API.
   */
  loginWithSystem(request: LoginModel) {
    return this.http.post<LoggedUserModel>(`${this.apiUrl}/auth/login/system`, request, {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true
    });
  }

  /**
   * Checks weather a user is authenticated on the system and returns the 
   * user data if required for the local storage.
   * @returns  Observable<LoggedUserModel | null> - The response from the backend. Null if the 
   * session is not present and LoggedUserModel if the session is present.
   */
  isAuthenticated(){
    return this.http.post<LoggedUserModel | null>(`${this.apiUrl}/auth/me`, null, {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true
    })
  }

  /**
   * Logs out the currently authenticated user.
   * @returns Observable<any> - The response from the API.
   */
  logout() {
    return this.http.post(`${this.apiUrl}/auth/logout`, {}, {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true
    });
  }
}
