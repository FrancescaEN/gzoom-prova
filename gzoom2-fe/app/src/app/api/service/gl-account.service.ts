import { Injectable } from '@angular/core';

import { lastValueFrom, Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { GlAccount } from '../model/glAccount';




@Injectable({
    providedIn: 'root'
})
export class GlAccountService {

    constructor(private client: ApiClientService) { }

    /**
     * Gets decimal precision
     * 
     * @returns Promise<number>
     */
    getPrecisionDecimal(glAccountId): Promise<number> {

        const client$ = this.client.get(`glaccount-precision/${glAccountId}`)
        return lastValueFrom(client$).catch((response: any) => {
            console.log(`Error: ${response}`);
            return Promise.reject(response.json() || response);
        })

    }

    getGlAccount(id: string): Observable<GlAccount> {
        return this.client
            .get(`gl-account/${id}`)
            .pipe(map(json => json as GlAccount));
    }

    getGlAccountByOrganizationPartyId(): Observable<GlAccount[]> {
        return this.client
            .get(`gl-account/referenced-account-id`)
            .pipe(map(json => json.results as GlAccount[]));
    }


    getGlAccountByAccountTypeEnumIdAndIsReservedAccount(accountTypeEnumId: string, isReservedAccount?: string): Observable<GlAccount[]> {
        let url = `gl-account/account-type-enum-id/${accountTypeEnumId}/`;
        if (isReservedAccount) url += isReservedAccount;
        return this.client
            .get(url)
            .pipe(map(json => json.results as GlAccount[]));
    }

    getGlAccountMovements(accountTypeEnumId: string, inputEnumId: string, detectOrgUnitIdFlag: string, isReservedAccount: string): Observable<GlAccount[]> {
        let url = `gl-account/movements/${accountTypeEnumId}/${inputEnumId}/${detectOrgUnitIdFlag}/`;
        if (isReservedAccount) url += isReservedAccount;
        return this.client
            .get(url)
            .pipe(map(json => json.results as GlAccount[]));
    }

    isByInputEnumIdAndDetectOrgUnitIdFlag(glAccountId: string, inputEnumId: string, detectOrgUnitIdFlag: string): Observable<boolean> {
        return this.client
            .get(`gl-account/${glAccountId}/input-enum-id-and-detect-org-unit-id-flag/${inputEnumId}/${detectOrgUnitIdFlag}`)
            .pipe(map(json => json as boolean));
    }

    getGlAccountByParam(accountTypeEnumId: string, isReservedAccount: string, params: any): Observable<GlAccount[]> {
        return this.client
            .get(isReservedAccount ? `gl-account/account-type-enum-id/${accountTypeEnumId}/${isReservedAccount}` : `gl-account/account-type-enum-id/${accountTypeEnumId}`, params)
            .pipe(map(json => json.results as GlAccount[]))
    }

    getGlAccountByFilterParams(params: any): Observable<GlAccount[]> {
        return this.client
            .get(`gl-account/filter-params`, params)
            .pipe(map(json => json.results as GlAccount[]))
    }

    selectGlAccountByOrgId(): Observable<GlAccount[]> {
        return this.client
            .get(`gl-account/select-all-by-org-user`)
            .pipe(map(json => json.results as GlAccount[]))
    }

    createGlAccount(glAccount: GlAccount, workEffortPurposeTypeId: string[]): Observable<GlAccount> {
        const body = {
            glAccount,
            workEffortPurposeTypeId
        }
        return this.client.post(`gl-account`, body).pipe(
            map(json => json as GlAccount)
        )
    }

    updateGlAccount(glAccount: GlAccount): Observable<GlAccount> {
        return this.client.put(`gl-account`, glAccount).pipe(
            map(json => json as GlAccount)
        )
    }

    deleteGlAccount(glAccountIds: string[]): Observable<boolean> {
        const client$ = this.client.delete(`gl-account/${glAccountIds}`);

        return client$
            .pipe(map(json => json as boolean));
    }

    updateCalcCustomMethodAndPrioCalc(glAccountId: string, calcCustomMethodId: string, prioCalc: number) {

        let ga: GlAccount = new GlAccount();
        ga.calcCustomMethodId = calcCustomMethodId;
        ga.prioCalc = prioCalc;
        return this.client.put(`gl-account/${glAccountId}/calc-method`, ga).pipe(
            map(json => json as GlAccount)
        )
    }

}