import {Injectable} from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { KeycloakAuthService } from '../service/keycloak-auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private keycloakAuth: KeycloakAuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Get the auth header from the service.
    if (this.authService.token() == null) {
      return next.handle(req);
    }
    const authHeader = 'Bearer ' + this.authService.token();
    // Clone the request to add the new header.
    //console.log('[AUTH] token: ' + authHeader);
    const authReq = req.clone({headers: req.headers.set('Authorization', authHeader)});
    // const jsonReq = req.clone({headers: req.headers.set('Content-Type', 'application/json')});
    // Pass on the cloned request instead of the original request.
    return next.handle(authReq).pipe(
      catchError(err => {

        if (err instanceof HttpErrorResponse && err.status === 401) {
          console.warn('[AUTH] 401 → token invalido');
          this.authService.lockout();  
          console.log('[AUTH] logout eseguito');
        }
        return throwError(() => err);
      })
    );
  }
}
