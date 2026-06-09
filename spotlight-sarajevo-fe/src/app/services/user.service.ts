import { Inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { UserInfoModel } from '../shared/models/auth.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly apiUrl: string = environment.API_URL;

  constructor(private http: HttpClient) {}

  getUserDetails() {
    return this.http.get<UserInfoModel>(this.apiUrl + `/user/info`, {
        withCredentials: true
    })
  }
}
