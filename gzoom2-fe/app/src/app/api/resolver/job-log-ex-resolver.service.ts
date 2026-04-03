import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { JobLogEx } from '../model/jobLogEx';
import { JobLogService } from '../service/job-log.service';
import { LoaderService } from 'app/shared/loader/loader.service';
import { InfoPage } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';

export const jobLogExResolver: ResolveFn<void | JobLogEx[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | JobLogEx[] | Observable<void | JobLogEx[]> | Promise<void | JobLogEx[]> => {

    console.log('resolve consultingLog');

    const jobLogService = inject(JobLogService);
    const lockoutService = inject(LockoutService);
    const loaderService = inject(LoaderService);

    const limit = route.queryParamMap.get('limit') ?? 50;
    const offset = route.queryParamMap.get('offset') ?? 0;
    let filters = [];

    const infoCurrentPage: InfoPage = { offset: Number(offset), limit: Number(limit), secondaryLang: false, filter: filters };

    loaderService.show();

    const obss$ = jobLogService.getJobLogEx(infoCurrentPage);

    return lastValueFrom(obss$).then(obss => { return obss; })

        .catch(err => {

            console.error('Cannot retrieve JobLogEx', err);

            lockoutService.lockout();

        });

}