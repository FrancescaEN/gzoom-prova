import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { EmplPositionType } from 'app/api/model/emplPositionType';
import { EmplPositionTypeService } from 'app/api/service/empl-position-type.service';
/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */

export const emplPositionTypeResolver: ResolveFn<void | EmplPositionType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | EmplPositionType[] | Observable<void | EmplPositionType[]> | Promise<void | EmplPositionType[]> => {

    console.log('resolve EmplPositionTypes');

    const emplPositionTypeService = inject(EmplPositionTypeService);

    const lockoutService = inject(LockoutService);


    const epts$ = emplPositionTypeService.emplPositionTypes();

    return lastValueFrom(epts$).then(emplPositionTypes => { return emplPositionTypes; })

        .catch(err => {

            console.error('Cannot retrieve EmplPositionTypes', err);

            lockoutService.lockout();

        });

}
