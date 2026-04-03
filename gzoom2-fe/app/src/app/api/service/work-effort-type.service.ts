import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { WorkEffortType } from '../../view/report-print/report';

@Injectable()
export class WorkEffortTypeService {

  constructor(private client: ApiClientService) { }

  getAllWorkEffortTypes(): Observable<WorkEffortType[]> {
    return this.client
      .get(`work-effort-type/`).pipe(
        map(json => json.results as WorkEffortType[])
      );
  }

  workEffortTypes(workEffortTypeId: string): Observable<WorkEffortType[]> {
    return this.client
      .get(`work-effort-type/like/${workEffortTypeId}`).pipe(
        map(json => json.results as WorkEffortType[])
      );
  }

  workEffortTypesParametric(workEffortTypeId: string): Observable<WorkEffortType[]> {
    return this.client
      .get(`work-effort-type/parametric/${workEffortTypeId}`).pipe(
        map(json => json.results as WorkEffortType[])
      );
  }

  workEffortTypesIsRoot(workEffortTypeIsRoot: string): Observable<WorkEffortType[]> {
    return this.client
      .get(`work-effort-type/isRoot/${workEffortTypeIsRoot}`).pipe(
        map(json => json.results as WorkEffortType[])
      );
  }

  getWorkEffortTypeByParentId(parentTypeId: string): Observable<WorkEffortType[]> {
    return this.client
      .get(`work-effort-type/parent-type-id/${parentTypeId}`).pipe(
        map(json => json.results as WorkEffortType[])
      );
  }

  getLikeWorkEffortTypeId(likeId: string): Observable<WorkEffortType[]> {
    const client$ = this.client.post(`work-effort-type/like`, JSON.stringify(likeId));
    return client$.pipe(map(json => json.results as WorkEffortType[]))

  };
}
