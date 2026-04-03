import { inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';

import { lastValueFrom, Observable } from 'rxjs';

import { LockoutService } from '../commons/service/lockout.service';
import { UserPermissionService } from './user-permission.service';

export const permissionsResolver: ResolveFn<void | boolean> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<void | boolean> => {

  const userPermissionService = inject(UserPermissionService);
  const lockoutService = inject(LockoutService);

  return lastValueFrom(userPermissionService.init()).then((perm) => {
    return true;
  })
    .catch(err => {
      console.error('An error occurred while loading account permissions', err);
      lockoutService.lockout();
    });

}