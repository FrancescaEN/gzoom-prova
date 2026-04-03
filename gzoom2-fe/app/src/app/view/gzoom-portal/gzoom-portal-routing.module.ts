import { NgModule } from '@angular/core';
import { Route, RouterModule, Routes } from '@angular/router';
import { canActivateCheckPermissionItemGuard } from 'app/commons/service/guard.service';
import { GzoomPortalComponent } from './gzoom-portal.component';

export const routeAnalysis: Route = {
  path: 'portal',
  loadChildren: () =>
    import('./gzoom-portal.module').then(m => m.GzoomPortalModule),
  data: { breadcrumb: 'Portal' },
  canActivate: [canActivateCheckPermissionItemGuard],
};

const routes: Routes = [
  {
    path: '',
    component: GzoomPortalComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GzoomPortalRoutingModule { }
