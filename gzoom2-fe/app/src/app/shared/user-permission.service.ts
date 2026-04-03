import { Injectable, inject } from '@angular/core';
import { Permissions } from '../commons/model/dto';
import * as _ from 'lodash';
import { Context } from 'app/commons/enum/context';
import { ApiClientService } from 'app/commons/service/client.service';
import { Observable, map, of, tap } from 'rxjs';
import { UserCtxPermissionView } from 'app/api/model/userCtxPermissionView';

/**
 * Possible permission actions.
 */
export enum Permission {
    ADMIN = "_ADMIN",
    RESP = "_RESP",
    VIEW = "_VIEW",
    NONE = "_NONE"
};

export type UserPermission = { [x: string]: string };

/**
 * Contains information about user authorization.
 */
@Injectable()
export class UserPermissionService {
    private permissions: UserPermission;
    private apiClient = inject(ApiClientService);

    constructor() {
        this.init;
    }

    init(): Observable<UserPermission> {
        return this.apiClient.get('user-ctx-permission-view')
            .pipe(
                map(json => json.results as UserCtxPermissionView[]),
                map(values => {
                    this.permissions = {};
                    values.forEach(value => this.permissions[value.userCtx] = value.ctxPermission);
                    return this.permissions;
                })
            )

    }

    isInitialized() {
        return !!this.permissions;
    }

    clear() {
        this.permissions = null;
    }

    hasPermission(context: Context, permission: Permission): Observable<boolean> {
        if (!this.permissions) {
            return this.init()
                .pipe(
                    map(perm => perm[context] === permission)
                );

        }
        else {
            return of(this.permissions[context] === permission);
        }

    }


    hasAnyPermission(context: Context): Observable<boolean> {
        if (!this.permissions) {
            return this.init()
                .pipe(
                    map(perm => !!perm[context] && perm[context] !== Permission.NONE)
                );
        }
        else
            return of(!!this.permissions[context] && this.permissions[context] !== Permission.NONE);
    }
}
