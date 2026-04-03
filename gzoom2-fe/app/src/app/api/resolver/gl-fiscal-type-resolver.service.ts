import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import { GlFiscalType } from '../model/glFiscalType';

export const glFiscalTypeResolver: ResolveFn<void | GlFiscalType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | GlFiscalType[] | Observable<void | GlFiscalType[]> | Promise<void | GlFiscalType[]> => {

    console.log('resolve DetectionType');

    const glAccountTypeService = inject(GlFiscalTypeService);

    const lockoutService = inject(LockoutService);


    const obs$ = glAccountTypeService.getGlFiscalType();

    return lastValueFrom(obs$).then(obss => { return obss; })

        .catch(err => {

            console.error('Cannot retrieve DetectionType', err);

            lockoutService.lockout();

        });

}
