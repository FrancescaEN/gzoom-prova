import { Injectable } from '@angular/core';

import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { HttpErrorResponse } from '@angular/common/http';
import { StatusValidChange } from '../model/statusValidChange';


@Injectable()
export class StatusValidChangeService {

    constructor(private client: ApiClientService) { }

    getByStatusTypeId(statusTypeId: string): Observable<StatusValidChange[]> {
        return this.client
            .get(`status-valid-change/status-type-id/${statusTypeId}`).pipe(
                map(json => json.results as StatusValidChange[])
            );
    }

    async createStatusValidChange(statusItem: StatusValidChange, statusTypeId: string): Promise<boolean> {
        const client$ = this.client.post(`status-valid-change/${statusTypeId}`, JSON.stringify(statusItem));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };


    async updateStatusValidChange(statusItem: StatusValidChange, statusTypeId: string): Promise<boolean> {
        const client$ = this.client.put(`status-valid-change/${statusTypeId}`, JSON.stringify(statusItem));
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (error) {
            console.error(`Error while updating in: ${error.error.message}`);
            return await Promise.reject(error.error.message);
        }

    };

    async deleteStatusValidChange(statusId: string, statusIdTo): Promise<boolean> {
        const client$ = this.client.delete(`status-valid-change/${statusId}/${statusIdTo}`);
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (error) {
            console.error(`Error while deleting in: ${error.error.message}`);
            return await Promise.reject(error.error);
        }
    }
}
