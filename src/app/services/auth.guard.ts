import {ThisReceiver} from '@angular/compiler';
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

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> | boolean {
    let userInfo = this.authService.loadUserFromLocalStorage();
    console.log(`AuthGuard local userInfo from localStorage: ${JSON.stringify(userInfo)}`);
    if (userInfo) {
      return true;
    }
    this.router.navigate([Path.SignIn], {queryParams: route.queryParams});
    return false;
    // return this.authService.user$.pipe(
    //   first(),
    //   switchMap(user => {
    //     const ticket = route.queryParams['ticket'];
    //     return ticket ? this.authService.validateUser(ticket) : of(user);
    //   }),
    //   map(user => {
    //     return this.router.parseUrl(user ? Path.Main : Path.SignIn);
    //   })
    // );
  }
}
