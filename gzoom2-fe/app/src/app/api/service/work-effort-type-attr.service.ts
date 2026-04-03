import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from 'app/commons/service/client.service';
import { HttpErrorResponse } from '@angular/common/http';
import { WorkEffortTypeAttr } from '../model/workEffortTypeAttr';


/**
 *  @author Leonardo Minaudo
 */
@Injectable()
export class WorkEffortTypeAttrService {

    constructor(private client: ApiClientService) { }

    getWorkEffortTypeAttrList(workEffortId: string): Observable<WorkEffortTypeAttr[]> {
        return this.client
            .get(`work-effort-type-attr/${workEffortId}`).pipe(
                map(json => json.results as WorkEffortTypeAttr[])
            );
    }

    getWorkEffortTypeAttrListAll(): Observable<WorkEffortTypeAttr[]> {
        return this.client
            .get(`work-effort-type-attr`).pipe(
                map(json => json.results as WorkEffortTypeAttr[])
            );
    }


}
