import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from 'app/commons/service/client.service';
import { HttpErrorResponse } from '@angular/common/http';
import { WorkEffortContentEx } from '../model/workEffortContentEx';
import { InfoPage } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';

@Injectable()
export class WorkEffortContentService {

    constructor(private client: ApiClientService) { }

    getWorkEffortContentExList(): Observable<WorkEffortContentEx[]> {
        return this.client
            .get(`work-effort-content`).pipe(
                map(json => json.results as WorkEffortContentEx[])
            );
    }

    getWorkEffortContentExListFilter(infoPage: InfoPage): Observable<WorkEffortContentEx[]> {
        return this.client
            .post(`work-effort-content/filter`, infoPage).pipe(
                map(json => json.results as WorkEffortContentEx[])
            );
    }

    async createWorkEffortContentEx(workEffortContentEx: WorkEffortContentEx): Promise<WorkEffortContentEx> {        
        const client$ = this.client.post(`work-effort-content`, JSON.stringify(workEffortContentEx));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    updateWorkEffortContentEx(workEffortContentEx: WorkEffortContentEx): Promise<WorkEffortContentEx> {
        const client$ = this.client.put('work-effort-content', JSON.stringify(workEffortContentEx));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    deleteWorkEffortContentEx(workEffortContentEx: WorkEffortContentEx): Promise<any> {
        const client$ = this.client.post(`work-effort-content/delete/`, JSON.stringify(workEffortContentEx));
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }

}
