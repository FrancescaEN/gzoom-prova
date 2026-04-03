import { Injectable } from '@angular/core';
import { WorkEffortContentType } from 'app/api/model/workEffortContentType';

import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class WorkEffortContentTypeService {

    constructor(private client: ApiClientService) { }

    /**
     * Gets the list of work_effort_content_type.
     * 
     * @returns Observable<WorkEffortContentType[]>
     */
    getWorkEffortContentType(): Observable<WorkEffortContentType[]> {
        return this.client
            .get(`work-effort-content-type/`).pipe(
                map(json => json.results as WorkEffortContentType[])
            );
    }

    /**
     * Create a work_effort_content_type.
     * 
     * @param WorkEffortContentType - WorkEffortContentType
     * @returns Promise<WorkEffortContentType>
     */
    async createWorkEffortContentType(WorkEffortContentType: WorkEffortContentType): Promise<WorkEffortContentType> {
        const client$ = this.client.post(`work-effort-content-type/`, JSON.stringify(WorkEffortContentType));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a work_effort_content_type.
     * 
     * @param WorkEffortContentType - WorkEffortContentType
     * @returns Promise<WorkEffortContentType>
     */
    updateWorkEffortContentType(WorkEffortContentType: WorkEffortContentType): Promise<WorkEffortContentType> {
        const client$ = this.client.put('work-effort-content-type/', JSON.stringify(WorkEffortContentType));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the work_effort_content_type ids passed to it.
     * 
     * @param id - string[]
     * @returns Promise
     */
    deleteWorkEffortContentType(id: string[]): Promise<any> {
        const client$ = this.client.delete(`work-effort-content-type/${id}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }

    getContentTypeList(workEffortId: string): Observable<WorkEffortContentType[]> {
        return this.client
            .get(`work-effort-content-type/${workEffortId}`).pipe(
                map(json => json.results as WorkEffortContentType[])
            );
    }

}
