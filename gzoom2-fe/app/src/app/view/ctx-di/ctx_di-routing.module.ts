import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";


const CTX_DI_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "GP_MENU_00467" },
    children: [
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00614' } }}
    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00474" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00510' } }},
    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "GP_MENU_00478" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00500' } }},
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00505' } }},      
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00553' } }}            
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_DI_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_DI_RoutingModule { }
