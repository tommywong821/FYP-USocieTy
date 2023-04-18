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
    localStorage.removeItem(environment.user_key);
    return of(true);
  }

  validateUser(queryParams: Params): Observable<User> {
    const request = {
      // endpoint: validateUserEndpoint,
      endpoint: '/auth/mockServiceValidate',
      body: {
        ticket: queryParams['ticket'],
      },
    };
    return this.apiService.call<User>(request);
  }

  saveUserToLocalStorage(user: User) {
    this._user$.next(user);
    localStorage.setItem(environment.user_key, JSON.stringify(user));
  }

  loadUserFromLocalStorage(): User | null {
    if (!this._user$.value) {
      const fromLocalStorage = localStorage.getItem(environment.user_key);
      if (fromLocalStorage) {
        const user: User = JSON.parse(fromLocalStorage);
        const formattedDate = new Date().toLocaleString('en-US', {timeZone: 'Asia/Hong_Kong'});
        const currentDateObj = new Date(formattedDate);
        currentDateObj.setDate(currentDateObj.getDate());
        if (new Date(user.expireDate) < currentDateObj) {
          // token expire
          localStorage.removeItem(environment.user_key);
          return null;
        }
        this._user$.next(user);
      }
    }
    return this._user$.value;
  }
}
