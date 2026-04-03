import { HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { QrtzJobDetails } from "app/api/model/qrtzJobDetails";
import { QrtzTriggers } from "app/api/model/qrtzTriggers";
import { Frequency, JobData, ServiceJob } from "app/api/model/scheduler";
import { ApiClientService } from "app/commons/service/client.service";
import { Observable, lastValueFrom, map, tap } from "rxjs";

@Injectable()
export class PlannerService {
    constructor(private client: ApiClientService) { }

    getTriggers(): Observable<QrtzTriggers[]> {
        return this.client
            .get(`planner/triggers`).pipe(
                map(json => json.results as QrtzTriggers[])
            );
    }

    getServiceList(): Observable<ServiceJob> {
        return this.client
            .get(`planner/services`).pipe(
                map(json => json as ServiceJob)
            );
    }

    getJobDetails(): Observable<QrtzJobDetails[]> {
        return this.client
            .get(`planner/job-details`).pipe(
                map(json => json.results as QrtzJobDetails[])
            );
    }

    getJobData(jobName: string): Observable<JobData> {
        return this.client
            .get(`planner/job-data/${jobName}`).pipe(
                tap((a) => console.log(a)),
                map(json => json as JobData)
                //  map(jobData => Object.values(jobData))
            );
    }

    updateDescription(jobName: string, description: string): Promise<any> {
        const client$ = this.client.put(`planner/set-description/${jobName}`, JSON.stringify(description));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)

            })
    }

    async updateTrigger(jobDetails: QrtzJobDetails, callbackObject: JobData, startDate: Date, cronExpression: string, endDate: Date, frequency: Frequency) {
        let body = {
            jobDetails: jobDetails,
            callbackObject: callbackObject,
            startDate: startDate,
            endDate: endDate,
            frequency: frequency.toString(),
            cronExpression: cronExpression
        }
        const client$ = this.client.post(`planner/update/trigger`, JSON.stringify(body));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    }

    async createJob(jobDetails: QrtzJobDetails, callbackObject: JobData, startDate: Date, cronExpression: string, endDate: Date, frequency: Frequency) {
        let body = {
            jobDetails: jobDetails,
            callbackObject: callbackObject,
            startDate: startDate,
            endDate: endDate,
            frequency: frequency.toString(),
            cronExpression: cronExpression
        }
        const client$ = this.client.post(`planner/create/job`, JSON.stringify(body));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    }

    deleteJob(id: string): Promise<any> {
        const client$ = this.client.delete(`planner/${id}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });

    }
}