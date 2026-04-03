import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TwoFactorAuthComponent } from './two-factor-auth.component';

const routes: Routes = [
  { path: '', component: TwoFactorAuthComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TwoFactorAuthRoutingModule { }
