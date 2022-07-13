import {Component} from '@angular/core';
import {environment} from "../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'appName';

  constructor(private http: HttpClient) {
    console.log(`[${this.constructor.name}] constructor`);
    console.log(`app_url: `, environment.app_url)
    console.log(`cas_url: `, environment.cas_url)
    console.log(`cas_validate_url: `, environment.cas_validate_url)

    console.log(`loginUser`, window.sessionStorage.getItem('loginUser'))
    console.log(`ticketUser`, window.sessionStorage.getItem('ticketUser'))

    const url = 'https://cas.ust.hk/cas/p3/serviceValidate?service=' + environment.app_url + '&ticket=' + window.sessionStorage.getItem('ticketUser')
    console.log(`login info url:`, url)
    const httpOptions = {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': 'https://cas.ust.hk/cas',
        'Access-Control-Allow-Methods': 'GET, PUT, POST, DELETE, OPTIONS',
        'Access-Control-Max-Age': '86400'
      })
    };

    this.http.get(url, httpOptions).subscribe((response) => {
      console.log(`url response: `, response)
    })

    const url2 = 'https://cas.ust.hk/cas/p3/serviceValidate?service=https://ngok3fyp-frontend.herokuapp.com&ticket=' + window.sessionStorage.getItem('ticketUser')

    this.http.get(url2, httpOptions).subscribe((response) => {
      console.log(`url2 response: `, response)
    })
  }
}
