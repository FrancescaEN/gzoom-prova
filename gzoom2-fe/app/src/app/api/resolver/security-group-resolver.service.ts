import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { SecurityGroup } from '../model/securityGroup';
import { SecurityGroupService } from '../service/security-group.service';

export const securityGroupResolver: ResolveFn<void | SecurityGroup[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | SecurityGroup[]> => {

    const securityGroupService = inject(SecurityGroupService);
    const lockoutService = inject(LockoutService);

    const obs$ = securityGroupService.getSecurityGroupList();
    return lastValueFrom(obs$).then(x => { return x; })
        .catch(err => {
            console.error(err);
            lockoutService.lockout();
        });
}