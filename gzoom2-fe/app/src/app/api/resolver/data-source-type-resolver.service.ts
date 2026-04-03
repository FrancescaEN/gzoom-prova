import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { DataSourceType } from 'app/api/model/dataSourceType';
import { DataSourceTypeService } from 'app/api/service/dataSourceType.service';

export const dataSourceTypeResolver: ResolveFn<void | DataSourceType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | DataSourceType[] | Observable<void | DataSourceType[]> | Promise<void | DataSourceType[]> => {

    console.log('resolve SubsystemTypes');

    const dataSourceTypeService = inject(DataSourceTypeService);

    const lockoutService = inject(LockoutService);


    const dst$ = dataSourceTypeService.getDataSourceType();

    return lastValueFrom(dst$).then(dsts => { return dsts; })

        .catch(err => {

            console.error('Cannot retrieve dataSourceType', err);

            lockoutService.lockout();

        });

}