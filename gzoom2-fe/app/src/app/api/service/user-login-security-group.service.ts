import { Injectable } from '@angular/core';

import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { HttpErrorResponse } from '@angular/common/http';
import { UserLoginSecurityGroup } from '../model/userLoginSecurityGroup';


@Injectable()
export class UserLoginSecurityGroupService {

    constructor(private client: ApiClientService) { }

    getUserLoginSecurityGroupByGroupId(groupId: string): Observable<UserLoginSecurityGroup[]> {
        return this.client
            .get(`user-login-security-group/group-id/${groupId}`).pipe(
                map(json => json.results as UserLoginSecurityGroup[])
            );
    }

    async createUserLoginSecurityGroup(userLoginSecurityGroup: UserLoginSecurityGroup): Promise<boolean> {
        const client$ = this.client.post(`user-login-security-group`, JSON.stringify(userLoginSecurityGroup));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };


    async updateUserLoginSecurityGroup(userLoginSecurityGroup: UserLoginSecurityGroup): Promise<boolean> {
        const client$ = this.client.put(`user-login-security-group`, JSON.stringify(userLoginSecurityGroup));
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (error) {
            console.error(`Error while updating in: ${error.error.message}`);
            return await Promise.reject(error.error.message);
        }

    };

    async deleteUserLoginSecurityGroup(userLoginId: string, groupId: string, fromDate: Date): Promise<boolean> {
        const client$ = this.client.delete(`user-login-security-group/${userLoginId}/${groupId}/${new Date(fromDate).toISOString()}`);
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (error) {
            console.error(`Error while deleting in: ${error.error.message}`);
            return await Promise.reject(error.error);
        }
    }
}
