import { Injectable } from '@angular/core';

import { lastValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { UomRange } from '../model/uomRange';
import { UomRatingScale } from '../model/uomRatingScale';
import { HttpErrorResponse } from '@angular/common/http';
import { UomRangeValues } from '../model/uomRangeValues';

@Injectable({
    providedIn: 'root'
})
export class UomRatingScaleService {
    constructor(private client: ApiClientService) { }

    getAllUomRatingScale(): Observable<UomRatingScale[]> {
        return this.client
            .get(`uom/scale`).pipe(
                map(json => json.results as UomRatingScale[])
            );
    }

    getUomRatingScaleByPrimaryKey(uomId: string, uomRatingValue: number): Observable<UomRatingScale> {
        return this.client
            .get(`uom/uom-rating-value/${uomId}/${uomRatingValue}`).pipe(
                map(json => json as UomRatingScale)
            );
    }

    getUomRatingScale(uomId: string): Observable<UomRatingScale[]> {
        return this.client
            .get(`uom/scale/uomId/${uomId}`).pipe(
                map(json => json.results as UomRatingScale[])
            );
    }

    getByGlAccountId(glAccountId: string): Observable<UomRatingScale[]> {
        return this.client
            .get(`uom/gl-account/${glAccountId}`).pipe(
                map(json => json.results as UomRatingScale[])
            );
    }

    getByGlAccountIdOnGlAccountMeasRatSc(glAccountId: string): Observable<UomRatingScale[]> {
        return this.client
            .get(`uom/gl-account-meas-rat-sc/${glAccountId}`).pipe(
                map(json => json.results as UomRatingScale[])
            );
    }


    getUomRatingScalesExcludingGlAccount(glAccountId: string): Observable<UomRatingScale[]> {
        return this.client
            .get(`uom/gl-account-meas-rat-sc-excluding/${glAccountId}`).pipe(
                map(json => json.results as UomRatingScale[])
            );
    }



    uomRatingScales(uomId: string): Observable<UomRatingScale[]> {
        return this.client
            .get(`uom/scale/${uomId}`).pipe(
                map(json => json.results as UomRatingScale[])
            );
    }

    uomRangeValues(uomRangeId: string): Observable<UomRangeValues[]> {
        return this.client
            .get(`uom-range-values/${uomRangeId}`).pipe(
                map(json => json.results as UomRangeValues[])
            );
    }

    createUomRatingScale(uomRatingScale: UomRatingScale): Promise<UomRatingScale> {
        console.log('create UomRatingScale');

        const client$ = this.client.post('uom/scale', JSON.stringify(uomRatingScale))
        return lastValueFrom(client$).then(response => response)
            .catch(response => {
                console.error(`Error while creating in: ${response}`);
                return Promise.reject(response);
            });
    }

    updateUomRatingScale(uomRatingScale: UomRatingScale): Promise<UomRatingScale> {
        console.log('update UomRatingScale');

        const client$ = this.client.put(`uom/scale`, JSON.stringify(uomRatingScale));
        return lastValueFrom(client$).then(response => response)
            .catch((response: any) => {
                console.error(`Error while updating in: ${response}`);
                return Promise.reject(response);
            });
    }

    deleteUomRatingScale(uomId: string, uomRatingValue: number): Promise<UomRatingScale> {
        console.log('delete UomRatingScale with ' + uomId);

        const client$ = this.client.delete(`uom/scale/${uomId}/${uomRatingValue}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while exec query: ${error.error.message}`);
                return Promise.reject(error);
            });
    }

}