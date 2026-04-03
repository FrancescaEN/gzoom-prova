import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { GlAccountMeasRatSc } from '../model/glAccountMeasRatSc';

@Injectable()
export class GlAccountMeasRatScService {
    constructor(private client: ApiClientService) { }

    showWarningValueList(glAccontId: string): Observable<boolean> {
        return this.client
            .get(`gl-account-meas-rat-sc/warning-value-list/${glAccontId}`).pipe(
                map(json => json as boolean)
            );
    }

    getByGlAccountId(glAccountId: string): Observable<GlAccountMeasRatSc[]> {
        return this.client
            .get(`gl-account-meas-rat-sc/${glAccountId}`)
            .pipe(map(json => json.results as GlAccountMeasRatSc[]))
    }

    createGlAccountMeasRatSc(glAccountMeasRatSc: GlAccountMeasRatSc): Observable<boolean> {
        return this.client.post(`gl-account-meas-rat-sc`, glAccountMeasRatSc).pipe(
            map(json => json as boolean)
        )
    }

    updateGlAccountMeasRatSc(glAccountMeasRatSc: GlAccountMeasRatSc[]): Observable<boolean> {
        return this.client.put(`gl-account-meas-rat-sc`, glAccountMeasRatSc).pipe(
            map(json => json as boolean)
        )
    }

    deleteGlAccountMeasRatSc(glAccountMeasRatSc: GlAccountMeasRatSc[]): Observable<boolean> {
        const client$ = this.client.post(`gl-account-meas-rat-sc/delete`, glAccountMeasRatSc);
        return client$
            .pipe(map(json => json as boolean));
    }
}
