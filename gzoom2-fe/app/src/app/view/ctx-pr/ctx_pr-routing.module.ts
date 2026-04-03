import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeTimesheet } from "../timesheet/timesheet-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";


const CTX_PR_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "GP_MENU_00412" },
    children: [
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00617' } }}

    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00413" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00513' } }},
      {... routeTimesheet, ... { data: { breadcrumb: 'GP_MENU_00563' } }}

    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "GP_MENU_00414" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00503' } }},
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00508' } }},
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00556' } }}
      
    ]
  }
];


@NgModule({
  imports: [
    RouterModule.forChild(CTX_PR_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_PR_RoutingModule { }
