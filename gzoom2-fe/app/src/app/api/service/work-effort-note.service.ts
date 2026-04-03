import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from 'app/commons/service/client.service';
import { HttpErrorResponse } from '@angular/common/http';
import { WorkEffortNoteExNoteData } from '../model/workEffortNoteExNoteData';
import { InfoPage } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';


/**
 *  @author Leonardo Minaudo
 */
@Injectable()
export class WorkEffortNoteService {

    constructor(private client: ApiClientService) { }

    getWorkEffortNoteExNoteDataList(): Observable<WorkEffortNoteExNoteData[]> {
        return this.client
            .get(`work-effort-note`).pipe(
                map(json => json.results as WorkEffortNoteExNoteData[])
            );
    }

    getWorkEffortNoteExNoteDataListPaginationTotal(infoPage: InfoPage): Observable<any> {
        return this.client
            .post(`work-effort-note/total`, infoPage).pipe(
                map(json => json as Number)
            );
    }

    getWorkEffortNoteExNoteDataListPagination(infoPage: InfoPage): Observable<any[]> {
        return this.client
            .post(`work-effort-note/pagination`, infoPage).pipe(
                map(json => json as WorkEffortNoteExNoteData[])
            );
    }

    async createWorkEffortNoteExNoteData(workEffortNoteExNoteData: WorkEffortNoteExNoteData): Promise<WorkEffortNoteExNoteData> {
        const client$ = this.client.post(`work-effort-note`, JSON.stringify(workEffortNoteExNoteData));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    updateWorkEffortNoteExNoteData(workEffortNoteExNoteData: WorkEffortNoteExNoteData): Promise<WorkEffortNoteExNoteData> {
        const client$ = this.client.put('work-effort-note', JSON.stringify(workEffortNoteExNoteData));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })

    };

    deleteWorkEffortNoteExNoteData(workEffortId: string, noteId: string): Promise<any> {
        const client$ = this.client.delete(`work-effort-note/${workEffortId}/${noteId}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }


}
