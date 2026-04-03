import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { GlAccountInputCalc } from '../model/glAccountInputCalc';

@Injectable()
export class GlAccountInputCalcService {

    constructor(private client: ApiClientService) { }

    showWarningCalculationFormula(glAccontId: string): Observable<boolean> {
        return this.client
            .get(`gl-account-input-calc/warning-calculation-formula/${glAccontId}`).pipe(
                map(json => json as boolean)
            );
    }

    getGlAccountInputCalcByGlAccountId(glAccontId: string): Observable<GlAccountInputCalc[]> {
        return this.client
            .get(`gl-account-input-calc/${glAccontId}`).pipe(
                map(json => json.results as GlAccountInputCalc[])
            );
    }

    getGlAccountInputCalcByGlAccountIdRef(glAccontIdRef: string): Observable<GlAccountInputCalc[]> {
        return this.client
            .get(`gl-account-input-calc/ref/${glAccontIdRef}`).pipe(
                map(json => json.results as GlAccountInputCalc[])
            );
    }

    create(glAccountInputCalc: GlAccountInputCalc): Observable<GlAccountInputCalc> {
        return this.client.post(`gl-account-input-calc`, glAccountInputCalc).pipe(
            map(json => json as GlAccountInputCalc)
        )
    }

    updateGlAccountIdRef(glAccountInputCalcId: string, glAccountIdRef: string): Observable<boolean> {
        return this.client.put(`gl-account-input-calc/ref/${glAccountInputCalcId}`, glAccountIdRef).pipe(
            map(json => json as boolean)
        )
    }

    update(GlAccountInputCalcItems: GlAccountInputCalc[]): Observable<boolean> {
        return this.client.put(`gl-account-input-calc/`, GlAccountInputCalcItems).pipe(
            map(json => json as boolean)
        )
    }

    delete(glAccountInputCalcIds: string[]) {
        return this.client.delete(`gl-account-input-calc/${glAccountInputCalcIds}`).pipe(
            map(json => json as boolean)
        )
    }
}
