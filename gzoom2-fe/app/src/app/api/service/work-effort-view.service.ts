import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { WorkEffortView } from '../model/workEffortView';
import { Filter } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';
import { WorkEffortViewEx } from '../model/workEffortViewEx';




@Injectable()
export class WorkEffortViewService {

    constructor(private client: ApiClientService) { }

    getWorkEffortViewEx(orgUnitId: string): Observable<WorkEffortViewEx[]> {
        return this.client
            .get(`work-effort-view/${orgUnitId}`).pipe(
                map(json => json.results as WorkEffortViewEx[])
            );
    }

    /**
     * Gets the list of work_effort_assoc pagination.
     * 
     * @returns Observable<WorkEffortView[]>
     */
    getWorkEffortViewFilter(filter: Filter): Observable<WorkEffortViewEx[]> {

        return this.client
            .post(`work-effort-view/filter`, filter).pipe(
                map(json => json.results as WorkEffortViewEx[])
            );
    }


}
