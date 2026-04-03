import { Injectable } from '@angular/core';
import { Observable, lastValueFrom, tap } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { WorkEffortMeasure } from '../model/workEffortMeasure';
import { WorkEffortMeasExUom } from '../model/workEffortMeasExUom';
import { HttpErrorResponse, HttpParams } from '@angular/common/http';
import { InfoPage } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';


/**
 *  @author Leonardo Minaudo
 */
@Injectable({
    providedIn: 'root'
})
export class WorkEffortMeasureService {

    constructor(private client: ApiClientService) { }

    getWorkEffortMeasureJoinWorkEffort(value: string, secondaryLang: boolean): Observable<WorkEffortMeasure[]> {
        let params: HttpParams = new HttpParams()
            .set('query', value)
            .set('secondaryLang', secondaryLang);

        return this.client
            .get(`work-effort-measure/work-effort`, params).pipe(
                map(json => json.results as WorkEffortMeasure[])
            );
    }

    getWeMeasureEvalId(workEffortIdFrom: string): Observable<WorkEffortMeasure[]> {
        return this.client
            .get(`work-effort-measure/${workEffortIdFrom}`).pipe(
                map(json => json.results as WorkEffortMeasure[])
            );
    }

    getWorkEffortMeasure(workEffortMeasureId: string): Observable<WorkEffortMeasure> {
        return this.client
            .get(`work-effort-measure/id/${workEffortMeasureId}`).pipe(
                map(json => json as WorkEffortMeasure)
            );
    }

    dropdownWorkEffortMeasure(): Observable<WorkEffortMeasure[]> {
        return this.client
            .get(`work-effort-measure/dropdown-movement`).pipe(
                map(json => json.results as WorkEffortMeasure[])
            );
    }

    dropdownWorkEffortMeasureWEA(): Observable<WorkEffortMeasure[]> {
        return this.client
            .get(`work-effort-measure/dropdown-wea`).pipe(
                map(json => json.results as WorkEffortMeasure[])
            );
    }

    /**
    * Gets the list of work_effort_measure.
    * 
    * @returns Observable<workEffortMeasExUom[]>
    */
    getWorkEffortMeasExUomList(): Observable<WorkEffortMeasExUom[]> {
        return this.client
            .get(`work-effort-measure/`).pipe(
                map(json => json.results as WorkEffortMeasExUom[])
            );
    }

    /**
   * Gets the list of work_effort_measure.
   * 
   * @returns Observable<workEffortMeasExUom[]>
   */
    getWorkEffortMeasExUomListPagination(infoPage: InfoPage): Observable<any> {

        return this.client
            .post(`work-effort-measure/pagination`, infoPage).pipe(
                map(json => json as Number)
            );
    }

        /**
   * Gets the total record of work_effort_measure.
   * 
   * @returns Observable<workEffortMeasExUom[]>
   */
        getWorkEffortMeasExUomListPaginationTotal(infoPage: InfoPage): Observable<any> {

            return this.client
                .post(`work-effort-measure/total`, infoPage).pipe(
                    map(json => json as any)
                );
        }
    


    /**
     * Create a work_effort_measure.
     * 
     * @param WorkEffortMeasure - WorkEffortMeasure
     * @returns Promise<WorkEffortMeasure>
     */
    async createWorkEffortMeasure(WorkEffortMeasure: WorkEffortMeasure): Promise<WorkEffortMeasExUom> {
        const client$ = this.client.post(`work-effort-measure/`, JSON.stringify(WorkEffortMeasure));
        return await lastValueFrom(client$).then(response => response as WorkEffortMeasExUom)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a work_effort_measure.
     * 
     * @param workEffortMeasure - WorkEffortMeasure
     * @returns Promise<WorkEffortMeasure>
     */
    updateWorkEffortMeasure(workEffortMeasure: WorkEffortMeasure): Promise<boolean> {
        const client$ = this.client.put('work-effort-measure/', JSON.stringify(workEffortMeasure));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the work_effort_measure ids passed to it.
     * 
     * @param id - string
     * @returns Promise
     */
    deleteWorkEffortMeasure(id: string): Promise<boolean> {
        const client$ = this.client.delete(`work-effort-measure/${id}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }


}
