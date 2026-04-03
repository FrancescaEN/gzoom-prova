import { Injectable } from '@angular/core';

import { lastValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { CustomMethod } from '../model/customMethod';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class CustomMethodService {
    constructor(private client: ApiClientService) { }

    getCustomMethodList(): Observable<CustomMethod[]> {
        return this.client
            .get(`custom-method/`).pipe(
                map(json => json.results as CustomMethod[])
            );
    }

    /**
     * Create a custom_method.
     * 
     * @param CustomMethod - CustomMethod
     * @returns Promise<CustomMethod>
     */
    async createCustomMethod(CustomMethod: CustomMethod): Promise<CustomMethod> {
        const client$ = this.client.post(`custom-method/`, JSON.stringify(CustomMethod));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a custom_method.
     * 
     * @param CustomMethod - CustomMethod
     * @returns Promise<CustomMethod>
     */
    updateCustomMethod(CustomMethod: CustomMethod): Promise<CustomMethod> {
        const client$ = this.client.put('custom-method/', JSON.stringify(CustomMethod));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the custom_method id passed to it.
     * 
     * @param id - string
     * @returns Promise
     */
    deleteCustomMethod(id: string): Promise<any> {

        const client$ = this.client.post(`custom-method/delete`, JSON.stringify(id));
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }


}