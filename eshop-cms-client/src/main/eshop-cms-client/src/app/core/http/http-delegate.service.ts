import { AuthenticationService } from './../auth/authentication.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { saveAs } from 'file-saver';
import { Result } from 'src/app/shared/model';
import { Injectable } from '@angular/core';
import { MessageService } from '../service/message.service';

@Injectable()
export class HttpDelegateService {

  constructor(
    private authService: AuthenticationService,
    private http: HttpClient,
    private messageService: MessageService) {}

  get<T>(url: string, options?: {
    params: HttpParams
  }): Promise<T> {
    return new Promise((resolve, reject) => {
      return this.http.get<T>(url, options).subscribe(result => {
        resolve(result);
      }, error => {
        if (error.status === 401) {
          this.authService.logout();
        } else {
          this.messageService.showError(error);
          reject(error);
        }
      });
    });
  }

  post<T>(url: string, body: any,  options?: {
    params?: HttpParams,
  }): Promise<T> {
    return new Promise((resolve, reject) => {
      return this.http.post<Result<T>>(url, body, options).subscribe(result => {
        if (!result || result.issues && result.issues.length > 0) {
          this.messageService.showIssues(result.issues);
          reject(result.issues);
        }
        resolve(result.value);
      }, error => {
        if (error.status === 401) {
          this.authService.logout();
        } else {
          this.messageService.showError(error);
          reject(error);
        }
      });
    });
  }

  put<T>(url: string, body: any,  options?: {
    params?: HttpParams,
  }): Promise<T> {
    return new Promise((resolve, reject) => {
      return this.http.put<Result<T>>(url, body, options).subscribe(result => {
        if (!result || result.issues && result.issues.length > 0) {
          this.messageService.showIssues(result.issues);
          reject(result.issues);
        }
        resolve(result.value);
      }, error => {
        if (error.status === 401) {
          this.authService.logout();
        } else {
          this.messageService.showError(error);
          reject(error);
        }
      });
    });
  }

  downloadFile(url: string): void {
    this.http.get(url, {
      observe: 'response',
      responseType: 'blob'
    }).subscribe(response => {
      const regex = /filename="(.*?)"/;
      const contentDisposition: string = response.headers.get('Content-Disposition');
      saveAs(response.body, contentDisposition.match(regex)[1]);
    });
  }
}
