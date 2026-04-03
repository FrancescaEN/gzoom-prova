import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeQueryConfigDataExtraction, routeQueryConfigDataUpdate } from "../query-config/query-config-routing.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";


const CTX_CG_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "GP_MENU_00418" },
    children: [
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00619' } }}
    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00419" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00512' } }}
    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "GP_MENU_00420" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00502' } }},
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00507' } }},
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00558' } }}
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_CG_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_CG_RoutingModule { }
