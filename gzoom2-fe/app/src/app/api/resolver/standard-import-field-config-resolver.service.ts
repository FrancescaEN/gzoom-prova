import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { StandardImportFieldConfigEx } from '../model/standardImportFieldConfigEx';
import { StandardImportFieldConfigService } from '../service/standard-import-field-config.service';

export const StandardImportFieldConfigExResolver: ResolveFn<void | StandardImportFieldConfigEx[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | StandardImportFieldConfigEx[] | Observable<void | StandardImportFieldConfigEx[]> | Promise<void | StandardImportFieldConfigEx[]> => {

    console.log('resolve Subsystem-details');

    const standardImportFieldConfigService = inject(StandardImportFieldConfigService);

    const lockoutService = inject(LockoutService);

    var dataSourceId = route.paramMap.get('dataSourceId');

    const obss$ = standardImportFieldConfigService.getStandardImportFieldConfigEx(dataSourceId);

    return lastValueFrom(obss$).then(obss => { return obss; })

        .catch(err => {

            console.error('Cannot retrieve standardImportFieldConfigEx', err);

            lockoutService.lockout();

        });

}