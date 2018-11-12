import { Router } from '@angular/router';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export const LOGIN_URL = '/login';
export const CURENT_USER_TOKEN = 'currentUserToken';
export const CURRENT_USER = 'currentUser';
export const AUTHORIZATION = 'Authorization';

const API_LOGIN_URL = 'login';

@Injectable()
export class AuthenticationService {

  userDetails: any;

  constructor(
    private http: HttpClient,
    private router: Router) {}

  login(username: string, password: string): Observable<any> {
    return new Observable(observer => {
      this.http.post<any>(API_LOGIN_URL, JSON.stringify({username: username, password: password})).subscribe(
        (result: any) => {
          if (result && result.value.token) {
            if (result.value.token) {
              localStorage.setItem(CURENT_USER_TOKEN, result.value.token);
              localStorage.setItem(CURRENT_USER, JSON.stringify(result.value));
            }
            if (result.userDetails) {
              this.userDetails = result.userDetails;
            }
          }
          observer.next(result);
          observer.complete();
        },
        (error) => {
          observer.error(error);
          observer.complete();
       });
    });
  }

  logout(): void {
    localStorage.removeItem(CURENT_USER_TOKEN);
    localStorage.removeItem(CURRENT_USER);
    this.userDetails = null;
    this.router.navigate([LOGIN_URL]);
  }

  isLogged(): boolean {
    return this.userDetails != null || JSON.parse(localStorage.getItem(CURRENT_USER)) != null;
  }

  getUserDetails(): any {
    return JSON.parse(localStorage.getItem(CURRENT_USER)).userDetails;
  }
}
