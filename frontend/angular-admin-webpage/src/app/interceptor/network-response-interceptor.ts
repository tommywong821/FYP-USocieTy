import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpStatusCode,
} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {catchError, Observable, throwError} from 'rxjs';
import {AuthService} from '../services/auth.service';

@Injectable()
export class NetworkResponseInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMsg = '';
        if (error.error instanceof ErrorEvent) {
          console.log('this is client side error');
          errorMsg = `Error: ${error.error.message}`;
        } else {
          if (error.status === HttpStatusCode.Unauthorized || error.status === HttpStatusCode.Forbidden) {
            alert('Unauthorized or Login Session is over. Please login again');
            this.authService.signOut().subscribe({
              next: () => {
                this.router.navigate(['sign-in']);
              },
            });
          }
          console.log('this is server side error');
          errorMsg = `Error Code: ${error.status},  Message: ${error.message}`;
        }
        console.log(errorMsg);
        return throwError(() => errorMsg);
      })
    );
  }
}
