import { inject } from "@angular/core";
import {
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  ResolveFn,
} from "@angular/router";
import { lastValueFrom, Observable } from "rxjs";
import { LockoutService } from "../../commons/service/lockout.service";
import { WorkEffortPartyAssignmentService } from "app/api/service/work-effort-party-assignment.service";
import { WorkEffortPartyAssignment } from "../model/workEffortPartyAssignment";
import { LoaderService } from "app/shared/loader/loader.service";
import { InfoPage } from "app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration";

export const workEffortPartyAssignmentExResolver: ResolveFn<void | WorkEffortPartyAssignment[]> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
  | void
  | WorkEffortPartyAssignment[]
  | Observable<void | WorkEffortPartyAssignment[]>
  | Promise<void | WorkEffortPartyAssignment[]> => {

  const workEffortPartyAssignmentService = inject(WorkEffortPartyAssignmentService);
  const lockoutService = inject(LockoutService);
  const loaderService = inject(LoaderService);

  console.log("resolve Party Assignment");
  loaderService.show();

  const limit = route.queryParamMap.get('limit') ?? 0;
  const offset = route.queryParamMap.get('offset') ?? 0;
  let filters = [];
  const infoCurrentPage: InfoPage = { offset: Number(offset), limit: Number(limit), secondaryLang: false, filter: filters };

  const obs$ = workEffortPartyAssignmentService.getWorkEffortPartyAssignmentListFilter(infoCurrentPage);
  return lastValueFrom(obs$)
    .then((obs) => {
      return obs;
    })
    .catch((err) => {
      console.error("Cannot retrieve Party Assignment Ex", err);
      lockoutService.lockout();
    })
    .finally(() => {
      loaderService.hide();
    });
}
