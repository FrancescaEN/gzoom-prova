import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortSequenceService } from 'app/api/service/work-effort-sequence.service';
import { WorkEffortSequence } from 'app/api/model/workEffortSequence';

export const workeEffortSequenceResolver: ResolveFn<void | WorkEffortSequence[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffortSequence[]> => {
    const workEffortSequenceService = inject(WorkEffortSequenceService);
    const lockoutService = inject(LockoutService);

    console.log('resolve ObjectiveCodes');

    const objCod$ = workEffortSequenceService.getWorkEffortSequence();
    return lastValueFrom(objCod$).then(objCod => { return objCod; })
        .catch(err => {
            console.error('Cannot retrieve ObjectiveCodes', err);
            lockoutService.lockout();
        });
}