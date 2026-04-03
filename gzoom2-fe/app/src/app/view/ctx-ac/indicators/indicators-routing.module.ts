import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IndicatorsComponent } from './indicators.component';
import { IndicatorTabComponent } from './indicator-tab/indicator-tab.component';

const routes: Routes = [
    {
        path: '',
        component: IndicatorsComponent,

    },
    {
        path: 'financial-tab/:glAccountId',
        component: IndicatorTabComponent,
        data: { breadcrumb: "financialTab.CTX_CA.GP_MENU_00604" },
        loadChildren: () => import('./indicator-tab/indicator-tab.module')
    },
    {
        path: 'economic-tab/:glAccountId',
        component: IndicatorTabComponent,
        data: { breadcrumb: "economicTab.CTX_CA.GP_MENU_00608" },
        loadChildren: () => import('./indicator-tab/indicator-tab.module')
    },
    {
        path: 'indicator-tab/:glAccountId',
        component: IndicatorTabComponent,
        data: { breadcrumb: "indicatorTab.CTX_CA.GP_MENU_00609" },
        loadChildren: () => import('./indicator-tab/indicator-tab.module')
    },
    {
        path: 'reserved-indicator-tab/:glAccountId',
        component: IndicatorTabComponent,
        data: { breadcrumb: "reservedIndicatorTab.CTX_CA.GP_MENU_00610" },
        loadChildren: () => import('./indicator-tab/indicator-tab.module')
    }
];
@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class IndicatorsRoutingModule { }
