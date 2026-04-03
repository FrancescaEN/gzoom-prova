import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";


const CTX_EP_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "GP_MENU_00406" },
    children: [
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00613' } }}      
      
    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00407" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00496' } }}
    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "GP_MENU_00408" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00447' } }},
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00490' } }},      
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00552' } }}            
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_EP_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_EP_RoutingModule { }
