import { Injectable } from '@angular/core';

import { lastValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { GlAccountTypeGlFiscalType } from '../model/glAccoutTypeGlFiscalType';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class GlAccountTypeGlFiscalTypeService {
    constructor(private client: ApiClientService) { }

    getGlAccountTypeGlFiscalTypeList(glAccontTypeId: string): Observable<GlAccountTypeGlFiscalType[]> {
        return this.client
            .get(`gl-account-type-gl-fiscal-type/${glAccontTypeId}`).pipe(
                map(json => json.results as GlAccountTypeGlFiscalType[])
            );
    }

    /**
     * Create a gl_account_type_gl_fiscal_type.
     * 
     * @param GlAccountTypeGlFiscalType - GlAccountTypeGlFiscalType
     * @returns Promise<GlAccountTypeGlFiscalType>
     */
    async createGlAccountTypeGlFiscalType(GlAccountTypeGlFiscalType: GlAccountTypeGlFiscalType): Promise<GlAccountTypeGlFiscalType> {
        const client$ = this.client.post(`gl-account-type-gl-fiscal-type/`, JSON.stringify(GlAccountTypeGlFiscalType));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a gl_account_type_gl_fiscal_type.
     * 
     * @param GlAccountTypeGlFiscalType - GlAccountTypeGlFiscalType
     * @returns Promise<GlAccountTypeGlFiscalType>
     */
    updateGlAccountTypeGlFiscalType(GlAccountTypeGlFiscalType: GlAccountTypeGlFiscalType): Promise<GlAccountTypeGlFiscalType> {
        const client$ = this.client.put('gl-account-type-gl-fiscal-type/', JSON.stringify(GlAccountTypeGlFiscalType));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    /**
     * Delete the gl_account_type_gl_fiscal_type id passed to it.
     * 
     * @param glAccountTypeId - string
     * @param glFiscalTypeId - string
     * @returns Promise
     */
    deleteGlAccountTypeGlFiscalType(glAccountTypeId: string, glFiscalTypeId: string): Promise<any> {
        const client$ = this.client.delete(`gl-account-type-gl-fiscal-type/${glAccountTypeId}/${glFiscalTypeId}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }


}