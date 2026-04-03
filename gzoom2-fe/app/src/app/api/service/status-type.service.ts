import { Injectable } from '@angular/core';

import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { StatusType } from '../model/statusType';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class StatusTypeService {

    constructor(private client: ApiClientService) { }

    getStatusTypeList(): Observable<StatusType[]> {
        return this.client
            .get(`status-type/`).pipe(
                map(json => json.results as StatusType[])
            );
    }

    getStatusTypeById(statusTypeId): Observable<StatusType> {
        return this.client
            .get(`status-type/${statusTypeId}`).pipe(
                map(json => json as StatusType)
            );
    }

    async createStatusType(statusType: StatusType): Promise<boolean> {
        const client$ = this.client.post(`status-type/`, JSON.stringify(statusType));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };


    async updateStatusType(statusType: StatusType): Promise<boolean> {
        const client$ = this.client.put('status-type/', JSON.stringify(statusType));
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (response_1) {
            console.error(`Error while updating in: ${response_1.error.message}`);
            return await Promise.reject(response_1.error.message);
        }

    };

    async deleteStatusType(id: String[]): Promise<boolean> {
        const client$ = this.client.delete(`status-type/${id}`);
        try {
            const response = await lastValueFrom(client$);
            return response;
        } catch (error) {
            console.error(`Error while deleting in: ${error.error.message}`);
            return await Promise.reject(error.error);
        }
    }
}
