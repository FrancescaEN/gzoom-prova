import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';
import { TypesRelationshipsBetweenSubjectsComponent } from './types-relationships-between-subjects.component';
import { TypesRelationshipsBetweenSubjectsDetailsComponent } from './types-relationships-between-subjects-details/types-relationships-between-subjects-details.component';
import { partyRelationshipTypeResolver } from 'app/api/resolver/party-relationship-type-resolver.service';
import { partyRelationshipRoleResolver } from 'app/api/resolver/party-relationship-role-resolver.service';


const routes: Routes = [
    { path: '', component: TypesRelationshipsBetweenSubjectsComponent, resolve: { obss: partyRelationshipTypeResolver }, canDeactivate: [canDeactivateUnsavedGuard] },
    { path: ':partyRelationshipTypeId', component: TypesRelationshipsBetweenSubjectsDetailsComponent, resolve: { obss: partyRelationshipRoleResolver }, data: { breadcrumb: 'Allowed Roles' }, canDeactivate: [canDeactivateUnsavedGuard] }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TypesRelationshipsBetweenSubjectsRoutingModule { }
