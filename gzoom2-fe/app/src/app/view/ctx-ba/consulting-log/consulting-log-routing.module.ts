import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { jobLogExResolver } from 'app/api/resolver/job-log-ex-resolver.service';
import { ConsultingLogComponent } from './consulting-log.component';
import { JobLogJobExecParamsComponent } from './job-log-job-exec-params/job-log-job-exec-params.component';
import { jobLogJobExecParamsResolver } from 'app/api/resolver/job-log-job-exec-params-resolver.service';
import { JobLogLogComponent } from './job-log-log/job-log-log.component';
import { jobLogLogExResolver } from 'app/api/resolver/job-log-log-ex-resolver.service';
import { OverDetailsConsultingLogComponent } from './over-details-consulting-log/over-details-consulting-log.component';

const routes: Routes = [
    { path: '', component: ConsultingLogComponent, resolve: { obss: jobLogExResolver }, canDeactivate: [canDeactivateUnsavedGuard] },
    {
        path: ':jobLogId', component: OverDetailsConsultingLogComponent, children: [ { 
            path: 'jobLogJobExecParams', component: JobLogJobExecParamsComponent, resolve: { obss: jobLogJobExecParamsResolver }, data: { breadcrumb: 'Exec Params' }, canDeactivate: [canDeactivateUnsavedGuard] },
        { path: 'jobLogLog', component: JobLogLogComponent, resolve: { obss: jobLogLogExResolver }, data: { breadcrumb: 'Details Log' }, canDeactivate: [canDeactivateUnsavedGuard] },]}
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ConsultingLogRoutingModule { }
