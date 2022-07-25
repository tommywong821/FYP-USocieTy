import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CasResponse} from 'src/app/api/api';
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
    private restful: HttpClient
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (!params) {
        return;
      }

      const url = `${environment.backendUrl}/auth/serviceValidate`;
      this.restful.get(url, {params}).subscribe({
        next: res => {
          const casResponse = res as CasResponse;
          if (casResponse.authenticationFailure.value) {
            // TODO report login error
            console.error(casResponse.authenticationFailure);
            return;
          }

          const name = casResponse.authenticationSucess.user;
          const email = casResponse.authenticationSucess.attributes.mail;

          this.authService.signIn({name, email});
          this.router.navigate(['/home']);
        },
      });
    });
  }

  authenticateCAS(): void {
    const url = `${environment.casUrl}/login?service=${encodeURIComponent(environment.appUrl)}`;

    window.location.assign(url);
  }
}
