import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { uomTypeResolver } from '../../../api/resolver/uom-type-resolver.service';
import { UomTypeComponent } from '../uom-type/uom-type.component';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
const routes: Routes = [
    { path: '', component: UomTypeComponent, resolve: { uomTypes: uomTypeResolver }, canDeactivate: [canDeactivateUnsavedGuard] },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class UomTypeRoutingModule { }
