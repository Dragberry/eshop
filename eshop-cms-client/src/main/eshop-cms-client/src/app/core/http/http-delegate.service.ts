import { map } from 'rxjs/operators';
import { HttpClient, HttpParams } from '@angular/common/http';
import { saveAs } from 'file-saver';
import { Result } from '../../shared/model/result';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable()
export class HttpDelegateService {

  constructor(private http: HttpClient) {}

  get<T>(url: string, options?: {
    params: HttpParams
  }): Observable<T> {
    return this.http.get<T>(url, options);
  }

  put<T>(url: string, body: any,  options?: {
    params?: HttpParams,
  }): Promise<T> {
    return new Promise((resolve, reject) => {
      return this.http.put<Result<T>>(url, body, options).subscribe(result => {
        if (!result || result.issues && result.issues.length > 0) {
          console.log(result.issues);
          reject(result.issues);
        }
        resolve(result.value);
      }, error => {
        console.log(error);
        reject(error);
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
