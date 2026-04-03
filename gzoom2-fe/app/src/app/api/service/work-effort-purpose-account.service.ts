import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';
import { WorkEffortPurposeAccount } from '../model/workEffortPurposeAccount';
import { AbstractControl } from '@angular/forms';


/**
 *  @author Leonardo Minaudo
 */
@Injectable()
export class WorkEffortPurposeAccountService {

    constructor(private client: ApiClientService) { }

    existWorkEffortPurposeAccountByGlAccountId(glAccountId: string): Observable<boolean> {
        return this.client
            .get(`work-effort-purpose-account/${glAccountId}`).pipe(
                map(json => json as boolean)
            );
    }

    updateComments(workEffortPurposeAccount: WorkEffortPurposeAccount[]): Observable<boolean> {
        return this.client
            .put(`work-effort-purpose-account/`, workEffortPurposeAccount).pipe(
                map(json => json as boolean)
            );
    }

    create(workEffortPurposeTypeId: string[], glAccountId: string, comments: string) {
        return this.client
            .post(`work-effort-purpose-account/${glAccountId}/${workEffortPurposeTypeId}`, JSON.stringify(comments)).pipe(
                map(json => json as boolean)
            );
    }

    delete(workEffortPurposeTypeId: string[], glAccountId: string): Observable<boolean> {
        return this.client
            .delete(`work-effort-purpose-account/${glAccountId}/${workEffortPurposeTypeId}`).pipe(
                map(json => json as boolean)
            );
    }



}
