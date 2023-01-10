import {HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Params} from '@angular/router';
import {BehaviorSubject, filter, map, Observable, of, tap} from 'rxjs';
import {validateUserEndpoint, validateUserResponse} from '../api/auth';
import {User} from '../model/user';
import {ApiService} from './api.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private apiService: ApiService) {}

  private _user$ = new BehaviorSubject<User | null>(null);

  get user$(): Observable<User | null> {
    return this._user$;
  }

  signIn(user: User): Observable<User> {
    this._user$.next(user);
    return of(user);
  }

  signOut(): Observable<boolean> {
    this._user$.next(null);
    window.location.reload();
    return of(true);
  }

  validateUser(queryParams: Params): Observable<User> {
    const request = {
      endpoint: validateUserEndpoint,
      queryParam: new HttpParams().set('ticket', queryParams['ticket']),
      body: null,
    };
    return this.apiService.call<validateUserResponse>(request).pipe(
      tap(res => {
        if (res.authenticationFailure) {
          console.error(res.authenticationFailure);
        }
      }),
      filter(res => !!res.authenticationFailure),
      map(res => {
        return {
          name: res.authenticationSuccess.attributes.name,
          email: res.authenticationSuccess.attributes.mail,
        };
      })
    );
  }
}
