import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { canActivateCheckPermissionItemGuard } from "app/commons/service/guard.service";

const CTX_BA_routes: Routes = [

  {
    path: "interoperability",
    data: { breadcrumb: "GP_MENU_00599" },
    children: [
      {
        path: "interfacciamentoDati",
        loadChildren: () =>
          import("./interfacciamento-dati/interfacciamento-dati.module"),
        data: { breadcrumb: "GP_MENU_00561" },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "subsystem",
        loadChildren: () =>
          import("./subsystem/subsystem.module"),
        data: { breadcrumb: 'GP_MENU_00592' },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "subsystem-types",
        loadChildren: () =>
          import("./subsystem-types/subsystem-types.module"),
        data: { breadcrumb: 'GP_MENU_00569' },
        canActivate: [canActivateCheckPermissionItemGuard],
      }

    ],
  },
  {
    path: "security",
    data: { breadcrumb: "GP_MENU_00078" },
    children: [
      {
        path: "approval-paths",
        loadChildren: () =>
          import("./approval-paths/approval-paths.module"),
        data: { breadcrumb: 'GP_MENU_00595' },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "security-groups",
        loadChildren: () =>
          import("./security-groups/security-groups.module"),
        data: { breadcrumb: 'GP_MENU_00601' },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
    ]
  },
  {
    path: "support",
    data: { breadcrumb: "GP_MENU_00080" },
    children: [
      {
        path: "scheduler",
        loadChildren: () =>
          import("./scheduler/scheduler.module"),
        data: { breadcrumb: 'GP_MENU_00593' },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "consultingLog",
        loadChildren: () =>
          import("./consulting-log/consulting-log.module"),
        data: { breadcrumb: 'GP_MENU_00594' },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_BA_routes),
    SharedModule,
  ],
  providers: [],
  exports: [RouterModule],
})
export class CTX_BA_RoutingModule { }
