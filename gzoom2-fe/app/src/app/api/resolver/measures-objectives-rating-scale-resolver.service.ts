import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortMeasRatScService } from '../service/work-effort-meas-rat-sc.service';
import { WorkEffortMeasRatScExUomRatingScale } from '../model/workEffortMeasRatScExUomRatingScale';

export const measuresObjectivesRatingScaleResolver: ResolveFn<void | WorkEffortMeasRatScExUomRatingScale[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | WorkEffortMeasRatScExUomRatingScale[] | Observable<void | WorkEffortMeasRatScExUomRatingScale[]> | Promise<void | WorkEffortMeasRatScExUomRatingScale[]> => {

    console.log('resolve MeasuresObjectives');

    const wemrsService = inject(WorkEffortMeasRatScService);

    const lockoutService = inject(LockoutService);

    let id = route.parent.paramMap.get('workEffortMeasureId');
    const obs$ = wemrsService.getRatingScaleWEM(id);

    return lastValueFrom(obs$).then(x => { return x; })

        .catch(err => {

            console.error('Cannot retrieve MeasuresObjectives', err);

            lockoutService.lockout();

        });
}
