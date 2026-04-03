import { Injectable } from '@angular/core';
import { ApiClientService } from '../../commons/service/client.service';
import { Observable, map } from 'rxjs';
import { JobLogEx } from '../model/jobLogEx';
import { JobLogLogEx } from '../model/jobLogLogEx';


@Injectable()
export class JobLogLogService {

    constructor(private client: ApiClientService) { }

    /**
    * Gets the list of job_log_log_ex.
    * 
    * @returns Observable<JobLogLogEx[]>
    */
    getJobLogLogEx(jobLogId): Observable<JobLogLogEx[]> {
        return this.client
            .get(`job-log-log/${jobLogId}`).pipe(
                map(json => json.results as JobLogLogEx[])
            );
    }
}
