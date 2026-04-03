import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { DataSourceEx } from '../model/dataSourceEx';
import { DataSourceService } from '../service/data-source.service';

export const dataSourceExResolver: ResolveFn<void | DataSourceEx[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | DataSourceEx[] | Observable<void | DataSourceEx[]> | Promise<void | DataSourceEx[]> => {

    console.log('resolve Subsystem');

    const dataSourceService = inject(DataSourceService);

    const lockoutService = inject(LockoutService);

    const obss$ = dataSourceService.getDataSourceEx();

    return lastValueFrom(obss$).then(obss => { return obss; })

        .catch(err => {

            console.error('Cannot retrieve dataSourceEx', err);

            lockoutService.lockout();

        });

}