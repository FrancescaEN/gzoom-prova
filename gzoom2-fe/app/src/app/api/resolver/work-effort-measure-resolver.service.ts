import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortMeasure } from '../model/workEffortMeasure';
import { WorkEffortMeasureService } from '../service/work-effort-measure.service';

export const workEffortMeasureResolver: ResolveFn<void | WorkEffortMeasure> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | WorkEffortMeasure | Observable<void | WorkEffortMeasure> | Promise<void | WorkEffortMeasure> => {
    const workEffortMeasureService = inject(WorkEffortMeasureService);

    const lockoutService = inject(LockoutService);

    let id = route.parent.paramMap.get('workEffortMeasureId');
    const obs$ = workEffortMeasureService.getWorkEffortMeasure(id);

    return lastValueFrom(obs$).then(x => { return x; })

        .catch(err => {

            console.error('Cannot retrieve workEffortMeasure', err);

            lockoutService.lockout();

        });
}
