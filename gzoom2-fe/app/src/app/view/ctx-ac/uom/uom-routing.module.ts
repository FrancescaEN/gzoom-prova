import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { uomRatingScaleResolver } from '../../../api/resolver/uom-rating-scale-resolver.service';
import { UomRatingScaleComponent } from './scale/uom-rating-scale.component';


import { uomTypeResolver } from '../../../api/resolver/uom-type-resolver.service';
import { uomResolver } from '../../../api/resolver/uom-resolver.service';
import { UomComponent } from './uom/uom.component';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';

const routes: Routes = [
  { path: '', component: UomComponent, resolve: { uoms: uomResolver, uomTypes: uomTypeResolver }, canDeactivate: [canDeactivateUnsavedGuard] },
  { path: ':uomId', component: UomRatingScaleComponent, data: { breadcrumb: 'Measurement scale' }, resolve: { uomRatingScales: uomRatingScaleResolver, uoms: uomResolver, uomTypes: uomTypeResolver }, canDeactivate: [canDeactivateUnsavedGuard] }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UomRoutingModule { }
