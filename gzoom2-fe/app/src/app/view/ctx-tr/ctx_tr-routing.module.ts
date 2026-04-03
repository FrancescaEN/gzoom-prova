import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeQueryConfigDataExtraction, routeQueryConfigDataUpdate } from "../query-config/query-config-routing.module";
import { routeTimesheet } from "../timesheet/timesheet-routing.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";


const CTX_TR_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "GP_MENU_00421" },
    children: [
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00620' } }}
    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00422" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00498' } }},
      {... routeTimesheet, ... { data: { breadcrumb: 'GP_MENU_C0004' } }},
    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "GP_MENU_00423" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00449' } }},
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00492' } }},
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00559' } }}      
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_TR_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_TR_RoutingModule { }
