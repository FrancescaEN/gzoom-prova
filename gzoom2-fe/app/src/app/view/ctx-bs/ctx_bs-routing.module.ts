import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";


const CTX_BS_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "GP_MENU_00400" },
    children: [
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00611' } }}
    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00401" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00494' } }}
    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "GP_MENU_00402" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00445' } }},
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00488' } }},
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00550' } }} 

    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_BS_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_BS_RoutingModule { }
