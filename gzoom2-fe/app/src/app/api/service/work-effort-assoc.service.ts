import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { WorkEffortAssoc } from '../model/workEffortAssoc';
import { HttpErrorResponse } from '@angular/common/http';
import { WorkEffortAssocEx } from '../model/workEffortAssocEx';
import { InfoPage } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';




@Injectable()
export class WorkEffortAssocService {

    constructor(private client: ApiClientService) { }



    /**
     * Gets the list of work_effort_assoc.
     * 
     * @returns Observable<WorkEffortAssocEx[]>
     */
    getWorkEffortAssoc(): Observable<WorkEffortAssocEx[]> {
        return this.client
            .get(`work-effort-assoc/`).pipe(
                map(json => json.results as WorkEffortAssocEx[])
            );
    }

    /**
     * Gets the list of work_effort_assoc pagination.
     * 
     * @returns Observable<WorkEffortAssocEx[]>
     */
    getWorkEffortAssocPagination(infoPage: InfoPage): Observable<any> {        
        return this.client
            .post(`work-effort-assoc/pagination`, infoPage).pipe(
                map(json => json as WorkEffortAssocEx[])
            );
    }

    /**
     * Gets the list of total.
     * 
     * @returns Observable<int>
     */
    getWorkEffortAssocPaginationTotal(infoPage: InfoPage): Observable<any> {        
        return this.client
            .post(`work-effort-assoc/total`, infoPage).pipe(
                map(json => json as Number)
            );
    }

    /**
     * Create a work_effort_assoc.
     * 
     * @param workEffortAssoc - WorkEffortAssoc
     * @returns Promise<WorkEffortAssoc>
     */
    async createWorkEffortAssoc(workEffortAssoc: WorkEffortAssoc): Promise<WorkEffortAssoc> {
        const client$ = this.client.post(`work-effort-assoc/`, JSON.stringify(workEffortAssoc));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a work_effort_assoc.
     * 
     * @param workEffortAssoc - WorkEffortAssoc
     * @returns Promise<WorkEffortAssoc>
     */
    updateWorkEffortAssoc(workEffortAssoc: WorkEffortAssoc): Promise<WorkEffortAssoc> {
        const client$ = this.client.put('work-effort-assoc/', JSON.stringify(workEffortAssoc));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the work_effort_assoc.
     * 
     * @param workEffortIdFrom
     * @param workEffortIdTo
     * @param workEffortAssocTypeId
     * @param fromDate
     * @returns Promise
     */
    deleteWorkEffortAssoc(workEffortAssoc: WorkEffortAssoc): Promise<any> {
        const client$ = this.client.post(`work-effort-assoc/delete/`, JSON.stringify(workEffortAssoc));
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }

}
