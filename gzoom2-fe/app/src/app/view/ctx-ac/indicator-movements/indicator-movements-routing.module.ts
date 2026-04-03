import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
    {
        path: '',
        loadComponent: () =>
            import("./list-indicator-movements/indicator-movements.component"),
    },
    {
        path: ':acctgTransId/:acctgTransEntrySeqId',
        data: { breadcrumb: 'detail' },
        loadComponent: () =>
            import("./detail-movement/detail-movement.component")

    }
];
@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export default class IndicatorMovementsRoutingModule { }
