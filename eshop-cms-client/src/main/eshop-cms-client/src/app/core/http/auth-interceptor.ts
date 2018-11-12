import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService, CURENT_USER_TOKEN, AUTHORIZATION } from '../auth/authentication.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private authService: AuthenticationService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>  {
        if (this.authService.isLogged()) {
            const request = req.clone({
                setHeaders: {
                    AUTHORIZATION: `Bearer ${localStorage.getItem(CURENT_USER_TOKEN)}`
                }
            });
            return next.handle(request);
        }
        return next.handle(req);
    }
}
