import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";


const CTX_RE_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "GP_MENU_00424" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00449' } }},
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00621' } }}


    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00425" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00511' } }}
    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "GP_MENU_00426" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00501' } }},
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00506' } }},
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00560' } }},
            
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_RE_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_RE_RoutingModule { }
