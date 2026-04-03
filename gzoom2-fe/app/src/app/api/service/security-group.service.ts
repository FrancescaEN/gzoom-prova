import { Injectable } from '@angular/core';

import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { HttpErrorResponse } from '@angular/common/http';
import { SecurityGroup } from '../model/securityGroup';


@Injectable()
export class SecurityGroupService {

    constructor(private client: ApiClientService) { }

    getSecurityGroupList(): Observable<SecurityGroup[]> {
        return this.client
            .get(`security-group/`).pipe(
                map(json => json.results as SecurityGroup[])
            );
    }

    getSecurityGroupById(securityGroupId): Observable<SecurityGroup> {
        return this.client
            .get(`security-group/${securityGroupId}`).pipe(
                map(json => json as SecurityGroup)
            );
    }

    async createSecurityGroup(securityGroup: SecurityGroup): Promise<boolean> {
        const client$ = this.client.post(`security-group/`, JSON.stringify(securityGroup));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };


    async updateSecurityGroup(securityGroup: SecurityGroup): Promise<boolean> {
        const client$ = this.client.put('security-group/', JSON.stringify(securityGroup));
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (response_1) {
            console.error(`Error while updating in: ${response_1.error.message}`);
            return await Promise.reject(response_1.error.message);
        }

    };

    async deleteSecurityGroup(id: String[]): Promise<boolean> {
        const client$ = this.client.delete(`security-group/${id}`);
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (error) {
            console.error(`Error while deleting in: ${error.error.message}`);
            return await Promise.reject(error.error);
        }
    }
}
