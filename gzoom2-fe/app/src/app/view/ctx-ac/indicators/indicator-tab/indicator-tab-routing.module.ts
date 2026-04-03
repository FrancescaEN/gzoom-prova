import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canDeactivateGuard, canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { IndicatorTabPurposeComponent } from './indicator-tab-purpose/indicator-tab-purpose.component';
import { IndicatorTabCalculationParametersComponent } from './indicator-tab-calculation-parameters/indicator-tab-calculation-parameters.component';
import IndicatorTabIndicatorComponent from './indicator-tab-indicator/indicator-tab-indicator.component';
import { IndicatorTabOrganizationComponent } from './indicator-tab-organization/indicator-tab-organization.component';
import IndicatorTabValueListComponent from './indicator-tab-value-list/indicator-tab-value-list.component';
import IndicatorTabUoDetectedComponent from './indicator-tab-uo-detected/indicator-tab-uo-detected.component';


const routes: Routes = [
    {
        path: '',
        component: IndicatorTabIndicatorComponent,
        canDeactivate: [canDeactivateGuard]

    },
    {
        path: 'purpose',
        component: IndicatorTabPurposeComponent,
        canDeactivate: [canDeactivateUnsavedGuard],
        data: { breadcrumb: "purpose" }

    },
    {
        path: 'uo-detected',
        component: IndicatorTabUoDetectedComponent,
        canDeactivate: [canDeactivateUnsavedGuard],
        data: { breadcrumb: "uo-detected" }

    },
    {
        path: 'value-list',
        component: IndicatorTabValueListComponent,
        canDeactivate: [canDeactivateUnsavedGuard],
        data: { breadcrumb: "value-list" }

    },
    {
        path: 'organization',
        component: IndicatorTabOrganizationComponent,
        canDeactivate: [canDeactivateUnsavedGuard],
        data: { breadcrumb: "organization" }

    },
    {
        path: 'calculation-parameters',
        component: IndicatorTabCalculationParametersComponent,
        canDeactivate: [canDeactivateUnsavedGuard],
        data: { breadcrumb: "calculation-parameters" }

    }

];
@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export default class IndicatorTabRoutingModule { }