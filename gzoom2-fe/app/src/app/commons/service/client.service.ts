
import { Observable, throwError as observableThrowError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { Injectable, Optional } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { ApiConfig } from '../model/api-config';
import { behead, untail } from '../model/commons';
import { AuthService } from './auth.service';
import { LockoutService } from './lockout.service';

/**
 * A client that performs athenticated requests and exchanges JSON data.
 */
@Injectable()
export class ApiClientService {

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService,
    private lockout: LockoutService,
    private apiConfig: ApiConfig) { }

  /**
   * Performs an HTTP GET call.
   *
   * @param  {string}             path    The path relative to ApiConfig.rootPath
   * @param  {RequestOptionsArgs} options Additional options
   * @return {Observable<any>}            An Observable of the outcome
   */
  get(path: string, params?: HttpParams): Observable<any> {
    const url = this.makeUrl(path);
    const headers = this.makeOptions();

    return this.http
      .get(url, { headers: headers, params: params })
      .pipe(
        catchError(this.onAuthError(this)) // then handle the error
      );
  }

  // todo merge with get
  // getFile(path: string, params?: HttpParams, responseType: 'json' | 'blob' = 'json'): Observable<any> {
  getFile(path: string, params?: HttpParams/*, responseType: 'json' | 'blob' = 'json'*/): Observable<any> {
    const url = this.makeUrl(path);

    return this.http.get(url, {
      params,
      responseType: 'blob',
      observe: 'response' // So you can access headers like Content-Disposition
    });
  }

  /**
 * Performs an HTTP POST call with return blob file.
 *
 * @param  {string}             path    The path relative to ApiConfig.rootPath
 * @param  {RequestOptionsArgs} options Additional options
 * @return {Observable<any>}            An Observable of the outcome
 */
  postBlob(path: string, body?: any): Observable<any> {
    const url = this.makeUrl(path);
    const msg = body ? (typeof body === 'string') ? body : JSON.stringify(body) : undefined;
    return this.http
      .post(url, body, { headers: new HttpHeaders().set('Content-Type', 'application/json'), responseType: 'blob' })
      .pipe(
        catchError(this.onAuthError(this)) // then handle the error
      );
  }


  postFormData(path: string, body?: FormData): Observable<any> {
    const url = this.makeUrl(path);
    return this.http
      .post(url, body)
      .pipe(
        catchError(this.onAuthError(this)) // then handle the error
      );
  }

  /**
   * Performs an HTTP POST call.
   *
   * @param  {string}             path    The path relative to ApiConfig.rootPath
   * @param  {any}                body    Any value that will be converted to a JSON string and
   *                                      sent as the HTTP message body. Optional, if nothing is
   *                                      specified then no body is sent at all.
   * @return {Observable<any>}            An Observable of the outcome
   */
  post(path: string, body?: any, params?: any): Observable<any> {
    const url = this.makeUrl(path);
    const opts = this.makeOptions(true);
    const msg = body ? (typeof body === 'string') ? body : JSON.stringify(body) : undefined;
    // TODO opts
    return this.http
      .post(url, msg, {
        headers: new HttpHeaders().set('Content-Type', 'application/json').set('Cache-Control', 'no-cache'),
        params
      })
      .pipe(catchError(this.onAuthError(this)));
  }

  // todo merge with post
  /**
   * Performs an HTTP POST call sending FormData multipart/form-data
   *
   * @param  {string}           path      The path relative to ApiConfig.rootPath
   * @param  {FormData}         formData  Form data.
   * @param                     params
   * @return {Observable<any>}            An Observable of the outcome
   */
  postForm(path: string, formData: FormData, params?: any): Observable<any> {
    const url = this.makeUrl(path);
    const opts = this.makeOptions(true);
    // TODO opts
    return this.http
      .post(url, formData, {
        // headers: new HttpHeaders().set('Content-Type', 'multipart/form-data').set('Cache-Control', 'no-cache'),
        headers: new HttpHeaders().set('Cache-Control', 'no-cache'),
        params
      })
      .pipe(catchError(this.onAuthError(this)));
  }

  put(path: string, body?: any): Observable<any> {
    const url = this.makeUrl(path);
    const opts = this.makeOptions(true);
    const msg = body ? (typeof body === 'string') ? body : JSON.stringify(body) : undefined;
    // TODO opts
    return this.http
      .put(url, msg, {
        headers: new HttpHeaders().set('Content-Type', 'application/json').set('Cache-Control', 'no-cache'),
      })
      .pipe(catchError(this.onAuthError(this)));
  }

  delete(path: string): Observable<any> {
    const url = this.makeUrl(path);
    const opts = this.makeOptions(true);
    // TODO opts
    return this.http
      .delete(url, {
        headers: new HttpHeaders().set('Content-Type', 'application/json').set('Cache-Control', 'no-cache'),
      })
      .pipe(
        catchError(this.onAuthError(this)));
  }

  /**
 *
 * @param path of rest service
 * @returns complete rest url
 */
  makeUrl(path: string): string {
    const rootUrl = untail(this.apiConfig.rootPath, '/');
    const relUrl = behead(path, '/');
    return rootUrl + '/' + relUrl;
  }

  private makeOptions(hasBody = false): HttpHeaders {
    let headers = new HttpHeaders();
    if (hasBody) {
      headers.set('Content-Type', 'application/json').set('Cache-Control', 'no-cache');
    }
    return headers;
  }

  /**
   * Sets the JSON content type.
   *
   * @param  {HttpHeaders} headers The headers
   */
  private addJsonHeader(headers: HttpHeaders) {
    headers.set('Content-Type', 'application/json');
    // headers.set('Accept', 'application/json');
  }

  /**
   * Manages the authentication issues due to an attempt to call a remote service
   * from an unauthorized (not logged in) user.
   *
   * @param  {ApiClientService} self This service
   * @return {(err: any, caught: Observable<T>) => ObservableInput<R>}
   */
  private onAuthError(self: ApiClientService) {
    return (res: Response) => {
      // 401: unauthorized (read not authenticated)
      // 403: forbidden (read not authorized to do that operation)
      if (res.status === 401) {
        // if not authenticated
        console.error(`Unauthorized request to ${res.url}, locking user out!`);
        self.lockout.lockout(this.router.url);
      }
      return observableThrowError(() => res);
    };
  }
}
