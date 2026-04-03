import { Injectable } from '@angular/core';

import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { HttpErrorResponse } from '@angular/common/http';
import { SecurityGroupContent } from '../model/securityGroupContent';


@Injectable()
export class SecurityGroupContentService {

    constructor(private client: ApiClientService) { }

    getSecurityGroupContentByGroupId(groupId: string): Observable<SecurityGroupContent[]> {
        return this.client
            .get(`security-group-content/group-id/${groupId}`).pipe(
                map(json => json.results as SecurityGroupContent[])
            );
    }

    async createSecurityGroupContent(securityGroupContent: SecurityGroupContent): Promise<boolean> {
        const client$ = this.client.post(`security-group-content`, JSON.stringify(securityGroupContent));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };


    async updateSecurityGroupContent(securityGroupContent: SecurityGroupContent): Promise<boolean> {
        const client$ = this.client.put(`security-group-content`, JSON.stringify(securityGroupContent));
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (error) {
            console.error(`Error while updating in: ${error.error.message}`);
            return await Promise.reject(error.error.message);
        }

    };

    async deleteSecurityGroupContent(groupId: string, contentId: string, fromDate: Date): Promise<boolean> {
        const client$ = this.client.delete(`security-group-content/${groupId}/${contentId}/${new Date(fromDate).toISOString()}`);
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (error) {
            console.error(`Error while deleting in: ${error.error.message}`);
            return await Promise.reject(error.error);
        }
    }
}
