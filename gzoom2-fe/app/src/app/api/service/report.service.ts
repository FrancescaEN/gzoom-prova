import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import * as moment from 'moment';
import * as _ from 'lodash';
import { Report, ReportQueryConfig } from 'app/view/report-print/report';
import { ApiClientService } from 'app/commons/service/client.service';
import { QueryConfig } from '../model/queryConfig';


@Injectable()
export class ReportService {

  constructor(private client: ApiClientService) { }

  reports(parentTypeId: string): Observable<Report[]> {
    return this.client.get(`report/${parentTypeId}`).pipe(
      map(json => json.results as Report[]),
      catchError((response: any) => {                          //utilizzo di catchError dentro la pipe
        console.error(`Error get all reports: ${response}`);
        return Promise.reject(response.json() || response);
      })
    )
    // .catch((response: any) => {
    //   console.error(`Error get all reports: ${response}`);
    //   return Promise.reject(response.json() || response);
    // });
  }

  reportsByWorkEffortTypeId(workEffortTypeId: string): Observable<Report[]> {
    return this.client
      .get(`report/workEffortTypeId/${workEffortTypeId}`).pipe(
        map(json => json.results as Report[]),
        catchError((response: any) => {                             //utilizzo di catchError dentro la pipe
          console.error(`Error get all reports: ${response}`);
          return Promise.reject(response.json() || response);
        })
      )
    // .catch((response: any) => {
    //   console.error(`Error get all reports: ${response}`);
    //   return Promise.reject(response.json() || response);
    // });
  }

  report(parentTypeId: string, reportContentId: string, resourceName: string, workEffortTypeId: string): Observable<Report> {
    //console.log('search report with ' + reportContentId + " resourceName " + resourceName );
    return this.client
      .get(`report/${parentTypeId}/${reportContentId}/${resourceName}/${workEffortTypeId}`).pipe(
        map(json => json as Report),
        catchError((response: any) => {                              //utilizzo di catchError dentro la pipe
          console.error(`Error get report`, response);
          return Promise.reject(response.json() || response);
        })
      )
    // .catch((response: any) => {
    //   console.error(`Error get report`,response);
    //   return Promise.reject(response.json() || response);
    // });
  }


  async add(report: Report): Promise<String> {
    // console.log('add ', report);
    try {
      const client$ = this.client.post('report/add', report);
      const response = await lastValueFrom(client$);
      return response;
    } catch (response_1) {
      console.error(`Error report add `, response_1);
      return Promise.reject(response_1.message || response_1);
    }
  }

  async addQueryConfig(report: Report, queryConfig: QueryConfig): Promise<String> {
    let reportQuery: ReportQueryConfig = new ReportQueryConfig(report, queryConfig)
    try {
      const client$ = this.client.post('report/query-config/add', reportQuery);
      const response = await lastValueFrom(client$);
      return response;
    } catch (response_1) {
      console.error(`Error report add `, response_1);
      return Promise.reject(response_1.message || response_1);
    }
  }

  async mail(report: Report): Promise<String> {
    // console.log('mail ', report);
    try {
      const client$ = this.client.post('report/mail', report);
      const response = await lastValueFrom(client$);
      return response;
    } catch (response_1) {
      console.error(`Error report mail: ${response_1}`);
      return Promise.reject(response_1.message || response_1);
    }
  }

  // async add(report: Report):  Promise<String> {
  //   console.log('add ', report);
  //   try {
  //     const response = await this.client
  //       .post('report/add', report)
  //       .toPromise();
  //     return response;
  //   } catch (response_1) {
  //     console.error(`Error report add `,response_1);
  //     return Promise.reject(response_1.message || response_1);
  //   }
  // }

  // async mail(report: Report):  Promise<String> {
  //   console.log('mail ', report);
  //   try {
  //     const response = await this.client
  //       .post('report/mail', report)
  //       .toPromise();
  //     return response;
  //   } catch (response_1) {
  //     console.error(`Error report mail: ${response_1}`);
  //     return Promise.reject(response_1.message || response_1);
  //   }
  // }

  getDate(date: Date) {
    if (date) return moment(date).format('YYYY-MM-DD');
    else return null;
  }
}
