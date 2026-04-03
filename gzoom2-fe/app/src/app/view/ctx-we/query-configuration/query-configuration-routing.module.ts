import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { QueryConfigurationComponent } from './query-configuration.component';
import { queryConfigIdResolver, queryConfigurationResolver } from 'app/api/resolver/query-config-resolver.service';
import { DetailQueryComponent } from './detail-query/detail-query.component';
import { DetailConditionsComponent } from './detail-conditions/detail-conditions.component';
import { OverDetailsQueryConfigurationComponent } from './over-details-query-configuration/over-details-query-configuration.component';


const routes: Routes = [
    { path: '', component: QueryConfigurationComponent, resolve: { obss: queryConfigurationResolver }, canDeactivate: [canDeactivateUnsavedGuard] },
    {
        path: ':id', component: OverDetailsQueryConfigurationComponent, children: [
            { path: 'query', component: DetailQueryComponent, resolve: { obss: queryConfigIdResolver }, data: { breadcrumb: 'Query' }, canDeactivate: [canDeactivateUnsavedGuard] },
            { path: 'conditions', component: DetailConditionsComponent, resolve: { obss: queryConfigIdResolver }, data: { breadcrumb: 'Conditions' }, canDeactivate: [canDeactivateUnsavedGuard] },]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class QueryConfigurationRoutingModule { }
