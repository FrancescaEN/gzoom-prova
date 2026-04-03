import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";
import { routeDashboardAcr } from "./dashboard-acr/dashboard-acr-routing.module";
import {Context} from "../../commons/enum/context";


const CTX_CO_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "Administration" },
    children: [
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00616' } }}
    ]
  },
  {
    path: "management",
    data: { breadcrumb: "Management" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00616' } }}
    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "Consultation" },
    children: [
      {
        path: 'dashboard',
        data: { breadcrumb: "GP_MENU_00626", context: Context.CTX_CO },
        children: routeDashboardAcr


      }
      ,
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00444' } }},
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00487' } }},
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00555' } }}
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_CO_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_CO_RoutingModule { }
