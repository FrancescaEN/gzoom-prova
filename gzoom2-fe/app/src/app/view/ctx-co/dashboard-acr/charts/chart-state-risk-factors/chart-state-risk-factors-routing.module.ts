import { Route } from '@angular/router';

export const routeDetailStateRiskFactors: Route = {
    path: 'state-risk-factors/:levelId',
    loadComponent: () =>
        import('./detail-state-risk-factors/detail-state-risk-factors.component'),
    data: { breadcrumb: 'stateRiskFactors.CTX_CO.GP_MENU_00626' }
};