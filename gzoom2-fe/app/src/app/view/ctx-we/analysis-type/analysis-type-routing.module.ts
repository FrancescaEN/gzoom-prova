import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { AnalysisTypeComponent } from './analysis-type.component';
import { workeEffortAnalysisExByIdResolver, workeEffortAnalysisExResolver } from 'app/api/resolver/work-effort-analysis-resolver.service';
import { AnalysisTypeDetailComponent } from './analysis-type-detail/analysis-type-detail.component';
import { canActivateCheckPermissionItemGuard } from 'app/commons/service/guard.service';

export const routeAnalysisType: Route =
{
    path: 'analysis-type',
    loadChildren: () =>
        import('./analysis-type.module'),
    data: { breadcrumb: 'analysis-type' },
    canActivate: [canActivateCheckPermissionItemGuard],
};

const routes: Routes = [
    { path: '', component: AnalysisTypeComponent, resolve: { obss: workeEffortAnalysisExResolver }, canDeactivate: [canDeactivateUnsavedGuard] },
    { path: 'add/new', component: AnalysisTypeDetailComponent, canDeactivate: [canDeactivateUnsavedGuard], data: { breadcrumb: 'New' }, },
    { path: ':workEffortAnalysisId', component: AnalysisTypeDetailComponent, resolve: { obss: workeEffortAnalysisExByIdResolver }, canDeactivate: [canDeactivateUnsavedGuard], data: { breadcrumb: 'Detail' }, },

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AnalysisTypeRoutingModule { }
