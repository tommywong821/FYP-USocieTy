import {Injectable} from '@angular/core';
import {Params} from '@angular/router';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {environment} from 'src/environments/environment';
import {validateUserEndpoint} from '../api/auth';
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
    return of(true);
  }

  validateUser(queryParams: Params): Observable<User> {
    const request = {
      endpoint: validateUserEndpoint,
      queryParam: null,
      body: {
        ticket: queryParams['ticket'],
      },
    };
    return this.apiService.call<User>(request);
  }

  saveUserToLocalStorage(user: User) {
    console.log(`saveUserToLocalStorage: ${JSON.stringify(user)}`);
    this._user$.next(user);
    localStorage.setItem(environment.user_key, JSON.stringify(user));
  }

  loadUserFromLocalStorage(): User | null {
    if (!this._user$.value) {
      console.log(`loadUserFromLocalStorage: this._user$.value is null`);
      let fromLocalStorage = localStorage.getItem(environment.user_key);
      if (fromLocalStorage) {
        let user = JSON.parse(fromLocalStorage);
        this._user$.next(user);
      }
    }
    console.log(`loadUserFromLocalStorage: this._user$.value is not null`);
    return this._user$.value;
  }
}
