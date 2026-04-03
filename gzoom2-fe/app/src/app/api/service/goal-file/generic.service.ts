import { Injectable } from '@angular/core';
import { ApiClientService } from '../../../commons/service/client.service';
import { HttpParams } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { VarUtils } from '../../../commons/utils/var-utils';

@Injectable({
  providedIn: 'root'
})
export class GenericService {
  constructor(protected client: ApiClientService) {
  }

  /**
   * Creates HTTP request query parameters
   * Can be overridden in the child classes in order to customize parameters
   *
   * @param _ - Object containing the parameters to transform.
   * @returns An HttpParams object.
   */
  protected makeQueryParams(_: any): HttpParams {
    return new HttpParams();
  }

  /**
   * Executes a generic GET request.
   *
   * @param operation - Name of the operation (used for error logging).
   * @param path - API endpoint path
   * @param filter - Optional filter for the request.
   * @param defaultResult - Default result to return in case of error.
   * @returns An Observable containing the result of the request.
   */
  protected get(operation: string,
                path: string,
                filter: any = null,
                defaultResult: any = null): Observable<any> {
    return this.client
      .get(path, VarUtils.isDefined(filter) ? this.makeQueryParams(filter) : filter)
      .pipe(
        map((res) => res),
        catchError(this.handleError(operation, defaultResult))
      );
  }

  /**
   * Executes a generic POST request.
   *
   * @param operation - Name of the operation (used for error logging).
   * @param path - API endpoint path
   * @param body - Body of the request to be sent.
   * @param defaultResult - Default result to return in case of error.
   * @returns An Observable containing the result of the request.
   */
  protected post(operation: string,
                 path: string,
                 body: any,
                 defaultResult: any = null): Observable<any> {
    return this.client.post(path, body).pipe(
      map((res) => res),
      catchError(this.handleError(operation, defaultResult))
    );
  }

  protected put(operation: string,
                path: string,
                body: any = null,
                defaultResult: any = null): Observable<any> {
    return this.client.put(path, body).pipe(
      map(res => res),
      catchError(this.handleError(operation, defaultResult))
    );
  }

  protected delete(operation: string,
                   path: string,
                   defaultResult: any = null): Observable<any> {
    return this.client.delete(path).pipe(
      map(res => res),
      catchError(this.handleError(operation, defaultResult))
    );
  }

  /**
   * Handles HTTP request errors.
   *
   * @param operation - Name of the operation (used for error logging).
   * @param defaultResult - Default result to return in case of error.
   * @returns A function that handles the error and returns an Observable.
   */
  protected handleError<T>(operation: string, defaultResult: any = null) {
    return (error: any): Observable<T> => {
      console.log(`Operation '${operation}' failed with code ${error.status}: ${error.message}`);
      return VarUtils.isDefined(defaultResult) ? of(defaultResult) : throwError(() => new Error(error.error));
    };
  }
}
