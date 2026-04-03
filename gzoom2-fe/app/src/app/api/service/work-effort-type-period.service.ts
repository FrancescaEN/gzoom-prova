import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from '../../commons/service/client.service';
import { WorkEffortTypePeriod } from '../model/workEffortTypePeriod';

@Injectable()
export class WorkEffortTypePeriodService {

  constructor(private client: ApiClientService) { }

  getWorkEffortTypePeriodByWorkEffortTypeId(workEffortTypeId: string): Observable<WorkEffortTypePeriod[]> {
    return this.client
      .get(`work-effort-type-period/${workEffortTypeId}`).pipe(
        map(json => json as WorkEffortTypePeriod[])
      );
  }

}
