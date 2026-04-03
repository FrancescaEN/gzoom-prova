import { Route } from '@angular/router';

export const routeDetailMeasurementTypesStatus: Route = {
    path: 'measurement-types-status/:levelId',
    loadComponent: () =>
        import('./detail-measurement-types-status/detail-measurement-types-status.component'),
    data: { breadcrumb: 'measurementTypesStatus.CTX_CO.GP_MENU_00626' }
};