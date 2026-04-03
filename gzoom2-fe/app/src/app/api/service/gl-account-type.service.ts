import { Injectable } from '@angular/core';

import { lastValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { GlAccountType } from '../model/glAccountType';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class GlAccountTypeService {
    constructor(private client: ApiClientService) { }

    getGlAccountTypeId(glAccountTypeId?: string): Observable<GlAccountType> {
        return this.client
            .get(`gl-account-type/glAccountTypeId/${glAccountTypeId}`).pipe(
                map(json => json as GlAccountType)
            );
    }

    getGlAccountTypeList(accountTypeEnumId?: string): Observable<GlAccountType[]> {
        return this.client
            .get(`gl-account-type/${accountTypeEnumId ?? ''}`).pipe(
                map(json => json.results as GlAccountType[])
            );
    }

    getGlAccountTypeByReservedAccount(accountTypeEnumId: string, isReservedAccount?: string): Observable<GlAccountType[]> {
        return this.client
            .get(`gl-account-type/${accountTypeEnumId}/${isReservedAccount ?? ''}`).pipe(
                map(json => json.results as GlAccountType[])
            );
    }

    /**
     * Create a gl_account_type.
     * 
     * @param GlAccountType - GlAccountType
     * @returns Promise<GlAccountType>
     */
    async createGlAccountType(GlAccountType: GlAccountType): Promise<GlAccountType> {
        const client$ = this.client.post(`gl-account-type/`, JSON.stringify(GlAccountType));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a gl_account_type.
     * 
     * @param GlAccountType - GlAccountType
     * @returns Promise<GlAccountType>
     */
    updateGlAccountType(GlAccountType: GlAccountType): Promise<GlAccountType> {
        const client$ = this.client.put('gl-account-type/', JSON.stringify(GlAccountType));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the gl_account_type id passed to it.
     * 
     * @param id - string
     * @returns Promise
     */
    deleteGlAccountType(id: string): Promise<any> {

        const client$ = this.client.delete(`gl-account-type/${id}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }


}