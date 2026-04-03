import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { TargetComponent } from './target/target.component';
import { workeEffortAnalysisTargetResolver } from '../../api/resolver/work-effort-analysis-target-resolver.service';
import { workeEffortAnalysisByContextResolver } from '../../api/resolver/work-effort-analysis-resolver.service';
import { AnalysisListComponent } from './analysis-list/analysis-list.component';
import { canActivateCheckPermissionItemGuard } from 'app/commons/service/guard.service';

export const routeAnalysis: Route = {
  path: 'analysis',
  loadChildren: () =>
    import('./analysis.module'),
  data: { breadcrumb: 'Analysis' },
  canActivate: [canActivateCheckPermissionItemGuard],
};

const routes: Routes = [

  { path: ':analysisId', component: TargetComponent, data: { breadcrumb: ':analysisId' }, resolve: { analyses: workeEffortAnalysisTargetResolver } },
  {
    path: '',
    component: AnalysisListComponent,
    resolve: { analyses: workeEffortAnalysisByContextResolver },
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AnalysisRoutingModule { }
