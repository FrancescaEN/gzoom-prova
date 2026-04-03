import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { WorkEffortMeasRatScExUomRatingScale } from '../model/workEffortMeasRatScExUomRatingScale';
import { WorkEffortMeasRatSc } from '../model/workEffortMeasRatSc';
import { HttpErrorResponse } from '@angular/common/http';


/**
 *  @author Leonardo Minaudo
 */
@Injectable()
export class WorkEffortMeasRatScService {

    constructor(private client: ApiClientService) { }

    getRatingScaleWEM(workEffortMeasureId: string): Observable<WorkEffortMeasRatScExUomRatingScale[]> {
        return this.client
            .get(`work-effort-meas-rat-sc/${workEffortMeasureId}`).pipe(
                map(json => json.results as WorkEffortMeasRatScExUomRatingScale[])
            );
    }


    async createWorkEffortMeasRatSc(WorkEffortMeasRatSc: WorkEffortMeasRatSc): Promise<WorkEffortMeasRatSc> {
        const client$ = this.client.post(`work-effort-meas-rat-sc/`, JSON.stringify(WorkEffortMeasRatSc));
        return await lastValueFrom(client$).then(response => response as WorkEffortMeasRatSc)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };


    updateWorkEffortMeasRatSc(workEffortMeasure: WorkEffortMeasRatSc): Promise<WorkEffortMeasRatSc> {
        const client$ = this.client.put('work-effort-meas-rat-sc/', JSON.stringify(workEffortMeasure));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };


    deleteWorkEffortMeasRatSc(workEffortMeasureId: string, uomId: string, uomRatingValue: number): Promise<any> {
        const client$ = this.client.delete(`work-effort-meas-rat-sc/${workEffortMeasureId}/${uomId}/${uomRatingValue}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }


}
