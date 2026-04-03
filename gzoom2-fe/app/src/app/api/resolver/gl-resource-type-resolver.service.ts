import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { GlResourceTypeService } from 'app/api/service/gl-resource-type.service';
import { GlResourceType } from 'app/api/model/glResourceType';

export const glResourceTypeResolver: ResolveFn<void | GlResourceType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): void | GlResourceType[] | Observable<void | GlResourceType[]> | Promise<void | GlResourceType[]> => {

    console.log('resolve NatureUnitContExtr');

    const glResourceTypeService = inject(GlResourceTypeService);

    const lockoutService = inject(LockoutService);


    const obs$ = glResourceTypeService.getGlResourceType();

    return lastValueFrom(obs$).then(obs => { return obs; })

        .catch(err => {

            console.error('Cannot retrieve NatureUnitContExtr', err);

            lockoutService.lockout();

        });

}