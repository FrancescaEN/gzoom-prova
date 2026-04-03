import { Injectable } from '@angular/core';

import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { HttpErrorResponse } from '@angular/common/http';
import { SecurityGroupPermission } from '../model/securityGroupPermission';


@Injectable()
export class SecurityGroupPermissionService {

    constructor(private client: ApiClientService) { }

    getSecurityGroupPermissionByGroupId(groupId: string): Observable<SecurityGroupPermission[]> {
        return this.client
            .get(`security-group-permission/group-id/${groupId}`).pipe(
                map(json => json.results as SecurityGroupPermission[])
            );
    }

    async createSecurityGroupPermission(securityGroupPermission: SecurityGroupPermission): Promise<boolean> {
        const client$ = this.client.post(`security-group-permission`, JSON.stringify(securityGroupPermission));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };


    async updateSecurityGroupPermission(securityGroupPermission: SecurityGroupPermission): Promise<boolean> {
        const client$ = this.client.put(`security-group-permission`, JSON.stringify(securityGroupPermission));
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (error) {
            console.error(`Error while updating in: ${error.error.message}`);
            return await Promise.reject(error.error.message);
        }

    };

    async deleteSecurityGroupPermission(groupId: string, permissionId: string): Promise<boolean> {
        const client$ = this.client.delete(`security-group-permission/${groupId}/${permissionId}`);
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (error) {
            console.error(`Error while deleting in: ${error.error.message}`);
            return await Promise.reject(error.error);
        }
    }
}
