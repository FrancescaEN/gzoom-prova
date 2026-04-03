import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { GlAccountWithWorkEffortPurposeTypeView } from '../model/glAccountWithWorkEffortPurposeTypeView';
import { Filter } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';


/**
 *  @author Leonardo Minaudo
 */
@Injectable()
export class GlAccountWithWorkEffortPurposeTypeViewService {

    constructor(private client: ApiClientService) { }

    getGlAccountWithWorkEffortPurposeTypeView(organizationId: string): Observable<GlAccountWithWorkEffortPurposeTypeView[]> {
        return this.client
            .get(`ga-wept-view/${organizationId}`).pipe(
                map(json => json.results as GlAccountWithWorkEffortPurposeTypeView[])
            );
    }

    /**
    * Gets the list of GlAccountWithWorkEffortPurposeTypeView pagination.
    * 
    * @returns Observable<GlAccountWithWorkEffortPurposeTypeView[]>
    */
    getGlAccountWithWorkEffortPurposeTypeViewFilter(filter: Filter): Observable<GlAccountWithWorkEffortPurposeTypeView[]> {

        return this.client
            .post(`ga-wept-view/filter`, filter).pipe(
                map(json => json.results as GlAccountWithWorkEffortPurposeTypeView[])
            );
    }


}
