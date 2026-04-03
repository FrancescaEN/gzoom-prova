import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from "@angular/router";
import { StatusType } from "../model/statusType";
import { inject } from "@angular/core";
import { LockoutService } from "app/commons/service/lockout.service";
import { StatusTypeService } from "../service/status-type.service";
import { lastValueFrom } from "rxjs";

export const statusTypeResolver: ResolveFn<void | StatusType[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | StatusType[]> => {
    const statusTypeService = inject(StatusTypeService);
    const lockoutService = inject(LockoutService);

    const obs$ = statusTypeService.getStatusTypeList()
    return lastValueFrom(obs$).then(x => { return x; })
        .catch(err => { // TODO serve il lockout?
            console.error('Cannot retrieve Status Type', err);
            lockoutService.lockout();
        });
}