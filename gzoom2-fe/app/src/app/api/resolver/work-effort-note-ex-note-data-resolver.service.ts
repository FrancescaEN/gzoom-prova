import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortNoteService } from '../service/work-effort-note.service';
import { WorkEffortNoteExNoteData } from '../model/workEffortNoteExNoteData';
import { LoaderService } from 'app/shared/loader/loader.service';
import { InfoPage } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';


export const workEffortNoteExNoteDataResolver: ResolveFn<void | WorkEffortNoteExNoteData[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffortNoteExNoteData[]> => {

    const workEffortNoteService = inject(WorkEffortNoteService);
    const lockoutService = inject(LockoutService);
    const loaderService = inject(LoaderService);

    const limit = route.queryParamMap.get('limit') ?? 50;
    const offset = route.queryParamMap.get('offset') ?? 0;
    let filters = [];

    if (!!route.queryParamMap.get('code')) {
        if (route.queryParamMap.get('code') == "TN") {
            filters.push({ field: "workEffortId", value: route.queryParamMap.get('id') })
        }
    }


    const infoCurrentPage: InfoPage = { offset: Number(offset), limit: Number(limit), secondaryLang: false, filter: filters };

    loaderService.show();
    const obs$ = workEffortNoteService.getWorkEffortNoteExNoteDataListPagination(infoCurrentPage);
    return lastValueFrom(obs$).then(obs => { return obs; })
        .catch(err => {
            console.error('Cannot retrieve WorkEffortNoteExNoteData', err);
            lockoutService.lockout();
        });
}