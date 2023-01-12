import {Component, OnInit} from '@angular/core';
import {mergeMap} from 'rxjs';
import {environment} from 'src/environments/environment';
import {User} from '../model/user';
import {ApiService} from '../services/api.service';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  constructor(private authService: AuthService, private apiService: ApiService) {}

  user: User | null = null;

  ngOnInit(): void {
    this.authService.user$.subscribe({
      next: user => {
        this.user = user;
      },
    });
  }

  signOut() {
    //remove cookie
    this.apiService
      .signOutFromBackend()
      .pipe(
        mergeMap(() => {
          //remove user in guard service
          return this.authService.signOut();
        })
      )
      .subscribe(() => {
        //logout from hkust cas server
        const url = `${environment.cas_url}/logout?service=${encodeURIComponent(environment.app_url)}`;

        window.location.assign(url);
      });
  }

  healthCheck() {
    this.apiService.healthCheck().subscribe({
      next: () => {
        console.log('health check done');
      },
    });
  }
}
