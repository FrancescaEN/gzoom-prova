import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { Observable, catchError, lastValueFrom, of } from 'rxjs';
import { LockoutService } from '../../../commons/service/lockout.service';
import { TimesheetService } from '../../../api/service/timesheet.service';
import { Timesheet } from '../../../api/model/timesheet';
import { AuthService, UserProfile } from 'app/commons/service/auth.service';
import { LoaderService } from 'app/shared/loader/loader.service';
import { Filter, InfoPage } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';
import { LanguageService } from 'app/api/service/language.service';

export const TimesheetResolver: ResolveFn<void | Timesheet[]> = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
): void | Timesheet[] | Observable<void | Timesheet[]> | Promise<void | Timesheet[]> => {

  const timesheetService: TimesheetService = inject(TimesheetService)
  const loaderService: LoaderService = inject(LoaderService)
  const lockoutService = inject(LockoutService);
  const languageService: LanguageService = inject(LanguageService)

  const limit = route.queryParamMap.get('limit') ?? 50;
  const offset = route.queryParamMap.get('offset') ?? 0;
  const filters: Filter[] = [];
  // const secondaryLang = languageService.secondaryLang();
  filters.push({ field: "updatable", value: 'Y', matchMode: null })
  const infoCurrentPage: InfoPage = { offset: Number(offset), limit: Number(limit), secondaryLang: false };
  infoCurrentPage.filter = filters;

  console.log('resolve params of timesheets');
  if (!route.params.id) {
    loaderService.show();
  }

  const timesheetService$ = timesheetService.timesheetsPagination(infoCurrentPage);
  return lastValueFrom(timesheetService$).then(timesheets => {
    return timesheets;
  })
    .catch(err => {
      console.error('Cannot retrieve timesheets', err);
      lockoutService.lockout();
    })
    .finally(() => {
      loaderService.hide();
    });

}


