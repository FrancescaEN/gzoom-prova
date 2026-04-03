import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { SecurityGroupPermission } from '../model/securityGroupPermission';
import { SecurityGroupPermissionService } from '../service/security-group-permission.service';

export const securityGroupPermissionResolver: ResolveFn<void | SecurityGroupPermission[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | SecurityGroupPermission[]> => {

    const securityGroupPermissionService = inject(SecurityGroupPermissionService);
    const lockoutService = inject(LockoutService);

    let groupId = route.parent.paramMap.get('groupId');

    const obs$ = securityGroupPermissionService.getSecurityGroupPermissionByGroupId(groupId);
    return lastValueFrom(obs$).then(x => { return x; })
        .catch(err => {
            console.error(err);
            lockoutService.lockout();
        });
}