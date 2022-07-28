import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {environment} from "../environments/environment";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'appName';
  params: any
  userInfo: any

  constructor(private restful: HttpClient,
              private route: ActivatedRoute) {
    console.log(`[${this.constructor.name}] constructor`);
    this.userInfo = null
    this.params = null
    // this.route.queryParams.subscribe({
    //   next: (params) => {
    //     this.params = params
    //     if (this.params) {
    //       this.restful.get(`${environment.backend_url}/auth/serviceValidate`, {
    //         params: this.params
    //       }).subscribe({
    //         next: (res) => {
    //           console.log(`result: `, res)
    //           console.log(`this.userInfo: `, this.userInfo)
    //           this.userInfo = res
    //         }
    //       })
    //     }
    //   }
    // })
  }

  loginWithRedirect() {
    console.log('loginWithRedirect button is clicking')
    window.location.href = `${environment.cas_url}/login?service=${encodeURIComponent(environment.app_url)}`
  }

  serviceValidate() {
    this.restful.get(`${environment.backend_url}/auth/serviceValidate`, {
      params: {
        ticket: "ST-1658309663545-VL1xWCZdhfUkaBfV9mjTThyQ8"
      }
    }).subscribe(
      {
        next: (res) => {
          console.log(`result: `, res)
          console.log(`this.userInfo: `, this.userInfo)
          this.userInfo = res
        }
      }
    )
  }

  displayJson(obj: any) {
    return JSON.stringify(obj)
  }
}
