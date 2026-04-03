import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { WorkEffortPurposeType } from '../model/workEffortPurposeType';
import { HttpErrorResponse } from '@angular/common/http';
import { WorkEffortPurposeTypeEx } from '../model/workEffortPurposeTypeEx';

/**
 *  @author Leonardo Minaudo
 */
@Injectable()
export class WorkEffortPurposeTypeService {

    constructor(private client: ApiClientService) { }


    /**
     * Gets the list of work_effort_purpose_type.
     * 
     * @returns Observable<WorkEffortPurposeType[]>
     */
    getWorkEffortPurposeType(): Observable<WorkEffortPurposeType[]> {
        return this.client
            .get(`work-effort-purpose-type/`).pipe(
                map(json => json.results as WorkEffortPurposeType[])
            );
    }

    /**
 * Gets the list of work_effort_purpose_type by purpose-type-enum-id.
 * 
 * @returns Observable<WorkEffortPurposeType[]>
 */
    getWorkEffortPurposeTypeByPurposeTypeEnumId(purposeTypeEnumId: string): Observable<WorkEffortPurposeType[]> {
        return this.client
            .get(`work-effort-purpose-type/${purposeTypeEnumId}`).pipe(
                map(json => json.results as WorkEffortPurposeType[])
            );
    }

    getPurposeTabType(glAccountId: string, exists: boolean): Observable<WorkEffortPurposeTypeEx[]> {
        return this.client
            .get(`work-effort-purpose-type/purpose-tab-type/${glAccountId}/${exists}`).pipe(
                map(json => json.results as WorkEffortPurposeTypeEx[])
            );
    }

    /**
     * Create a work_effort_purpose_type.
     * 
     * @param WorkEffortPurposeType - WorkEffortPurposeType
     * @returns Promise<WorkEffortPurposeType>
     */
    async createWorkEffortPurposeType(WorkEffortPurposeType: WorkEffortPurposeType): Promise<WorkEffortPurposeType> {
        const client$ = this.client.post(`work-effort-purpose-type/`, JSON.stringify(WorkEffortPurposeType));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a work_effort_purpose_type.
     * 
     * @param WorkEffortPurposeType - WorkEffortPurposeType
     * @returns Promise<WorkEffortPurposeType>
     */
    updateWorkEffortPurposeType(WorkEffortPurposeType: WorkEffortPurposeType): Promise<WorkEffortPurposeType> {
        const client$ = this.client.put('work-effort-purpose-type/', JSON.stringify(WorkEffortPurposeType));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the work_effort_purpose_type ids passed to it.
     * 
     * @param id - string[]
     * @returns Promise
     */
    deleteWorkEffortPurposeType(id: string[]): Promise<any> {
        const client$ = this.client.delete(`work-effort-purpose-type/${id}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }


}
