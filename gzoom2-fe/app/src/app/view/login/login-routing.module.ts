import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LoginComponent } from "./login.component";
import { visualThemeNAResolver } from "../../shared/visual-theme-na-resolver.service";

const routes: Routes = [
  {
    path: "",
    component: LoginComponent,
    children: [
      {
        path: "",
        loadChildren: () =>
          import("./login-card/login-card.module").then(
            (m) => m.LoginCardModule,
          ),
      },
      {
        path: "check-user",
        loadChildren: () =>
          import("./check-user/check-user.module").then(
            (m) => m.CheckUserModule,
          ),
      },
      {
        path: "reset-password",
        loadChildren: () =>
          import("./reset-password/reset-password.module").then(
            (m) => m.ResetPasswordModule,
          ),
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LoginRoutingModule {}
