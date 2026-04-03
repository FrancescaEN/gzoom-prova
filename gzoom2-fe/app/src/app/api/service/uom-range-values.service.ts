import { Injectable } from '@angular/core';

import { lastValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';

import { HttpErrorResponse } from '@angular/common/http';
import { UomRangeValues } from '../model/uomRangeValues';
import { UomRangeValues as urv } from 'app/view/ctx-ac/uom/range-values/uomRangeValues'

@Injectable()
export class UomRangeValuesService {
    constructor(private client: ApiClientService) { }


    uomRangeValues(uomRangeId: string): Observable<UomRangeValues[]> {
        return this.client
            .get(`uom-range-values/${uomRangeId}`).pipe(
                map(json => json.results as UomRangeValues[])
            );
    }

    /**
     * Gets the maximum range value.
     * 
     * @param uomRangeId - rangeDafault from comments.
     * @returns Promise<number>
     */
    async uomRangeValuesMax(uomRangeId: string): Promise<number> {

        const client$ = this.client.get(`uom-range-values-max/${uomRangeId}`);
        return await lastValueFrom(client$).then(response => response.results[0])
            .catch((response: any) => {
                console.log(`Error: ${response}`);
                return Promise.reject(response.json() || response);

            })

        // return await this.client.get(`uom-range-values-max/${uomRangeId}`)
        //     .toPromise().then(response => response.results[0])
        //     .catch((response: any) => {
        //         console.log(`Error: ${response}`);
        //         return Promise.reject(response.json() || response);

        //     })
    }

    /**
     * Gets the minimum range value.
     * 
     * @param uomRangeId - rangeDafault from comments.
     * @returns Observable<number>
     */
    uomRangeValuesMin(uomRangeId: string): Observable<number> {

        return this.client
            .get(`uom-range-values-min/${uomRangeId}`).pipe(
                map(json => json as number)
            );
    }

    /**
     * Gets the emoticon path based on amount.
     * 
     * @param rangeDefault - rangeDafault from comments.
     * @param amount - Amount of work effort.
     * @returns Observable<UomRangeValues[]>
     */
    uomRangeValuesPathEmoticon(rangeDefault: string, amount: number): Observable<urv[]> {
        console.log(`uom-range-values-path/${rangeDefault}/${amount}`);

        return this.client
            .get(`uom-range-values-path/${rangeDefault}/${amount}`).pipe(
                map(json => json.results as urv[])
            );
    }

    getUomRangeValuesList(uomRangeId: string): Observable<UomRangeValues[]> {
        return this.client
            .get(`uom-range-values-list/${uomRangeId}`).pipe(
                map(json => json.results as UomRangeValues[])
            );
    }

    /**
     * Create a uom_range_values.
     * 
     * @param UomRangeValues - UomRangeValues
     * @returns Promise<UomRangeValues>
     */
    async createUomRangeValues(UomRangeValues: UomRangeValues): Promise<UomRangeValues> {
        const client$ = this.client.post(`uom-range-values/`, JSON.stringify(UomRangeValues));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a uom_range_values.
     * 
     * @param UomRangeValues - UomRangeValues
     * @returns Promise<UomRangeValues>
     */
    updateUomRangeValues(UomRangeValues: UomRangeValues): Promise<UomRangeValues> {
        const client$ = this.client.put('uom-range-values/', JSON.stringify(UomRangeValues));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the uom_range_values id passed to it.
     * 
     * @param id - string
     * @returns Promise
     */
    deleteUomRangeValues(id: string): Promise<any> {
        const client$ = this.client.delete(`uom-range-values/${id}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }
}