import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LoginCardComponent } from "./login-card.component";
import { nodeResolver } from "app/shared/node-resolver.service";
const routes: Routes = [
  { path: "", component: LoginCardComponent, resolve: { node: nodeResolver } },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LoginCardRoutingModule {}
