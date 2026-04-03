import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { GlAccountTypeGlFiscalTypeService } from '../service/gl-account-type-gl-fiscal-type.service';
import { GlAccountTypeGlFiscalType } from '../model/glAccoutTypeGlFiscalType';


export const glAccountTypeGlFiscalTypeResolver: ResolveFn<void | GlAccountTypeGlFiscalType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | GlAccountTypeGlFiscalType[]> => {

    const glAccountTypeGlFiscalTypeService = inject(GlAccountTypeGlFiscalTypeService);
    const lockoutService = inject(LockoutService);

    const glAccountTypeGlFiscalTypeService$ = glAccountTypeGlFiscalTypeService.getGlAccountTypeGlFiscalTypeList(route.parent.paramMap.get('glAccountTypeId'));
    return lastValueFrom(glAccountTypeGlFiscalTypeService$).then(glAccountTypeGlFiscalType => { return glAccountTypeGlFiscalType; })
        .catch(err => {
            console.error('Cannot retrieve gl account type gl fiscal type', err);
            lockoutService.lockout();
        });
}
