import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { routeAnalysis } from "../analysis/analysis-routing.module";
import { routeQueryConfigDataUpdate, routeQueryConfigDataExtraction } from "../query-config/query-config-routing.module";
import { routeReportPrint } from "../report-print/report-print-routing.module";
import { routeTimesheet } from "../timesheet/timesheet-routing.module";
import { routeAnalysisType } from "../ctx-we/analysis-type/analysis-type-routing.module";


const CTX_OR_routes: Routes = [
  {
    path: "administration",
    data: { breadcrumb: "GP_MENU_00403" },
    children: [
      {... routeAnalysisType, ... { data: { breadcrumb: 'GP_MENU_00612' } }}      

    ]
  },
  {
    path: "management",
    data: { breadcrumb: "GP_MENU_00404" },
    children: [
      {... routeQueryConfigDataUpdate, ... { data: { breadcrumb: 'GP_MENU_00495' } }},     
      {... routeTimesheet, ... { data: { breadcrumb: 'GP_MENU_00562' } }}      
    ]
  },
  {
    path: "consultation",
    data: { breadcrumb: "GP_MENU_00405" },
    children: [
      {... routeReportPrint, ... { data: { breadcrumb: 'GP_MENU_00446' } }},     
      {... routeQueryConfigDataExtraction, ... { data: { breadcrumb: 'GP_MENU_00489' } }},      
      {... routeAnalysis, ... { data: { breadcrumb: 'GP_MENU_00551' } }} 
      
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(CTX_OR_routes),
    SharedModule
  ],
  providers: [],
  exports: [RouterModule],
})
export default class CTX_OR_RoutingModule { }
