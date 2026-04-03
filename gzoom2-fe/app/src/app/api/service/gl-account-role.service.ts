import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { GlAccountRole } from '../model/glAccountRole';

@Injectable()
export class GlAccountRoleService {
    constructor(private client: ApiClientService) { }

    showWarningUODetected(glAccontId: string): Observable<boolean> {
        return this.client
            .get(`gl-account-role/${glAccontId}`).pipe(
                map(json => json as boolean)
            );
    }

    getUoDetectedByGlAccountId(glAccountId: string): Observable<GlAccountRole[]> {
        return this.client
            .get(`gl-account-role/uo-detected/${glAccountId}`).pipe(
                map(json => json.results as GlAccountRole[])
            );
    }

    createGlAccountRole(glAccountRole: GlAccountRole[]): Observable<boolean> {
        return this.client
            .post(`gl-account-role`, glAccountRole).pipe(
                map(json => json as boolean)
            );
    }

    updateGlAccountRole(glAccountRole: GlAccountRole[]): Observable<boolean> {
        return this.client
            .put(`gl-account-role`, glAccountRole)
            .pipe(
                map(json => json as boolean)
            );
    }

    deleteGlAccountRole(glAccountRole: GlAccountRole[]): Observable<boolean> {
        return this.client
            .post(`gl-account-role/delete`, glAccountRole)
            .pipe(
                map(json => json as boolean)
            );
    }
}
