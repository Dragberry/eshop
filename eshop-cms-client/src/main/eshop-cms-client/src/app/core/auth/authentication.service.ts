import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, Subscription} from 'rxjs';

export const LOGIN_URL = '/login';
export const CURENT_USER_TOKEN = 'currentUserToken';
export const CURRENT_USER = 'currentUser';
export const AUTHORISATION = 'Authorisation';

const API_LOGIN_URL = 'api/login';

@Injectable()
export class AuthenticationService {

  userDetails: any;

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    const response = this.http.post<any>(API_LOGIN_URL, JSON.stringify({username: username, password: password}));
    response.subscribe(
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
      });
    return response;
  }

  logout(): void {
    localStorage.removeItem(CURENT_USER_TOKEN);
    localStorage.removeItem(CURRENT_USER);
    this.userDetails = null;
  }

  isLogged(): boolean {
    return this.userDetails != null || JSON.parse(localStorage.getItem(CURRENT_USER)) != null;
  }

  getUserDetails(): any {
    return JSON.parse(localStorage.getItem(CURRENT_USER)).userDetails;
  }
}
