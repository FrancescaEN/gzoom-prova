import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { JobLogJobExecParams } from '../model/jobLogJobExecParams';
import { JobLogJobExecParamsService } from '../service/job-log-job-exec-params.service';

export const jobLogJobExecParamsResolver: ResolveFn<void | JobLogJobExecParams[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | JobLogJobExecParams[] | Observable<void | JobLogJobExecParams[]> | Promise<void | JobLogJobExecParams[]> => {

    console.log('resolve jobLogJobExecParams');

    const jobLogJobExecParamsService = inject(JobLogJobExecParamsService);

    const lockoutService = inject(LockoutService);
    var jobLogId = route.parent.paramMap.get('jobLogId');

    const obss$ = jobLogJobExecParamsService.getJobLogJobExecParams(jobLogId);

    return lastValueFrom(obss$).then(obss => { return obss; })

        .catch(err => {

            console.error('Cannot retrieve JobLogEx', err);

            lockoutService.lockout();

        });

}