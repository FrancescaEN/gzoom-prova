import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { QrtzJobDetails } from '../model/qrtzJobDetails';
import { PlannerService } from '../service/scheduler/planner.service';

export const qrtzJobDetailsResolver: ResolveFn<void | QrtzJobDetails[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | QrtzJobDetails[]> => {
    const plannerService = inject(PlannerService);
    const lockoutService = inject(LockoutService);


    const obs$ = plannerService.getJobDetails();
    return lastValueFrom(obs$).then(obs => { return obs; })
        .catch(err => {
            console.error('Cannot retrieve workEffortContentType', err);
            lockoutService.lockout();
        });
}