import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, switchMap, } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortMeasureService } from 'app/api/service/work-effort-measure.service';
import { WorkEffortMeasExUom } from 'app/api/model/workEffortMeasExUom';
import { InfoPage } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';
import { LoaderService } from 'app/shared/loader/loader.service';
import { UserPreferenceService } from '../service/user-preference.service';

export const workEffortMeasExUomResolver: ResolveFn<void | WorkEffortMeasExUom[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffortMeasExUom[]> => {

    console.log('resolve Work Effort Measure');
    const workEffortMeasureService = inject(WorkEffortMeasureService);
    const lockoutService = inject(LockoutService);
    const loaderService = inject(LoaderService);
    const usrPreferenceService = inject(UserPreferenceService)

    const limit = route.queryParamMap.get('limit') ?? 50;
    const offset = route.queryParamMap.get('offset') ?? 0;

    let filters = [];

    if (!!route.queryParamMap.get('code')) {
        if (route.queryParamMap.get('code') == "MO") {
            filters.push({ field: "workEffortId", value: route.queryParamMap.get('id') })
        }
    }
    let infoCurrentPage: InfoPage;

    loaderService.show();

    const obs$ = usrPreferenceService.getUserPreference('ORGANIZATION_PARTY').pipe(
        switchMap((data) => {
            infoCurrentPage = { offset: Number(offset), limit: Number(limit), organizationId: data.userPrefValue, secondaryLang: false, filter: filters };
            return workEffortMeasureService.getWorkEffortMeasExUomListPagination(infoCurrentPage);
        }));

    return lastValueFrom(obs$).then(obs => { return obs; })
        .catch(err => {
            console.error('Cannot retrieve Work Effort Measure', err);
            lockoutService.lockout();
        }).finally(() => {
            loaderService.hide();
        });
}