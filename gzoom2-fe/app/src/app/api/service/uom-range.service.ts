import { Injectable } from '@angular/core';

import { lastValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { UomRange } from '../model/uomRange';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class UomRangeService {
    constructor(private client: ApiClientService) { }

    getUomRangeList(): Observable<UomRange[]> {
        return this.client
            .get(`uom-range`).pipe(
                map(json => json.results as UomRange[])
            );
    }

    /**
     * Create a uom_range.
     * 
     * @param UomRange - UomRange
     * @returns Promise<UomRange>
     */
    async createUomRange(UomRange: UomRange): Promise<UomRange> {
        const client$ = this.client.post(`uom-range/`, JSON.stringify(UomRange));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a uom_range.
     * 
     * @param UomRange - UomRange
     * @returns Promise<UomRange>
     */
    updateUomRange(UomRange: UomRange): Promise<UomRange> {
        const client$ = this.client.put('uom-range/', JSON.stringify(UomRange));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the uom_range id passed to it.
     * 
     * @param id - string
     * @returns Promise
     */
    deleteUomRange(id: string): Promise<any> {
        const client$ = this.client.delete(`uom-range/${id}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }


}