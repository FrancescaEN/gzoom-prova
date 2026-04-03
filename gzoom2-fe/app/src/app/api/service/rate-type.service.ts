import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from '../../commons/service/client.service';
import { RateType } from '../model/rateType';
import { WorkEffortAssignmentRate } from '../model/workEffortAssignmentRate';

@Injectable()
export class RateTypeService {

    constructor(private client: ApiClientService) { }

    rateTypes(): Promise<RateType[]> {

        const client$ = this.client.get(`rate-types/`).pipe(map(json => json.results as RateType[]));
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });
    }

    rateTypesWorkEffortId(workEffortId: string): Observable<WorkEffortAssignmentRate[]> {
        return this.client
            .get(`rate-types/rate-types-work-effort-id/${workEffortId}`).pipe(
                map(json => json.results as WorkEffortAssignmentRate[])
            );
    }
}
