import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, map} from 'rxjs';
import {environment} from 'src/environments/environment';
import {Request} from '../api/common';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private restful: HttpClient) {}

  call<Response>(request: Request): Observable<Response> {
    const url = `${environment.backend_url}${request.endpoint}`;

    return this.restful.post(url, request.body, {withCredentials: true}).pipe(map(res => res as Response));
  }

  healthCheck() {
    return this.restful.get(`${environment.backend_url}/health`, {withCredentials: true});
  }
}
