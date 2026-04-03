import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TypologiesRolesSubjectsComponent } from './typologies-roles-subjects.component';
import { roleTypeResolverService } from 'app/api/resolver/role-type-resolver.service';
import { partyTypeResolverService } from 'app/api/resolver/party-type-resolver.service';
import { workEffortAssocTypeResolver } from 'app/api/resolver/work-effort-assoc-type-resolver.service';
import { enumerationResolverService } from 'app/api/resolver/enumeration-resolver.service';
import { workEffortTypeIsRootResolverService } from 'app/api/resolver/work-effort-type-is-root-resolver.service';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';

const routes: Routes = [
    { path: '', component: TypologiesRolesSubjectsComponent, resolve: { roleTypes: roleTypeResolverService, partyTypes: partyTypeResolverService, workEffortAssocTypes: workEffortAssocTypeResolver, enumerations: enumerationResolverService, workEffortTypes: workEffortTypeIsRootResolverService }, canDeactivate: [canDeactivateUnsavedGuard] }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TypologiesRolesSubjectsRoutingModule { }
