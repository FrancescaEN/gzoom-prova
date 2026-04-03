import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PositionsEconomicsComponent } from './positions-economics.component';
import { emplPositionTypeResolver } from '../../../api/resolver/empl-position-type-resolver.service';
import { canDeactivateUnsavedGuard } from 'app/shared/can-deactivate.guard';

const routes: Routes = [
    { path: '', component: PositionsEconomicsComponent, resolve: { emplPositionType: emplPositionTypeResolver }, canDeactivate: [canDeactivateUnsavedGuard] }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PositionsEconomicsRoutingModule { }
