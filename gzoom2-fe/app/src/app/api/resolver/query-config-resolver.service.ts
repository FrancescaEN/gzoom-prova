import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { QueryConfig } from '../model/queryConfig';
import { QueryConfigService } from '../service/query-config.service';
import { Context } from 'app/commons/enum/context';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */

export const queryConfigResolver: ResolveFn<void | QueryConfig[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | QueryConfig[] | Observable<void | QueryConfig[]> | Promise<void | QueryConfig[]> => {

  const parentTypeId = route.parent.data.context;
  console.log(parentTypeId);
  const queryType = route.params.id;

  console.log('resolve query cofig' + parentTypeId + queryType);

  const queryConfigService = inject(QueryConfigService);
  const lockoutService = inject(LockoutService);


  const queryConfigService$ = queryConfigService.queryConfigs(parentTypeId === Context.CTX_WE ? null : parentTypeId, queryType);

  return lastValueFrom(queryConfigService$).then(queryConfigs => { return queryConfigs; })

    .catch(err => {

      console.error('Cannot retrieve query config', err);

      lockoutService.lockout();

    });

}

export const queryConfigIdResolver: ResolveFn<void | QueryConfig> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | QueryConfig | Observable<void | QueryConfig> | Promise<void | QueryConfig> => {

  console.log('resolve query cofig');
  const id = route.parent.params.id;

  const queryConfigService = inject(QueryConfigService);

  const lockoutService = inject(LockoutService);


  const queryConfigService$ = queryConfigService.getQueryConfig(id);

  return lastValueFrom(queryConfigService$).then(queryConfig => { console.log("resolver id " + queryConfig); return queryConfig; })

    .catch(err => {

      console.error('Cannot retrieve query config', err);

      lockoutService.lockout();

    });

}

export const queryConfigurationResolver: ResolveFn<void | QueryConfig[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | QueryConfig[] | Observable<void | QueryConfig[]> | Promise<void | QueryConfig[]> => {

  const queryType = route.params.queryType;

  const queryConfigService = inject(QueryConfigService);
  const lockoutService = inject(LockoutService);

  const queryConfigService$ = queryConfigService.getQueryConfigList(queryType);

  return lastValueFrom(queryConfigService$).then(queryConfigs => { return queryConfigs; })
    .catch(err => {
      console.error('Cannot retrieve query config', err);
      lockoutService.lockout();
    });

}
