import { Route } from '@angular/router';

export const routeDetailRiskState: Route = {
    path: 'risk-state/:year',
    loadComponent: () =>
        import('./detail-risk-state/detail-risk-state.component'),
    data: { breadcrumb: 'riskStatus.CTX_CO.GP_MENU_00626' }
};