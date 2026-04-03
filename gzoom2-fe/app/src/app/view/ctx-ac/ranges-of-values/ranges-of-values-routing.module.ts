import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { RangesOfValuesComponent } from './ranges-of-values.component';
import { RangesOfValuesDetailComponent } from './ranges-of-values-detail/ranges-of-values-detail.component';
import { uomRangeResolver } from 'app/api/resolver/uom-range.resolver.service';
import { uomRangeValuesResolver } from 'app/api/resolver/uom-range-values-resolver.service';


const routes: Routes = [
    { path: '', component: RangesOfValuesComponent, resolve: { obss: uomRangeResolver }, canDeactivate: [canDeactivateUnsavedGuard] },
    { path: ':uomRangeId', component: RangesOfValuesDetailComponent, resolve: { obss: uomRangeValuesResolver }, data: { breadcrumb: 'Detail' }, canDeactivate: [canDeactivateUnsavedGuard] }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class RangesOfValuesRoutingModule { }
