import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { GlAccountType } from '../model/glAccountType';
import { GlAccountTypeService } from '../service/gl-account-type.service';


export const glAccountTypeResolver: ResolveFn<void | GlAccountType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | GlAccountType[]> => {

    const glAccountTypeService = inject(GlAccountTypeService);
    const lockoutService = inject(LockoutService);

    const glAccountTypeService$ = glAccountTypeService.getGlAccountTypeList(route.paramMap.get('accountTypeEnumId'));
    return lastValueFrom(glAccountTypeService$).then(glAccountType => { return glAccountType; })
        .catch(err => {
            console.error('Cannot retrieve gl account type', err);
            lockoutService.lockout();
        });
}
