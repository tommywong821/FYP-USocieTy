import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from 'src/app/services/auth.service';
import {environment} from 'src/environments/environment';
import {filter, Subject, switchMap, takeUntil} from 'rxjs';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss'],
})
export class SignInComponent implements OnInit {
  constructor(private authService: AuthService, private route: ActivatedRoute, private router: Router) {}

  destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.route.queryParams
      .pipe(
        takeUntil(this.destroy$),
        filter(params => !!params),
        switchMap(params => this.authService.validateUser(params))
      )
      .subscribe(user => {
        this.authService.signIn(user);
        this.router.navigate(['/home']);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
  }

  authenticateCAS(): void {
    const url = `${environment.casUrl}/login?service=${encodeURIComponent(environment.appUrl)}`;

    window.location.assign(url);
  }
}
