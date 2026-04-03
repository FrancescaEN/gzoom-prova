import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { GlAccountClass } from '../model/glAccountClass';




@Injectable()
export class GlAccountClassService {

    constructor(private client: ApiClientService) { }


    getByAccountTypeEnumId(accountTypeEnumId: string): Observable<GlAccountClass[]> {
        return this.client
            .get(`gl-account-class/${accountTypeEnumId}`).pipe(
                map(json => json.results as GlAccountClass[])
            );
    }

}
