import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';
import { SecurityPermission } from '../model/securityPermission';


@Injectable()
export class SecurityPermissionService {

    constructor(private client: ApiClientService) { }

    findByEnabledOrderByPrimaryKey(enabled: string): Observable<SecurityPermission[]> {
        return this.client
            .get(`security-permission/enabled/${enabled}`).pipe(
                map(json => json.results as SecurityPermission[])
            );
    }
}
