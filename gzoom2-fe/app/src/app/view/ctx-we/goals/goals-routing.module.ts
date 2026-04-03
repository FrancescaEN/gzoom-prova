import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { canDeactivateUnsavedGuard } from "app/shared/can-deactivate.guard";
import { GoalsComponent } from "./goals.component";
import { workEffortExResolverService } from "app/api/resolver/work-effort-ex-resolver.service";
import { DetailsGoalsComponent } from "./details-goals/details-goals.component";
import { workEffortByIdResolverService } from "app/api/resolver/work-effort-by-id-resolver.service";
import { DetailsDataHistoryComponent } from "./details-data-history/details-data-history.component";
import { workEffortStatusExByWorkEffortIdResolverService } from "app/api/resolver/work-effort-status-ex-by-work-effort-id-resolver.service";
import { OverDetailsGoalsComponent } from "./over-details-goals/over-details-goals.component";

const routes: Routes = [
  {
    path: "",
    component: GoalsComponent,
    canDeactivate: [canDeactivateUnsavedGuard],
  },
  {
    path: ':id', component: OverDetailsGoalsComponent, children: [{
      path: "DO",
      component: DetailsGoalsComponent,
      canDeactivate: [canDeactivateUnsavedGuard],
      resolve: {
        obss: workEffortByIdResolverService,
      },
      data: { breadcrumb: 'Details objective' }
    },
    {
      path: "SD",
      component: DetailsDataHistoryComponent,
      canDeactivate: [canDeactivateUnsavedGuard],
      resolve: {
        obss: workEffortStatusExByWorkEffortIdResolverService,
      },
      data: { breadcrumb: 'Data History' }
    }]
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class GoalsRoutingModule { }
