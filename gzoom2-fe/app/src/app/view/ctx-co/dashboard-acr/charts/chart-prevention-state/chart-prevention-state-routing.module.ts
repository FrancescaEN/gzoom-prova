import { Route } from '@angular/router';

export const routeDetailPreventionState: Route = {
    path: 'prevention-state/:year',
    loadComponent: () =>
        import('./detail-prevention-state/detail-prevention-state.component'),
    data: { breadcrumb: 'preventionState.CTX_CO.GP_MENU_00626' }
};