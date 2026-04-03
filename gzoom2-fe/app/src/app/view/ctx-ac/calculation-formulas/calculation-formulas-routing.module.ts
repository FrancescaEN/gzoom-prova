import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { CalculationFormulasComponent } from './calculation-formulas.component';
import { customMethodResolver } from 'app/api/resolver/custom-method-resolver.service';
import { ValuesMatrixComponent } from './values-matrix/values-matrix.component';
import { customMethodMatrixResolver } from 'app/api/resolver/custom-method-matrix-resolver.service';


const routes: Routes = [
    { path: '', component: CalculationFormulasComponent, resolve: { obss: customMethodResolver }, canDeactivate: [canDeactivateUnsavedGuard] },
    { path: ':customMethodId', component: ValuesMatrixComponent, resolve: { obss: customMethodMatrixResolver }, data: { breadcrumb: 'Value Matrix' }, canDeactivate: [canDeactivateUnsavedGuard] }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class CalculationFormulasRoutingModule { }
