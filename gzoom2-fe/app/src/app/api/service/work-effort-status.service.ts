import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from '../../commons/service/client.service';
import { HttpErrorResponse } from '@angular/common/http';
import { WorkEffortStatus } from '../model/workEffortStatus';
import { WorkEffortStatusEx } from '../model/workEffortStatusEx';

@Injectable()
export class WorkEffortStatusService {

  constructor(private client: ApiClientService) { }


  // getWorkEffortStatus(workEffortId: string): Observable<WorkEffortStatus[]> {
  //   return this.client
  //     .get(`work-effort-status/${workEffortId}`)
  //     .pipe(map(json => json as WorkEffortStatus[]));
  // }

  
  // getWorkEffortStatusList(): Observable<WorkEffortStatus[]> {
  //   console.log('search workEffortStatus list ');
  //   return this.client
  //     .get(`work-effort-status/`).pipe(
  //       map(json => json.results as WorkEffortStatus[])
  //     );
  // }

  // getWorkEffortStatusExList(): Observable<WorkEffortStatusEx[]> {
  //   console.log('search workEffortStatus list ');
  //   return this.client
  //     .get(`work-effort-status-ex/`).pipe(
  //       map(json => json.results as WorkEffortStatusEx[])
  //     );
  // }

  getWorkEffortStatusEx(workEffortId: string): Observable<WorkEffortStatusEx[]> {
    return this.client
      .get(`work-effort-status/work-effort-status-ex/${workEffortId}`)
      .pipe(map(json => json.results as WorkEffortStatusEx[]));
  }

  async createWorkEffortStatus(workEffortStatus: WorkEffortStatus): Promise<WorkEffortStatus> {
    const client$ = this.client.post(`work-effort-status`, JSON.stringify(workEffortStatus));
    return await lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while creating in: ${response.error.message}`);
        return Promise.reject(response.error.message);
      });
  };

  updateWorkEffortStatus(workEffortStatus: WorkEffortStatus): Promise<WorkEffortStatus> {
    const client$ = this.client.put('work-effort-status', JSON.stringify(workEffortStatus));
    return lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while updating in: ${response.error.message}`);
        return Promise.reject(response.error.message)

      })

  };


  /**
     * Delete the work_effort_status.
     * 
     * @param workEffortId
     * @param statusId
     * @param statusDatetime
     * @returns Promise
     */
    deleteWorkEffortStatus(workEffortStatus: WorkEffortStatus): Promise<any> {
        const client$ = this.client.post(`work-effort-status/delete/`, JSON.stringify(workEffortStatus));
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }
}
