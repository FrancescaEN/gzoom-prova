import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';


import { reportPrintResolver } from './report-print/report-print-resolver.service';
import { reportResolver } from './report/report-resolver.service';
import { ReportPrintComponent } from './report-print/report-print.component';
import { ReportComponent } from './report/report.component';

import { orgUnitResolver } from '../../api/resolver/org-unit-resolver.service';
import { statusItemResolverService } from '../../api/resolver/status-item-resolver.service';
import { roleTypeResolverService } from '../../api/resolver/role-type-resolver.service';
import { canActivateCheckPermissionItemGuard } from 'app/commons/service/guard.service';

export const routeReportPrint: Route =
{
  path: 'report-print',
  loadChildren: () =>
    import('./report-print.module'),
  data: { breadcrumb: 'report-print' },
  canActivate: [canActivateCheckPermissionItemGuard],

};

const routes: Routes = [
  {
    path: '', component: ReportPrintComponent, resolve: { reports: reportPrintResolver },
    children: [
      {
        path: ':reportContentId/:resourceName/:workEffortTypeId/:analysis',
        component: ReportComponent,
        resolve: {
          report: reportResolver,
          orgUnits: orgUnitResolver,
          statusItems: statusItemResolverService,
          roleTypes: roleTypeResolverService,
          //workEfforts: WorkEffortResolverService
        }
        //  children: [
        //    { path: ':workEffortTypeId', component: ReportWorkefforttypeComponent, resolve: { workEfforts: ReportWorkefforttypeResolverService }}
        //  ]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReportPrintRoutingModule { }
