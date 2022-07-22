import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor() {}

  authorizeSSO(): void {
    const url = `${environment.cas_url}/login?service=${encodeURIComponent(
      environment.app_url
    )}`;

    window.location.assign(url);
  }
}
