import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Params} from '@angular/router';
import {Observable, map, tap, filter} from 'rxjs';
import {environment} from 'src/environments/environment';
import {CasResponse} from '../api/api';
import {User} from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private restful: HttpClient) {}

  validateUser(params: Params): Observable<User> {
    const url = `${environment.backendUrl}/auth/serviceValidate`;
    return this.restful.get(url, {params}).pipe(
      map(res => res as CasResponse),
      tap(res => {
        if (res.authenticationFailure) {
          console.error(res.authenticationFailure);
        }
      }),
      filter(res => !!res.authenticationFailure),
      map(res => ({
        userId: res.authenticationSucess.user,
        email: res.authenticationSucess.attributes.mail,
      }))
    );
  }
}
