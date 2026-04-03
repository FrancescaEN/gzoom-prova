import { Route, Routes } from '@angular/router';
import { routeDetailRiskState } from './charts/chart-risk-state/chart-risk-state-routing.module';
import { routeDetailRiskTrend } from './charts/chart-risk-trend/chart-risk-trend-routing.module';
import { routeDetailPreventionTrend } from './charts/chart-prevention-trend/chart-prevention-trend-routing.module';
import { routeDetailStateRiskFactors } from './charts/chart-state-risk-factors/chart-state-risk-factors-routing.module';
import { routeDetailMeasurementTypesStatus } from './charts/chart-measurement-types-status/chart-measurement-types-status-routing.module';
import { routeDetailPreventionState } from './charts/chart-prevention-state/chart-prevention-state-routing.module';

export const routeDashboardAcr: Routes = [{
    path: '',
    loadComponent: () =>
        import('./dashboard-acr.component'),



},
    routeDetailRiskState,
    routeDetailRiskTrend,
    routeDetailPreventionTrend,
    routeDetailStateRiskFactors,
    routeDetailMeasurementTypesStatus,
    routeDetailPreventionState
];