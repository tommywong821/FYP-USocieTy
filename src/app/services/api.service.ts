import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, map} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Request} from '../api/common';
import {CreateEventRequest} from '../api/event';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private restful: HttpClient) {}

  call<Response>(request: Request): Observable<Response> {
    const url = `${environment.backend_url}${request.endpoint}`;
    const options = {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': environment.backend_url,
        'Access-Control-Request-Method': 'POST',
        'Access-Control-Max-Age': '86400',
      }),
    };

    return this.restful.post(url, request.body, options).pipe(map(res => res as Response));
  }

  signOutFromBackend(): Observable<any> {
    return this.restful.post(`${environment.backend_url}/auth/logout`, null);
  }

  healthCheck(): Observable<any> {
    return this.restful.get(`${environment.backend_url}/health`);
  }

  createEvent(request: CreateEventRequest): Observable<any> {
    return this.restful.post(`${environment.backend_url}/${request.endpoint}`, request.body);
  }
}
