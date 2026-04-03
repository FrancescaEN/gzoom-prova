import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CheckUserComponent } from './check-user.component';

const routes: Routes = [
  { path: '', component: CheckUserComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CheckUserRoutingModule { }
