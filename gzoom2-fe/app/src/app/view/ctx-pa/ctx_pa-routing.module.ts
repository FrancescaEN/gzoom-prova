import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";


const CTX_PA_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "GP_MENU_00451" },
    children: [
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00615' } }}
      
    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00456" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00509' } }}
    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "GP_MENU_00460" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00499' } }},
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00504' } }},
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00554' } }}      
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_PA_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_PA_RoutingModule { }
