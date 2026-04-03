import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { UserLoginSecurityGroup } from '../model/userLoginSecurityGroup';
import { UserLoginSecurityGroupService } from '../service/user-login-security-group.service';

export const userLoginSecurityGroupResolver: ResolveFn<void | UserLoginSecurityGroup[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | UserLoginSecurityGroup[]> => {

    const userLoginSecurityGroupService = inject(UserLoginSecurityGroupService);
    const lockoutService = inject(LockoutService);

    let groupId = route.parent.paramMap.get('groupId');    

    const obs$ = userLoginSecurityGroupService.getUserLoginSecurityGroupByGroupId(groupId);
    return lastValueFrom(obs$).then(x => { return x; })
        .catch(err => {
            console.error(err);
            lockoutService.lockout();
        });
}