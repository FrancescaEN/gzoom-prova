import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { SubsystemComponent } from './subsystem.component';
import { dataSourceExResolver } from 'app/api/resolver/data-source-resolver.service';
import { SubsystemDetailsComponent } from './subsystem-details/subsystem-details.component';
import { StandardImportFieldConfigExResolver } from 'app/api/resolver/standard-import-field-config-resolver.service';


const routes: Routes = [
    { path: ':dataSourceId', component: SubsystemDetailsComponent, resolve: { obss: StandardImportFieldConfigExResolver }, data: { breadcrumb: 'Subsystem Details' }, canDeactivate: [canDeactivateUnsavedGuard] },
    { path: '', component: SubsystemComponent, canDeactivate: [canDeactivateUnsavedGuard], resolve: { obss: dataSourceExResolver } }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SubsystemRoutingModule { }