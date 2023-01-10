import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {Observable, first, map, tap} from 'rxjs';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): Observable<boolean> {
    return this.authService.user$.pipe(
      first(),
      map(user => !!user),
      tap(pass => {
        if (pass) {
          return;
        }
        this.router.navigate(['sign-in']);
      })
    );
  }
}
