import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { ApprovalPathsComponent } from './approval-paths.component';
import { PassesAllowedComponent } from './passes-allowed/passes-allowed.component';
import { PredictedStatesComponent } from './predicted-states/predicted-states.component';
import { statusTypeResolver } from 'app/api/resolver/status-type-resolver.service';
import { statusItemResolver1Service } from 'app/api/resolver/status-item-resolver.service';
import { statusValidChangeResolverService } from 'app/api/resolver/status-valid-change-resolver.service';
import { OverDetailsApprovalPathsComponent } from './over-details-approval-paths/over-details-approval-paths.component';

const routes: Routes = [
    {
        path: '',
        component: ApprovalPathsComponent,
        resolve: { obss: statusTypeResolver },
        canDeactivate: [canDeactivateUnsavedGuard],
    },
    {
        path: ':statusTypeId', component: OverDetailsApprovalPathsComponent, children: [ {
             path: 'passes-allowed', component: PassesAllowedComponent, resolve: { obss: statusValidChangeResolverService }, data: { breadcrumb: 'Passes Allowed' }, canDeactivate: [canDeactivateUnsavedGuard] },
        { path: 'predicted-states', component: PredictedStatesComponent, resolve: { obss: statusItemResolver1Service }, data: { breadcrumb: 'Predicted States' }, canDeactivate: [canDeactivateUnsavedGuard] }]}
   

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ApprovalPathsRoutingModule { }
