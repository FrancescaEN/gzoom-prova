import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { WorkEffort } from '../model/work-effort';
import { HttpErrorResponse } from '@angular/common/http';
import { WorkEffortEx } from '../model/workEffortEx';

@Injectable()
export class WorkEffortService {

  constructor(private client: ApiClientService) { }

  getWorkEffortDropdown(orgUnitId: string): Observable<WorkEffortEx[]> {
          return this.client
              .get(`work-effort/work-effort-dropdown/${orgUnitId}`).pipe(
                  map(json => json.results as WorkEffortEx[])
              );
  }

  workEfforts(parentTypeId: string, workEffortTypeId: string, useFilter?: boolean): Observable<WorkEffort[]> {
    console.log('search workEffort list with workEffortTypeId=' + workEffortTypeId);
    if (useFilter == undefined)
      useFilter = true;
    return this.client
      .get(`work-effort/${parentTypeId}/${workEffortTypeId}/${useFilter}`).pipe(
        map(json => json.results as WorkEffort[])
      );
  }

  workEffortParents(workEffortParentId: string): Observable<WorkEffort[]> {
    console.log('search workEffort list with workEffortParentId=' + workEffortParentId);
    return this.client
      .get(`work-effort/work-effort-parent/${workEffortParentId}`).pipe(
        map(json => json.results as WorkEffort[])
      );
  }

  workEffortsIsRootIsTemplate(): Observable<WorkEffort[]> {
    console.log('search workEffort list with isRoot true and isTemplate true');
    return this.client
      .get(`work-effort/work-effort-isRoot-isTemplate`).pipe(
        map(json => json.results as WorkEffort[])
      );
  }

  getWorkEffort(id: string): Observable<WorkEffort> {
    return this.client
      .get(`work-effort/${id}`)
      .pipe(map(json => json as WorkEffort));
  }

  getWorkEffortEx(id: string): Observable<WorkEffortEx> {
    return this.client
      .get(`work-effort/work-effort-ex/${id}`)
      .pipe(map(json => json as WorkEffortEx));
  }

  workEffortEx(): Observable<WorkEffortEx[]> {
    console.log('search workEffortEx list ');
    return this.client
      .get(`work-effort/work-effort-ex`).pipe(
        map(json => json.results as WorkEffortEx[])
      );
  }

  workEffortExFilter(infoCurrentPage): Observable<WorkEffortEx[]> {
    console.log('search workEffortEx list ');
    return this.client
      .post(`work-effort/work-effort-ex/pagination`,infoCurrentPage).pipe(
        map(json => json.results as WorkEffortEx[])
      );
  }

  WorkEffortByOrgId(): Observable<WorkEffort[]> {
    console.log('search workEffort list ');
    return this.client
      .get(`work-effort/work-effort-by-org-id`).pipe(
        map(json => json.results as WorkEffort[])
      );
  }

  async createWorkEffort(workEffort: WorkEffort): Promise<WorkEffort> {
    const client$ = this.client.post(`work-effort`, JSON.stringify(workEffort));
    return await lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while creating in: ${response.error.message}`);
        return Promise.reject(response.error.message);
      });
  };

  updateWorkEffort(workEffort: WorkEffort): Promise<WorkEffort> {
    const client$ = this.client.put('work-effort', JSON.stringify(workEffort));
    return lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while updating in: ${response.error.message}`);
        return Promise.reject(response.error.message)

      })

  };

  deleteWorkEffort(workEffortId: string): Promise<any> {
    const client$ = this.client.delete(`work-effort/${workEffortId}`);
    return lastValueFrom(client$).then(response => response)
      .catch((error: any) => {
        console.error(`Error while deleting in: ${error.error.message}`);
        return Promise.reject(error.error);
      });
  }

  deleteWorkEffortTree(workEffortId: string): Promise<any> {
    const client$ = this.client.delete(`work-effort/deleteWorkEffortTree/${workEffortId}`);
    return lastValueFrom(client$).then(response => response)
      .catch((error: any) => {
        console.error(`Error while deleting in: ${error.error.message}`);
        return Promise.reject(error.error);
      });
  }
}
