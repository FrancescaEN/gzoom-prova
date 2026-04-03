import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PeriodTypeComponent } from './period-type.component';
import { periodTypeResolver } from '../../../api/resolver/period-type-resolver.service';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';

const routes: Routes = [
  { path: '', component: PeriodTypeComponent, resolve: { periodTypes: periodTypeResolver }, canDeactivate: [canDeactivateUnsavedGuard] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PeriodTypeRoutingModule { }
