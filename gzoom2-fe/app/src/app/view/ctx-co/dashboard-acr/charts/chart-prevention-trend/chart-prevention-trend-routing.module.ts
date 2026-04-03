import { Route } from '@angular/router';

export const routeDetailPreventionTrend: Route = {
    path: 'prevention-trend/:areaId',
    loadComponent: () =>
        import('./detail-prevention-trend/detail-prevention-trend.component'),
    data: { breadcrumb: 'preventionTrend.CTX_CO.GP_MENU_00626' }

};