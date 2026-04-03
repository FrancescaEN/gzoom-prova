import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { SecurityGroupContent } from '../model/securityGroupContent';
import { SecurityGroupContentService } from '../service/security-group-content.service';

export const securityGroupContentResolver: ResolveFn<void | SecurityGroupContent[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | SecurityGroupContent[]> => {

    const securityGroupContentService = inject(SecurityGroupContentService);
    const lockoutService = inject(LockoutService);

    let groupId = route.parent.paramMap.get('groupId');

    const obs$ = securityGroupContentService.getSecurityGroupContentByGroupId(groupId);
    return lastValueFrom(obs$).then(x => { return x; })
        .catch(err => {
            console.error(err);
            lockoutService.lockout();
        });
}