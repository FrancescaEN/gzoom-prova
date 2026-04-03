import { Injectable } from '@angular/core';

import { lastValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { CustomMethodMatrix } from '../model/customMethodMatrix';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class CustomMethodMatrixService {
    constructor(private client: ApiClientService) { }

    getCustomMethodMatrixList(customMethodId: string): Observable<CustomMethodMatrix[]> {
        return this.client
            .get(`custom-method-matrix?customMethodId=${customMethodId}`).pipe(
                map(json => json.results as CustomMethodMatrix[])
            );
    }

    /**
     * Create a custom_method_matrix.
     * 
     * @param CustomMethodMatrix - CustomMethodMatrix
     * @returns Promise<string>
     */
    async createCustomMethodMatrix(CustomMethodMatrix: CustomMethodMatrix): Promise<string> {
        const client$ = this.client.post(`custom-method-matrix/`, JSON.stringify(CustomMethodMatrix));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a custom_method_matrix.
     * 
     * @param CustomMethodMatrix - CustomMethodMatrix
     * @returns Promise<CustomMethodMatrix>
     */
    updateCustomMethodMatrix(CustomMethodMatrix: CustomMethodMatrix): Promise<CustomMethodMatrix> {
        const client$ = this.client.put('custom-method-matrix/', JSON.stringify(CustomMethodMatrix));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the custom_method_matrix id passed to it.
     * 
     * @param customMethodMatrixId - string
     * @returns Promise
     */
    deleteCustomMethodMatrix(customMethodMatrixId: string): Promise<any> {
        const client$ = this.client.delete(`custom-method-matrix/${customMethodMatrixId}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }


}