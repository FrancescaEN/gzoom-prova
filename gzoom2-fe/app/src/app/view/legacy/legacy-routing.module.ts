import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LegacyComponent } from './legacy.component';
import { safeIdentifierGuard } from 'app/shared/safe-identifier.guard';

const routes: Routes = [
  {
    path: ':context',
    children: [
      {
        path: ':menu',
        children: [{ path: ':id', component: LegacyComponent, canActivate: [safeIdentifierGuard], data: { breadcrumb: '' } }]
        , data: { breadcrumb: '' }
      }],
    data: { breadcrumb: '' }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LegacyRoutingModule { }
