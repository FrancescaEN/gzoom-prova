import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TwoFactorAuthDisableComponent } from './two-factor-auth-disable.component';

const routes: Routes = [
  { path: '', component: TwoFactorAuthDisableComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TwoFactorAuthDisableRoutingModule { }
