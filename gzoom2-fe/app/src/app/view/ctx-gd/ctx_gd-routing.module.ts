import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";


const CTX_GD_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "GP_MENU_00415" },
    children: [
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00618' } }}      
      
    ]
  },
  {
    path: "mapping-treatments",
    data: { breadcrumb: "GP_MENU_00416" },
    children: []
  },
  {
    path: "risk-assessment",
    data: { breadcrumb: "GP_MENU_00431" },
    children: []
  },
  {
    path: "management-risk",
    data: { breadcrumb: "GP_MENU_00432" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00497' } }}      
      
    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "GP_MENU_00417" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00448' } }},
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00491' } }},   
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00557' } }}          
      
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_GD_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_GD_RoutingModule { }
