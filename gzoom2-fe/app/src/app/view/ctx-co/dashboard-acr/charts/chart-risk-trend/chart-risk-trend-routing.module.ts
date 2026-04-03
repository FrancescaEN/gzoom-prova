import { Route } from '@angular/router';

export const routeDetailRiskTrend: Route = {
    path: 'risk-trend/:areaId',
    loadComponent: () =>
        import('./detail-risk-trend/detail-risk-trend.component'),
    data: { breadcrumb: 'riskTrend.CTX_CO.GP_MENU_00626' }

};