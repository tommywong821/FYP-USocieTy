import {Component, OnInit} from '@angular/core';
import {ApiService} from '../services/api.service';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  constructor(private authService: AuthService, private apiService: ApiService) {}

  ngOnInit(): void {}

  signOut() {
    this.authService.signOut();
  }

  healthCheck() {
    this.apiService.healthCheck().subscribe({
      next: () => {
        console.log('health check done');
      },
    });
  }
}
