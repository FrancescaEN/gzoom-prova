import { WorkEffortType } from "app/view/report-print/report";
import { WorkEffortAnalysis } from "./workEffortAnalysis";

export class WorkEffortAnalysisEx extends WorkEffortAnalysis {
    public workEffortType: WorkEffortType;
    public parentWorkEffortType: WorkEffortType;
}