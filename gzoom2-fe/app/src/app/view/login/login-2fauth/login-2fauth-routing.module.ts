import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login2fauthComponent } from './login-2fauth.component';

const routes: Routes = [
  { path: '', component: Login2fauthComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Login2fauthRoutingModule { }
