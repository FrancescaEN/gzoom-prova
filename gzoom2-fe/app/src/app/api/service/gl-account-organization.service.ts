import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { GlAccountOrganization } from '../model/glAccountOrganization';

@Injectable()
export class GlAccountOrganizationService {
    constructor(private client: ApiClientService) { }

    getGlAccountOrganizationByGlAccountId(glAccontId: string): Observable<GlAccountOrganization[]> {
        return this.client
            .get(`gl-account-organization/${glAccontId}`).pipe(
                map(json => json.results as GlAccountOrganization[])
            );
    }

    delete(organizationId: string[], glAccountId: string): Observable<boolean> {
        return this.client
            .delete(`gl-account-organization/${glAccountId}/${organizationId}`).pipe(
                map(json => json as boolean)
            );
    }

    update(glAccountOrganization: GlAccountOrganization[]): Observable<boolean> {
        return this.client
            .put(`gl-account-organization/`, glAccountOrganization).pipe(
                map(json => json as boolean)
            );
    }

    create(organizationPartyId: string[], glAccountId: string, fromDate: Date, thruDate: Date) {
        let glAccountOrganization = new GlAccountOrganization();
        glAccountOrganization.fromDate = fromDate;
        glAccountOrganization.thruDate = thruDate;

        return this.client
            .post(`gl-account-organization/${glAccountId}/${organizationPartyId}`, glAccountOrganization).pipe(
                map(json => json as boolean)
            );
    }
}
