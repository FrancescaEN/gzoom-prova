import { Injectable } from '@angular/core';
import { ApiClientService } from '../../commons/service/client.service';
import { Observable, map } from 'rxjs';
import { JobLogEx } from '../model/jobLogEx';
import { JobLog } from '../model/jobLog';
import { InfoPage } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';


@Injectable()
export class JobLogService {

    constructor(private client: ApiClientService) { }

    /**
    * Gets the list of job_log_ex.
    * 
    * @returns Observable<JobLogEx[]>
    */
    getJobLogEx(infoPage: InfoPage): Observable<JobLogEx[]> {
        return this.client
            .post(`job-log/pagination`, infoPage).pipe(
                map(json => json.results as JobLogEx[])
            );
    }

    /**
    * Gets the list of job_log.
    * 
    * @returns Observable<JobLog>
    */
    getJobLogById(jobLogId): Observable<JobLog> {
        return this.client
            .get(`job-log/${jobLogId}`).pipe(
                map(json => json as JobLog)
            );
    }
}
