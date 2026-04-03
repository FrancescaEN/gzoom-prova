import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { HttpErrorResponse } from '@angular/common/http';
import { GlResourceType } from '../model/glResourceType'




@Injectable()
export class GlResourceTypeService {

    constructor(private client: ApiClientService) { }

    /**
     * Gets the list of gl_resource_type.
     * 
     * @returns Observable<GlResourceType[]>
     */
    getGlResourceType(): Observable<GlResourceType[]> {
        return this.client
            .get(`gl-resource-type/`).pipe(
                map(json => json.results as GlResourceType[])
            );
    }

    getByGlAccountTypeId(glAccountTypeId: string): Observable<GlResourceType[]> {
        return this.client
            .get(`gl-resource-type/${glAccountTypeId}`).pipe(
                map(json => json.results as GlResourceType[])
            );
    }


    /**
     * Create a gl_resource_type.
     * 
     * @param GlResourceType - GlResourceType
     * @returns Promise<GlResourceType>
     */
    async createGlResourceType(GlResourceType: GlResourceType): Promise<GlResourceType> {
        const client$ = this.client.post(`gl-resource-type/`, JSON.stringify(GlResourceType));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a gl_resource_type.
     * 
     * @param GlResourceType - GlResourceType
     * @returns Promise<GlResourceType>
     */
    updateGlResourceType(GlResourceType: GlResourceType): Promise<GlResourceType> {
        const client$ = this.client.put('gl-resource-type/', JSON.stringify(GlResourceType));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the gl_resource_type ids passed to it.
     * 
     * @param id - string
     * @returns Promise
     */
    deleteGlResourceType(id: string): Promise<any> {
        const client$ = this.client.delete(`gl-resource-type/${id}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }

}
