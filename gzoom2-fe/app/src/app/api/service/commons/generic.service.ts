import {Injectable} from '@angular/core';
import {ApiClientService} from "../../../commons/service/client.service";
import {HttpParams, HttpStatusCode} from "@angular/common/http";
import {Observable, map, of, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
import {isDefined} from "../../../commons/model/commons";

@Injectable({
  providedIn: 'root'
})
export class GenericService {
  constructor(protected client: ApiClientService) {}

  /**
   * Creates HTTP request parameters
   *
   * @param _ - Object containing parameters.
   * @returns HttpParams object.
   */
  protected makeParams(_: any): HttpParams {
    return new HttpParams();
  }

  /**
   * Executes GET request
   *
   * @param operation - Operation name (used for logging).
   * @param path - API endpoint path.
   * @param params - Object containing request parameters.
   * @param defaultResult - Default result to return in case of errors.
   * @returns An Observable object containing request result.
   */
  protected get(operation: string,
                path: string,
                params: any = null,
                defaultResult: any = null): Observable<any> {
    return this.client
      .get(path, params !== null ? this.makeParams(params) : params)
      .pipe(
        map((res) => res),
        catchError(this.handleError(operation, defaultResult)),
      );
  }

  // todo merge with get
  protected getFile(operation: string,
                path: string,
                params: any = null,
                defaultResult: any = null,
                responseType: 'json' | 'blob' = 'json'): Observable<any> {
    return this.client
      // .getFile(path, params !== null ? this.makeParams(params) : params, responseType)
      .getFile(path, params !== null ? this.makeParams(params) : params)
      .pipe(
        map(res => res),
        catchError(this.handleError(operation, defaultResult)),
      );
  }

  /**
   * Executes POST request
   *
   * @param operation - Operation name (used for logging).
   * @param path - API endpoint path.
   * @param body - Request body.
   * @param defaultResult - Default result to return in case of errors.
   * @returns An Observable object containing request result.
   */
  protected post(operation: string,
                 path: string,
                 body: any,
                 defaultResult: any = null): Observable<any> {
    return this.client.post(path, body).pipe(
      map((res) => res),
      catchError(this.handleError(operation, defaultResult)),
    );
  }

  /**
   * Executes POST request sending a form todo merge with post
   *
   * @param operation - Operation name (used for logging).
   * @param path - API endpoint path.
   * @param form - Form data
   * @param defaultResult - Default result to return in case of errors.
   * @returns An Observable object containing request result.
   */
  protected postForm(operation: string,
                     path: string,
                     form: FormData,
                     defaultResult: any = null): Observable<any> {
    return this.client.postForm(path, form).pipe(
      map((res) => res),
      catchError(this.handleError(operation, defaultResult)),
    );
  }

  /**
   * Executes PUT request
   *
   * @param operation - Operation name (used for logging).
   * @param path - API endpoint path.
   * @param body - Request body.
   * @param defaultResult - Default result to return in case of errors.
   * @returns An Observable object containing request result.
   */
  protected put(operation: string,
                path: string,
                body: any = null,
                defaultResult: any = null): Observable<any> {
    return this.client.put(path, body).pipe(
      map(res => res),
      catchError(this.handleError(operation, defaultResult))
    );
  }

  /**
   * Executes DELETE request
   *
   * @param operation - Operation name (used for logging).
   * @param path - API endpoint path.
   * @param defaultResult - Default result to return in case of errors.
   * @returns An Observable object containing request result.
   */
  protected delete(operation: string,
                   path: string,
                   defaultResult: any = null): Observable<any> {
    return this.client.delete(path).pipe(
      map(res => res),
      catchError(this.handleError(operation, defaultResult))
    );
  }

  /**
   * HTTP errors handler
   *
   * @param operation - Operation name (used for logging).
   * @param defaultResult - Default result to return in case of errors.
   * @returns A function that handles the error and returns an Observable.
   */
  protected handleError<T>(operation: string, defaultResult: any = null) {
    return (error: any): Observable<T> => {
      switch (error.status) {
        case HttpStatusCode.NotFound:
          console.warn(`Errore 404: Risorsa non trovata per l'operazione "${operation}".`);
          break;

        case HttpStatusCode.InternalServerError:
          console.error(`Errore 500: Errore interno del server per l'operazione "${operation}".`);
          return throwError(
            () => new Error("Errore interno del server. Riprova più tardi."),
          );

        default:
          console.log(`${operation} failed: ${error.message}`);
          break;
      }

      // console.log('handleError error:')
      // console.log(error);
      // console.log('handleError error.error:')
      // console.log(error.error);
      // console.log('handleError error.error?.message:')
      // console.log(error.error?.message);
      // console.log('handleError error.error?.error:')
      // console.log(error.error?.error);
      // console.log('handleError error.error?.params:')
      // console.log(error.error?.params);
      return isDefined(defaultResult) ? of(defaultResult)
        : (isDefined(error.error?.message) ? of(error.error.message)
          : (isDefined(error?.error?.error) ? of(error.error.error)
            : (isDefined(error?.error?.params) ? of(error.error.params)
              : throwError(() => new Error(error.statusText))
            )
          )
        );
    };
  }
}
