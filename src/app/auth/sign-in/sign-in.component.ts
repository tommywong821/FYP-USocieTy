import {Path} from './../../app-routing.module';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {filter, Subject, switchMap, takeUntil, tap} from 'rxjs';
import {User} from 'src/app/model/user';
import {AuthService} from 'src/app/services/auth.service';
import {environment} from 'src/environments/environment';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss'],
})
export class SignInComponent implements OnInit {
  constructor(private authService: AuthService, private route: ActivatedRoute, private router: Router) {}

  destroy$ = new Subject<void>();

  loading$ = new Subject<void>();

  ngOnInit(): void {
    this.route.queryParams
      .pipe(
        takeUntil(this.destroy$),
        filter(params => Object.keys(params).length != 0),
        switchMap(queryParams => this.authService.validateUser(queryParams))
      )
      .subscribe((user: User) => {
        this.authService.saveUserToLocalStorage(user);
        this.router.navigate([Path.Main, Path.Event]);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
  }

  authenticateCAS(): void {
    const url = `${environment.cas_url}/login?service=${encodeURIComponent(environment.app_url)}`;

    window.location.assign(url);
  }
}
