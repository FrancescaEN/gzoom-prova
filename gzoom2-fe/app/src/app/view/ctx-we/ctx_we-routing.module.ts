import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { canActivateCheckPermissionItemGuard } from "app/commons/service/guard.service";
import { routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";
import { routeAnalysisType } from "./analysis-type/analysis-type-routing.module";


const CTX_WE_routes: Routes = [
  {
    path: "configuration",
    data: { breadcrumb: "GP_MENU_00029" },
    children: [
      {
        path: "typology-relationships-objectives",
        loadChildren: () =>
          import(
            "./typology-relationships-objectives/typology-relationships-objectives.module"
          ),
        data: { breadcrumb: "GP_MENU_00567" },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "types-attachments-objectives",
        loadChildren: () =>
          import(
            "./types-attachments-objectives/types-attachments-objectives.module"
          ),
        data: { breadcrumb: "GP_MENU_00576" },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00036" },
    children: [
      {
        path: "goals",
        loadChildren: () =>
          import("./goals/goals.module"),
        data: { breadcrumb: 'GP_MENU_00602' },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "objective-codes",
        loadChildren: () =>
          import("./objective-codes/objective-codes.module"),
        data: { breadcrumb: "GP_MENU_00568" },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "relationship-objectives",
        loadChildren: () =>
          import(
            "./relationships-objectives/relationships-objectives.module"
          ),
        data: { breadcrumb: "GP_MENU_00577" },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "subjects-objectives",
        loadChildren: () =>
          import("./subjects-objectives/subjects-objectives.module"),
        data: { breadcrumb: "GP_MENU_00580" },
        canActivate: [canActivateCheckPermissionItemGuard],
      },

      {
        path: "measures-objectives",
        loadChildren: () =>
          import("./measures-objectives/measures-objectives.module"),
        data: { breadcrumb: "GP_MENU_00578" },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "notes-objectives",
        loadChildren: () =>
          import("./notes-objectives/notes-objectives.module"),
        data: { breadcrumb: "GP_MENU_00579" },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "attachments-objectives",
        loadChildren: () =>
          import("./attachments-objectives/attachments-objectives.module"),
        data: { breadcrumb: "GP_MENU_00581" },
        canActivate: [canActivateCheckPermissionItemGuard],
      },

    ]
  },
  {
    path: "interoperability",
    data: { breadcrumb: "GP_MENU_00597" },
    children: [
      {
        path: "query-configuration",
        loadChildren: () =>
          import("./query-configuration/query-configuration.module"),
        data: { breadcrumb: 'GP_MENU_00589' },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {
        path: "query-configuration-extration/:queryType",
        loadChildren: () =>
          import("./query-configuration/query-configuration.module"),
        data: { breadcrumb: 'GP_MENU_00590' },
        canActivate: [canActivateCheckPermissionItemGuard],
      },
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00486' } }},
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00603' } }}
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_WE_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_WE_RoutingModule { }
