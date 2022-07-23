import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {User} from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
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
}
