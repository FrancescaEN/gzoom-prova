import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { WorkEffortMeasureRatScView } from '../model/workEffortMeasureRatScView';



/**
 *  @author Leonardo Minaudo
 */
@Injectable()
export class WorkEffortMeasureRatScViewService {

    constructor(private client: ApiClientService) { }

    getWorkEffortMeasureRatScView(workEffortMeasureId: string): Observable<WorkEffortMeasureRatScView[]> {
        return this.client
            .get(`work-effort-measure-rat-sc-view/${workEffortMeasureId}`).pipe(
                map(json => json.results as WorkEffortMeasureRatScView[])
            );
    }




}