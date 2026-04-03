import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountingAndExtraAccountingUnitsComponent } from './accounting-and-extra-accounting-units.component';
import { glAccountTypeResolver } from 'app/api/resolver/gl-account-type-resolver.service';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { glAccountTypeGlFiscalTypeResolver } from 'app/api/resolver/gl-account-type-gl-fiscal-type-resolver.service';
import { NatureComponent } from './nature/nature.component';
import { glAccountResourceResolver } from 'app/api/resolver/gl-account-resource-resolver.service';
import { DetectionTypesComponent } from './detection-types/detection-types.component';
import { OverDetailAccountingAndExtraAccountingUnitsComponent } from './over-detail-accounting-and-extra-accounting-units/over-detail-accounting-and-extra-accounting-units.component';

const routes: Routes = [
    {
        path: ':accountTypeEnumId',
        component: AccountingAndExtraAccountingUnitsComponent,
        resolve: { obss: glAccountTypeResolver },
        canDeactivate: [canDeactivateUnsavedGuard],

    },
    {
        path: ':accountTypeEnumId/:glAccountTypeId', component: OverDetailAccountingAndExtraAccountingUnitsComponent, children: [
            { path: 'detection-types', component: DetectionTypesComponent, resolve: { obss: glAccountTypeGlFiscalTypeResolver }, data: { breadcrumb: 'Detection Type' }, canDeactivate: [canDeactivateUnsavedGuard] },
            { path: 'nature', component: NatureComponent, resolve: { obss: glAccountResourceResolver }, data: { breadcrumb: 'Natura' }, canDeactivate: [canDeactivateUnsavedGuard] }
        ]
    },

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AccountingAndExtraAccountingUnitsRoutingModule { }
