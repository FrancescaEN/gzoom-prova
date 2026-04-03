import { Injectable } from '@angular/core';
import { ApiClientService } from '../../commons/service/client.service';
import { Observable, map } from 'rxjs';
import { JobLogEx } from '../model/jobLogEx';
import { JobLogJobExecParams } from '../model/jobLogJobExecParams';


@Injectable()
export class JobLogJobExecParamsService {

    constructor(private client: ApiClientService) { }

    /**
    * Gets the list of job_log_job_exec_params.
    * 
    * @returns Observable<JobLogJobExecParams[]>
    */
    getJobLogJobExecParams(jobLogId): Observable<JobLogJobExecParams[]> {
        return this.client
            .get(`job-log-job-exec-params/${jobLogId}`).pipe(
                map(json => json.results as JobLogJobExecParams[])
            );
    }
}
