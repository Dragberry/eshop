import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { AuthenticationService, CURRENT_USER, LOGIN_URL } from './authentication.service';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(
        private authService: AuthenticationService,
        private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        const user: any = JSON.parse(localStorage.getItem(CURRENT_USER));
        if (!user) {
            this.router.navigate([LOGIN_URL], { queryParams: { returnUrl: state.url } });
            return false;
        }
        return true;
    }
}
