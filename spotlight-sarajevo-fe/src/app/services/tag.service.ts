import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { TagModel } from '../shared/models/category.model';

@Injectable({
	providedIn: 'root'
})
export class TagService {
	private apiUrl = environment.API_URL;

	constructor(private http: HttpClient) {}

	findAll() {
		return this.http.get<TagModel[]>(this.apiUrl + '/tag/all', {
			withCredentials: true,
		});
	}
}
