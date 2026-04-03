import { Injectable } from '@angular/core';
import { ApiClientService } from '../../commons/service/client.service';

import { lastValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { QueryConfig } from '../model/queryConfig';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from 'app/commons/service/auth.service';

@Injectable()
export class QueryConfigService {

  private static readonly CSVmime = "text/csv";
  private static readonly PDFmime = "application/pdf";
  private static readonly XLSXmime = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  constructor(private readonly client: ApiClientService,
    private http: HttpClient,
    private readonly authService: AuthService) {
  }


  queryConfigs(parentTypeId: string, queryType: string): Observable<QueryConfig[]> {
    if (parentTypeId && queryType) {
      return this.client
        .get(`query-config/all/${parentTypeId}/${queryType}`).pipe(
          map(json => json.results as QueryConfig[])
        );
    } else {
      return this.client
        .get('query-config/all').pipe(
          map(json => json.results as QueryConfig[])
        );
    }
  }

  getQueryConfig(id: string): Observable<QueryConfig> {
    return this.client
      .get(`query-config/id/${id}`)
      .pipe(map(json => json as QueryConfig));
  }

  getQueryConfigList(queryType?: string): Observable<QueryConfig[]> {
    let url: string = (queryType) ? `query-config/query-type/${queryType}` : `query-config/`;

    return this.client
      .get(url)
      .pipe(map(json => json.results as QueryConfig[]));
  }

  executeQuery(query: QueryConfig) {
    query.queryInfo = "";

    const client$ = this.client.postBlob(`query-config/exec`, query);
    return lastValueFrom(client$).then(response => {
      this.downLoadFile(query, response)
    })
      .catch(async (error) => {
        const _error = JSON.parse(await error.error.text()).message;
        console.error(`Error while deleting in:`, _error);
        return Promise.reject(_error);
      });

    // return this.client
    // .postBlob(`query-config/exec`, query)
    // .toPromise()
    // .then(response => this.downLoadFile(query, response, query.exportMimeType))
    // .catch(async (error) => {
    //   const _error = JSON.parse(await error.error.text()).message;
    //   console.error(`Error while deleting in:`, _error);
    //   return Promise.reject(_error);
    // });
  }

  updateExecQuery(query: QueryConfig): Promise<QueryConfig> {
    query.queryInfo = "";

    const client$ = this.client.post(`query-config/exec`, query);
    return lastValueFrom(client$).then(response => response)
      .catch((error: HttpErrorResponse) => {
        const _error = error.error.message;
        console.error(`Error while exec query: ${_error}`);
        return Promise.reject(_error);
      });

    // return this.client
    // .post(`query-config/exec`, query)
    // .toPromise()
    // .then(response => response)
    // .catch((error: HttpErrorResponse ) => {
    //   const _error = error.error.message;
    //   console.error(`Error while exec query: ${_error}`);
    //   return Promise.reject(_error);
    // });
  }

  downLoadFile(query: QueryConfig, data: any) {

    let blob = new Blob([data], { type: data.type });
    let url = window.URL.createObjectURL(blob);
    let anchor = document.createElement('a');
    if (data.type == QueryConfigService.PDFmime)
      anchor.download = 'export_' + query.queryName + '.pdf';
    else if (data.type == QueryConfigService.XLSXmime)
      anchor.download = 'export_' + query.queryName + '.xlsx';
    else if (data.type == QueryConfigService.CSVmime)
      anchor.download = 'export_' + query.queryName + '.csv';
    anchor.href = url;
    anchor.click();
  }

  updateQueryConfigInfoBase(queryConfig: QueryConfig): Promise<QueryConfig> {
    const client$ = this.client.put('query-config/update/info-base/', JSON.stringify(queryConfig));
    return lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while updating in: ${response.error.message}`);
        return Promise.reject(response.error.message)

      })

  };

  updateQueryConfig(queryConfig: QueryConfig): Promise<QueryConfig> {
    queryConfig.queryInfo = this.toBase64(queryConfig.queryInfo);
    const client$ = this.client.put('query-config/update/', JSON.stringify(queryConfig));
    return lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while updating in: ${response.error.message}`);
        return Promise.reject(response.error.message)

      })

  };

  updateConditions(queryConfig: QueryConfig): Promise<QueryConfig> {
    queryConfig.queryInfo = this.toBase64(queryConfig.queryInfo);
    const client$ = this.client.put('query-config/update/conditions/', JSON.stringify(queryConfig));
    return lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while updating in: ${response.error.message}`);
        return Promise.reject(response.error.message)

      })

  };

  async createQueryConfig(QueryConfig: QueryConfig): Promise<string> {
    QueryConfig.queryInfo = this.toBase64(QueryConfig.queryInfo);
    const client$ = this.client.post(`query-config/create`, JSON.stringify(QueryConfig));
    return await lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while creating in: ${response.error.message}`);
        return Promise.reject(response.error.message);
      });
  };

  deleteQueryConfig(queryId: string): Promise<any> {
    const client$ = this.client.delete(`query-config/delete/${queryId}`);
    return lastValueFrom(client$).then(response => response)
      .catch((error: any) => {
        console.error(`Error while deleting in: ${error.error.message}`);
        return Promise.reject(error.error);
      });

  }

  toBase64(str: string) {
    const utf8Bytes = new TextEncoder().encode(str); // UTF-8 bytes
    let binary = '';
    utf8Bytes.forEach(byte => binary += String.fromCharCode(byte));
    return btoa(binary);
  }

}
