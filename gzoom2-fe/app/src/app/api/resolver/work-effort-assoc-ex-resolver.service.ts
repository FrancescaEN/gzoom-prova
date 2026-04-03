import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortAssocEx } from 'app/api/model/workEffortAssocEx';
import { WorkEffortAssocService } from 'app/api/service/work-effort-assoc.service';
import { LoaderService } from 'app/shared/loader/loader.service';
import { InfoPage } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';

export const workEffortAssocExResolver: ResolveFn<void | WorkEffortAssocEx[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | WorkEffortAssocEx[] | Observable<void | WorkEffortAssocEx[]> | Promise<void | WorkEffortAssocEx[]> => {
    console.log('resolve WorkEffortAssoc');
    const workEffortAssocService = inject(WorkEffortAssocService);
    const lockoutService = inject(LockoutService);
    const loaderService = inject(LoaderService);


    const limit = route.queryParamMap.get('limit') ?? 50;
    const offset = route.queryParamMap.get('offset') ?? 0;
    let filters = [];

    if (!!route.queryParamMap.get('code')) {
        if (route.queryParamMap.get('code') == "ROO") {
            filters.push({ field: "workEffortIdFrom", value: route.queryParamMap.get('id') })
        } else if (route.queryParamMap.get('code') == "RDO") {
            filters.push({ field: "workEffortIdTo", value: route.queryParamMap.get('id') })
        }
    }



    const infoCurrentPage: InfoPage = { offset: Number(offset), limit: Number(limit), secondaryLang: false, filter: filters };

    loaderService.show();
    const weat$ = workEffortAssocService.getWorkEffortAssocPagination(infoCurrentPage);
    return lastValueFrom(weat$).then(weats => {
        return weats;
    })
        .catch(err => {
            console.error('Cannot retrieve workEffortAssoc', err);
            lockoutService.lockout();
        })
        .finally(() => {
            loaderService.hide();
        });
}