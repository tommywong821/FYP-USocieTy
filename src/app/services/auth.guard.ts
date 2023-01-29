import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {Path} from '../app-routing.module';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> | boolean {
    const userInfo = this.authService.loadUserFromLocalStorage();
    if (userInfo) {
      this.router.navigate([Path.Main]);
      return true;
    }
    this.router.navigate([Path.SignIn], {queryParams: route.queryParams});
    return false;
  }
}
