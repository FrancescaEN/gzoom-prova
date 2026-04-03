import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../shared/shared.module";
import {
  canActivateAuthGuard,
  canActivateCheckPermissionGuard,
  canActivateChildAuthGuard,
  canActivateLoginGuard,
} from "../commons/service/guard.service";
import { permissionsResolver } from "../shared/permissions-resolver.service";
import { menuResolver } from "../shared/menu-resolver.service";
import { nodeResolver } from "../shared/node-resolver.service";
import { FocusComponent } from "../layout/focus/focus.component";
import { ContentComponent } from "../layout/content/content.component";
import { localizationResolver } from "app/shared/localization-resolver.service";
import { visualThemeResolver } from "app/shared/visual-theme-resolver.service";
import { Context } from "app/commons/enum/context";

const routes: Routes = [
  {
    path: "",
    component: FocusComponent,
    resolve: {
      node: nodeResolver,
    },
    children: [
      {
        path: "login",
        loadChildren: () =>
          import("./login/login.module").then((m) => m.LoginModule),
      },
    ],
  },
  {
    path: "c",
    component: ContentComponent,
    canActivate: [canActivateAuthGuard],
    // canActivateChild: [canActivateChildAuthGuard],
    resolve: {
      permissions: permissionsResolver,
      locale: localizationResolver,
      menu: menuResolver,
      node: nodeResolver,
      theme: visualThemeResolver,
    },
    children: [
      {
        path: "portal",
        loadChildren: () =>
          import("./gzoom-portal/gzoom-portal-routing.module").then(
            (m) => m.GzoomPortalRoutingModule,
          ),
      },
      {
        path: "dashboard",
        loadChildren: () =>
          import("./dashboard/dashboard.module").then((m) => m.DashboardModule),
      },
      {
        path: "2FAuth",
        loadChildren: () =>
          import("./login/two-factor-auth/two-factor-auth.module"),
      },
      {
        path: "2FAuthDisable",
        loadChildren: () =>
          import("./login/two-factor-auth-disable/two-factor-auth-disable.module"),
      },
      {
        path: "login2FAuth",
        loadChildren: () => import("./login/login-2fauth/login-2fauth.module"),
      },
      {
        path: "legacy",
        loadChildren: () =>
          import("./legacy/legacy.module").then((m) => m.LegacyModule),
      },
      {
        path: "CTX_AC",
        loadChildren: () => import("./ctx-ac/ctx_ac-routing.module"),
        data: { breadcrumb: "Indicators ", context: Context.CTX_AC },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_BA",
        loadChildren: () =>
          import("./ctx-ba/ctx_ba-routing.module").then(
            (x) => x.CTX_BA_RoutingModule,
          ),
        data: { breadcrumb: "System", context: Context.CTX_BA },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_WE",
        loadChildren: () => import("./ctx-we/ctx_we-routing.module"),
        data: { breadcrumb: "Objects", context: Context.CTX_WE },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_PY",
        loadChildren: () => import("./ctx-py/ctx_py-routing.module"),
        data: { breadcrumb: "Subjects", context: Context.CTX_PY },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_BS",
        loadChildren: () => import("./ctx-bs/ctx_bs-routing.module"),
        data: { breadcrumb: "Strategic Performance", context: Context.CTX_BS },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_CG",
        loadChildren: () => import("./ctx-cg/ctx_cg-routing.module"),
        data: { breadcrumb: "Management Control", context: Context.CTX_CG },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_OR",
        loadChildren: () => import("./ctx-or/ctx_or-routing.module"),
        data: {
          breadcrumb: "Operational Performance",
          context: Context.CTX_OR,
        },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_EP",
        loadChildren: () => import("./ctx-ep/ctx_ep-routing.module"),
        data: { breadcrumb: "Individual Performance", context: Context.CTX_EP },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_DI",
        loadChildren: () => import("./ctx-di/ctx_di-routing.module"),
        data: { breadcrumb: "Performance Executives", context: Context.CTX_DI },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_PA",
        loadChildren: () => import("./ctx-pa/ctx_pa-routing.module"),
        data: {
          breadcrumb: "Participatory Performance",
          context: Context.CTX_PA,
        },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_CO",
        loadChildren: () => import("./ctx-co/ctx_co-routing.module"),
        data: { breadcrumb: "Anticorruption", context: Context.CTX_CO },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_PR",
        loadChildren: () => import("./ctx-pr/ctx_pr-routing.module"),
        data: { breadcrumb: "Processes-Procedures", context: Context.CTX_PR },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_GD",
        loadChildren: () => import("./ctx-gd/ctx_gd-routing.module"),
        data: { breadcrumb: "Privacy GDPR", context: Context.CTX_GD },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_TR",
        loadChildren: () => import("./ctx-tr/ctx_tr-routing.module"),
        data: { breadcrumb: "Transparency", context: Context.CTX_TR },
        canActivate: [canActivateCheckPermissionGuard],
      },
      {
        path: "CTX_RE",
        loadChildren: () => import("./ctx-re/ctx_re-routing.module"),
        data: { breadcrumb: "Integrated Planning", context: Context.CTX_RE },
        canActivate: [canActivateCheckPermissionGuard],
      },
      { path: "**", pathMatch: "full", redirectTo: "/c/dashboard" },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes), SharedModule],
  exports: [RouterModule],
})
export class ViewRoutingModule {}
