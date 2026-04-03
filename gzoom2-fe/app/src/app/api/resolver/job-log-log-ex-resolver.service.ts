import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { JobLogLogEx } from '../model/jobLogLogEx';
import { JobLogLogService } from '../service/job-log-log.service';

export const jobLogLogExResolver: ResolveFn<void | JobLogLogEx[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | JobLogLogEx[] | Observable<void | JobLogLogEx[]> | Promise<void | JobLogLogEx[]> => {

    console.log('resolve JobLogLogEx');

    const jobLogLogService = inject(JobLogLogService);

    const lockoutService = inject(LockoutService);

    var jobLogId = route.parent.paramMap.get('jobLogId');

    const obss$ = jobLogLogService.getJobLogLogEx(jobLogId);

    return lastValueFrom(obss$).then(obss => { return obss; })

        .catch(err => {

            console.error('Cannot retrieve JobLogLogEx', err);

            lockoutService.lockout();

        });

}