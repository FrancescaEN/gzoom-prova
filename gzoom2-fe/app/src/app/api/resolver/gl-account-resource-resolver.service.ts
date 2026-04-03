import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { GlAccountResourceService } from '../service/gl-account-resource.service';
import { GlAccountResource } from '../model/glAccountResource';


export const glAccountResourceResolver: ResolveFn<void | GlAccountResource[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | GlAccountResource[]> => {

    const glAccountResourceService = inject(GlAccountResourceService);
    const lockoutService = inject(LockoutService);

    const glAccountResourceService$ = glAccountResourceService.getGlAccountResourceList(route.parent.paramMap.get('glAccountTypeId'));
    return lastValueFrom(glAccountResourceService$).then(glAccountResource => { return glAccountResource; })
        .catch(err => {
            console.error('Cannot retrieve gl account type', err);
            lockoutService.lockout();
        });
}
