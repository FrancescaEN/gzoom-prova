import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { periodTypeResolver } from '../../../api/resolver/period-type-resolver.service';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { SubsystemTypesComponent } from './subsystem-types.component';
import { dataSourceTypeResolver } from 'app/api/resolver/data-source-type-resolver.service';

const routes: Routes = [
  { path: '', component: SubsystemTypesComponent, resolve: { dataSourceType: dataSourceTypeResolver }, canDeactivate: [canDeactivateUnsavedGuard] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SubsystemTypesRoutingModule { }
