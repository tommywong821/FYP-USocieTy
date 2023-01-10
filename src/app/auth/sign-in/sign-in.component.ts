import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {filter, Subject, switchMap, takeUntil} from 'rxjs';
import {ApiService} from 'src/app/services/api.service';
import {AuthService} from 'src/app/services/auth.service';
import {environment} from 'src/environments/environment';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss'],
})
export class SignInComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService
  ) {}

  destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.route.queryParams
      .pipe(
        takeUntil(this.destroy$),
        filter(params => Object.keys(params).length != 0),
        switchMap(queryParams => this.authService.validateUser(queryParams))
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
    const url = `${environment.cas_url}/login?service=${encodeURIComponent(environment.app_url)}`;

    window.location.assign(url);
  }

  healthCheck(): void {
    console.log('health check');
    this.apiService.healthCheck().subscribe({
      next: () => {
        console.log('health check done');
      },
    });
  }
}
