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
    if (userInfo) {
      this.router.navigate([Path.Main]);
      return true;
    }
    this.router.navigate([Path.SignIn], {queryParams: route.queryParams});
    return false;
  }
}
