import { Injectable } from '@angular/core';

import { lastValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { GlAccountResource } from '../model/glAccountResource';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class GlAccountResourceService {
    constructor(private client: ApiClientService) { }

    getGlAccountResourceList(glAccontTypeId: string): Observable<GlAccountResource[]> {
        console.log(glAccontTypeId);
        
        return this.client
            .get(`gl-account-resource/${glAccontTypeId}`).pipe(
                map(json => json.results as GlAccountResource[])
            );
    }

    /**
     * Create a gl_account_type.
     * 
     * @param GlAccountResource - GlAccountResource
     * @returns Promise<GlAccountResource>
     */
    async createGlAccountResource(GlAccountResource: GlAccountResource): Promise<GlAccountResource> {
        const client$ = this.client.post(`gl-account-resource/`, JSON.stringify(GlAccountResource));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a gl_account_type.
     * 
     * @param GlAccountResource - GlAccountResource
     * @returns Promise<GlAccountResource>
     */
    updateGlAccountResource(GlAccountResource: GlAccountResource): Promise<GlAccountResource> {
        const client$ = this.client.put('gl-account-resource/', JSON.stringify(GlAccountResource));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the gl_account_type id passed to it.
     * 
     * @param glAccountTypeId - string
     * @param glResourceTypeId - string
     * @returns Promise
     */
    deleteGlAccountResource(glAccountTypeId: string, glResourceTypeId: string): Promise<any> {
        const client$ = this.client.delete(`gl-account-resource/${glAccountTypeId}/${glResourceTypeId}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }


}