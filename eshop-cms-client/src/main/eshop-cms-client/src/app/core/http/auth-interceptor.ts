import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService, CURENT_USER_TOKEN, AUTHORISATION } from '../auth/authentication.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private authService: AuthenticationService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>  {
        if (this.authService.isLogged()) {
            const authReq = req.clone({
                headers: req.headers.set(AUTHORISATION, `Bearer ${localStorage.getItem(CURENT_USER_TOKEN)}`)
            });
            next.handle(authReq);
        }
        return next.handle(req);
    }
}
