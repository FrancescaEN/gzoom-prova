import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { EnumerationService } from '../service/enumeration.service';
import { Enumeration } from '../model/enumeration';

export const enumerationResolverService: ResolveFn<void | Enumeration[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | Enumeration[] | Observable<void | Enumeration[]> | Promise<void | Enumeration[]> => {

    console.log('resolve Enumerations');

    const enumerationService = inject(EnumerationService);

    const lockoutService = inject(LockoutService);


    const obs$ = enumerationService.enumerations('WE_PERIOD_TYPE');

    return lastValueFrom(obs$).then(enumerations => { return enumerations; })

        .catch(err => {

            console.error('Cannot retrieve Enumerations', err);

            lockoutService.lockout();

        });

}
