import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { SubjectsObjectivesComponent } from './subjects-objectives.component';
import { workEffortPartyAssignmentExResolver } from 'app/api/resolver/work-effort-party-assignment-resolver.service';
const routes: Routes = [
    {
        path: ':code/:id', component: SubjectsObjectivesComponent, canDeactivate: [canDeactivateUnsavedGuard], resolve: {
            obss: workEffortPartyAssignmentExResolver,
        }
    },
    { path: '', component: SubjectsObjectivesComponent, resolve: { obss: workEffortPartyAssignmentExResolver }, canDeactivate: [canDeactivateUnsavedGuard] },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SubjectsObjectivesRoutingModule { }
