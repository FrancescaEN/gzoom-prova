import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { StatusItemService } from '../service/status-item.service';
import { StatusItem } from '../model/statusItem';

export const statusItemResolverService: ResolveFn<void | StatusItem[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | StatusItem[] | Observable<void | StatusItem[]> | Promise<void | StatusItem[]> => {
  console.log('resolve Status Item');
  var parentTypeId = route.parent.data.context;
  const statusItemService = inject(StatusItemService);
  const lockoutService = inject(LockoutService);

  const statusItemService$ = statusItemService.statusItems(parentTypeId);

  return lastValueFrom(statusItemService$).then(statusItems => { return statusItems; })
    .catch(err => {
      console.error('Cannot retrieve Status Item', err);
      lockoutService.lockout();
    });

}

export const statusItemResolver1Service: ResolveFn<void | StatusItem[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | StatusItem[] | Observable<void | StatusItem[]> | Promise<void | StatusItem[]> => {
  const statusTypeId = route.parent.paramMap.get('statusTypeId');
  const statusItemService = inject(StatusItemService);
  const lockoutService = inject(LockoutService);

  const statusItemService$ = statusItemService.getStatusItemList(statusTypeId);
  return lastValueFrom(statusItemService$).then(statusItems => { return statusItems; })
    .catch(err => {
      console.error('Cannot retrieve Status Item', err);
      lockoutService.lockout();
    });
}