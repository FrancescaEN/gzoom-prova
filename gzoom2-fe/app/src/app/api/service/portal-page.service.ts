import { Injectable } from '@angular/core';

import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';
import { PortalPage } from '../model/portalPage';


@Injectable()
export class PortalPageService {

    constructor(private client: ApiClientService) { }

    findByParentPortalPageId(parentPortalPageId: string): Observable<PortalPage[]> {
        return this.client
            .get(`portal-page/parent-portal-page-id/${parentPortalPageId}`).pipe(
                map(json => json.results as PortalPage[])
            );
    }
}
