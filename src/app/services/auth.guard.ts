import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, UrlTree} from '@angular/router';
import {first, map, Observable, of, switchMap, tap} from 'rxjs';
import {Path} from '../app-routing.module';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> {
    return this.authService.user$.pipe(
      first(),
      switchMap(user => {
        const ticket = route.queryParams['ticket'];
        return ticket ? this.authService.validateUser(ticket) : of(user);
      }),
      map(user => {
        return this.router.parseUrl(user ? Path.Main : Path.SignIn);
      })
    );
  }
}
