import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MeasuresObjectivesRatingScaleComponent } from './measures-objectives-rating-scale/measures-objectives-rating-scale.component';
import { MeasuresObjectivesComponent } from './measures-objectives.component';
import { workEffortMeasExUomResolver } from 'app/api/resolver/work-effort-meas-ex-uom-resolver.service';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { measuresObjectivesRatingScaleResolver } from 'app/api/resolver/measures-objectives-rating-scale-resolver.service';
import { MeasuresObjectivesDetailComponent } from './measures-objectives-detail/measures-objectives-detail.component';
import { workEffortMeasureResolver } from 'app/api/resolver/work-effort-measure-resolver.service';
import { OverDetailsMeasuresObjectivesComponent } from './over-details-measures-objectives/over-details-measures-objectives.component';


const routes: Routes = [
  { path: '', component: MeasuresObjectivesComponent, canDeactivate: [canDeactivateUnsavedGuard] },
  {
    path: ':workEffortMeasureId', component: OverDetailsMeasuresObjectivesComponent, children: [ 
    { path: 'detail', component: MeasuresObjectivesDetailComponent, resolve: { obss: workEffortMeasureResolver }, data: { breadcrumb: 'Measurement detail' }, canDeactivate: [canDeactivateUnsavedGuard] },
    { path: 'rating-scale', component: MeasuresObjectivesRatingScaleComponent, resolve: { obss: measuresObjectivesRatingScaleResolver }, data: { breadcrumb: 'Measurement scale' }, canDeactivate: [canDeactivateUnsavedGuard] }]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MeasuresObjectivesRoutingModule { }
