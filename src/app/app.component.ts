import {Component} from '@angular/core';
import {environment} from "../environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'appName';

  constructor() {
    console.log(`[${this.constructor.name}] constructor`);
    console.log(`app_url: `, environment.app_url)
    console.log(`cas_url: `, environment.cas_url)
    console.log(`cas_validate_url: `, environment.cas_validate_url)
  }
}
