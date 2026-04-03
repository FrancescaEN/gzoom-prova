import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { canActivateCheckPermissionItemGuard } from "app/commons/service/guard.service";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";


const CTX_PY_routes: Routes = [
  {
    path: "configuration",
    data: { breadcrumb: "GP_MENU_00055" },
    children: [
      {
        path: "positions-economics",
        loadChildren: () =>
          import("./positions-economics/positions-economics.module"),
        data: { breadcrumb: "GP_MENU_00573" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "typologies-roles-subjects",
        loadChildren: () =>
          import(
            "./typologies-roles-subjects/typologies-roles-subjects.module"
          ),
        data: { breadcrumb: "GP_MENU_00575" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "types-relationships-between-subjects",
        loadChildren: () =>
          import("./types-relationships-between-subjects/types-relationships-between-subjects.module"),
        data: { breadcrumb: "GP_MENU_00587" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00049" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00546' } }}
      
    ]
  },
  {
    path: "interoperability",
    data: { breadcrumb: "GP_MENU_00598" },
    children: [
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00545' } }}
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_PY_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_PY_RoutingModule { }
