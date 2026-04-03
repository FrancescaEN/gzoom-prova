import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortMeasureRatScViewService } from '../service/work-effort-measures-rat-sc-view.service';
import { WorkEffortMeasureRatScView } from '../model/workEffortMeasureRatScView';

export const workEffortMeasureRatScViewResolver: ResolveFn<void | WorkEffortMeasureRatScView[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffortMeasureRatScView[]> => {

    const workEffortMeasureRatScViewService = inject(WorkEffortMeasureRatScViewService);
    const lockoutService = inject(LockoutService);

    let id = route.paramMap.get('workEffortMeasureId');
    console.log('resolve MeasuresObjectives ' + id);

    const wemrsService$ = workEffortMeasureRatScViewService.getWorkEffortMeasureRatScView(id)
    return lastValueFrom(wemrsService$).then(x => { return x; })
        .catch(err => {
            console.error(err);
            lockoutService.lockout();
        });
}