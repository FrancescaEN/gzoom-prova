import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortPurposeTypeService } from 'app/api/service/work-effort-purpose-type.service';
import { WorkEffortPurposeType } from 'app/api/model/workEffortPurposeType';

export const workEffortPurposeResolver: ResolveFn<void | WorkEffortPurposeType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | WorkEffortPurposeType[] | Observable<void | WorkEffortPurposeType[]> | Promise<void | WorkEffortPurposeType[]> => {

    const workEffortPurposeTypeService = inject(WorkEffortPurposeTypeService);
    const lockoutService = inject(LockoutService);

    console.log('resolve Purpose');

    const obs$ = workEffortPurposeTypeService.getWorkEffortPurposeType();
    return lastValueFrom(obs$).then(obs => { return obs; })
        .catch(err => {
            console.error('Cannot retrieve Purpose', err);
            lockoutService.lockout();
        });
}