import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { QueryConfigComponent } from './query-config/query-config.component';
import { queryConfigResolver } from '../../api/resolver/query-config-resolver.service';
import { queryConfigIdResolver } from '../../api/resolver/query-config-resolver.service';
import { QueryConfigDetailsComponent } from './query-config-details/query-config-details.component';
import { canActivateCheckPermissionItemGuard } from 'app/commons/service/guard.service';

export const routeQueryConfigDataExtraction: Route =
{
  path: 'queryconfig/data-extraction',
  loadChildren: () =>
    import('./query-config.module'),
  data: { breadcrumb: 'Data Extraction' }
};

export const routeQueryConfigDataUpdate: Route =
{
  path: 'queryconfig/data-update',
  loadChildren: () =>
    import('./query-config.module'),
  data: { breadcrumb: 'Data Update' }
};

const routesChildren: Routes = [
  { path: ':id', component: QueryConfigDetailsComponent, resolve: { queryConfigs: queryConfigIdResolver } }
];

const routes: Routes = [

  {
    path: ':id',
    component: QueryConfigComponent,
    resolve: { queryConfigs: queryConfigResolver },
    canActivate: [canActivateCheckPermissionItemGuard],
    children: routesChildren
  },
  {
    path: ':context/:id', component: QueryConfigComponent, resolve: { queryConfigs: queryConfigResolver },
    children: [
      { path: ':id', component: QueryConfigDetailsComponent, resolve: { queryConfigs: queryConfigIdResolver } }
    ]
  }
];



@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class QueryConfigRoutingModule { }
