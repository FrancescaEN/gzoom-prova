import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { RoleTypeService } from '../service/role-type.service';
import { RoleType } from '../model/role-type';

export const roleTypeResolverService: ResolveFn<void | RoleType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | RoleType[] | Observable<void | RoleType[]> | Promise<void | RoleType[]> => {

  console.log('resolve Role Type');

  const roleTypeService = inject(RoleTypeService);

  const lockoutService = inject(LockoutService);


  const roleTypeService$ = roleTypeService.roleTypes();

  return lastValueFrom(roleTypeService$).then(roleTypes => { return roleTypes; })

    .catch(err => {

      console.error('Cannot retrieve roleType', err);

      lockoutService.lockout();

    });

}
