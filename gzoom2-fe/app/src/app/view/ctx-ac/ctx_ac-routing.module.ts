import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { canActivateCheckPermissionItemGuard } from "app/commons/service/guard.service";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";

const CTX_AC_routes: Routes = [
  {
    path: "configuration",
    data: { breadcrumb: "GP_MENU_00003" },
    children: [
      {
        path: "detection-type",
        loadChildren: () =>
          import("./detection-type/detection-type.module"),
        data: { breadcrumb: "GP_MENU_00570" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "Uom",
        loadChildren: () =>
          import("./uom/uom.module"),
        data: { breadcrumb: "GP_MENU_00332" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "uomType",
        loadChildren: () =>
          import("./uom-type/uom-type.module"),
        data: { breadcrumb: "GP_MENU_00347" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "ranges-of-values",
        loadChildren: () =>
          import("./ranges-of-values/ranges-of-values.module"),
        data: { breadcrumb: "GP_MENU_00582" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "periodType",
        loadChildren: () =>
          import("./period-type/period-type.module"),
        data: { breadcrumb: "GP_MENU_00566" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "periods",
        loadChildren: () =>
          import("./periods/periods.module"),
        data: { breadcrumb: "GP_MENU_00572" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "calculation-formulas",
        loadChildren: () =>
          import("./calculation-formulas/calculation-formulas.module"),
        data: { breadcrumb: "GP_MENU_00586" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "nature-unit-cont-extr",
        loadChildren: () =>
          import("./nature-unit-cont-extr/nature-unit-cont-extr.module"),
        data: { breadcrumb: "GP_MENU_00571" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "purpose",
        loadChildren: () =>
          import("./purpose/purpose.module"),
        data: { breadcrumb: "GP_MENU_00574" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00010" },
    children: [
      {
        path: "indicators/:accountTypeEnumId/:isReservedAccount",
        loadChildren: () =>
          import("./indicators/indicators.module"),
        data: { breadcrumb: "GP_MENU_00609", gpMenu: "GP_MENU_00609" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "financial-indicators/:accountTypeEnumId",
        loadChildren: () =>
          import("./indicators/indicators.module"),
        data: { breadcrumb: "GP_MENU_00604", gpMenu: "GP_MENU_00604" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "economic-indicators/:accountTypeEnumId",
        loadChildren: () =>
          import("./indicators/indicators.module"),
        data: { breadcrumb: "GP_MENU_00608", gpMenu: "GP_MENU_00608" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "reserved-indicators/:accountTypeEnumId/:isReservedAccount",
        loadChildren: () =>
          import("./indicators/indicators.module"),
        data: { breadcrumb: "GP_MENU_00610", gpMenu: "GP_MENU_00610" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "financial-indicator-movements/:accountTypeEnumId",
        loadChildren: () =>
          import("./indicator-movements/indicator-movements-routing.module"),
        data: { breadcrumb: "GP_MENU_00624", gpMenu: "GP_MENU_00624" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "economic-indicator-movements/:accountTypeEnumId",
        loadChildren: () =>
          import("./indicator-movements/indicator-movements-routing.module"),
        data: { breadcrumb: "GP_MENU_00623", gpMenu: "GP_MENU_00623" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "indicator-movements/:accountTypeEnumId/:isReservedAccount",
        loadChildren: () =>
          import("./indicator-movements/indicator-movements-routing.module"),
        data: { breadcrumb: "GP_MENU_00622", gpMenu: "GP_MENU_00622" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "reserved-indicator-movements/:accountTypeEnumId/:isReservedAccount",
        loadChildren: () =>
          import("./indicator-movements/indicator-movements-routing.module"),
        data: { breadcrumb: "GP_MENU_00625", gpMenu: "GP_MENU_00625" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "accounting-and-extra-accounting-units-indicator",
        loadChildren: () =>
          import("./accounting-and-extra-accounting-units/accounting-and-extra-accounting-units.module"),
        data: { breadcrumb: "GP_MENU_00583" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "accounting-and-extra-accounting-units-financial",
        loadChildren: () =>
          import("./accounting-and-extra-accounting-units/accounting-and-extra-accounting-units.module"),
        data: { breadcrumb: "GP_MENU_00584" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      {
        path: "accounting-and-extra-accounting-units-account",
        loadChildren: () =>
          import("./accounting-and-extra-accounting-units/accounting-and-extra-accounting-units.module"),
        data: { breadcrumb: "GP_MENU_00585" },
        canActivate: [canActivateCheckPermissionItemGuard]
      },
      routeQueryConfigDataUpdate,

    ]
  },
  {
    path: "interoperability",
    data: { breadcrumb: "GP_MENU_00600" },
    children: [
      routeQueryConfigDataExtraction,

    ]
  }

];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_AC_routes),
    SharedModule,
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_AC_RoutingModule { }
