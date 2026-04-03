import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { LockoutService } from '../../commons/service/lockout.service';
import { WorkEffortAssocType } from 'app/api/model/workEffortAssocType';
import { WorkEffortAssocTypeService } from 'app/api/service/work-effort-assoc-type.service';

export const workEffortAssocTypeResolver: ResolveFn<void | WorkEffortAssocType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | WorkEffortAssocType[]> => {
    const workEffortAssocTypeService = inject(WorkEffortAssocTypeService);
    const lockoutService = inject(LockoutService);

    console.log('resolve TypologyRelationshipObjectives');

    const weat$ = workEffortAssocTypeService.getWorkEffortAssocType();
    return lastValueFrom(weat$).then(weats => { return weats; })
        .catch(err => {
            console.error('Cannot retrieve workEffortAssocType', err);
            lockoutService.lockout();
        });
}