import { SummaryMeasure } from "./summaryMeasure";

export interface Area {
    areaId: string;
    areaName?: string;
    areaNameLang?: string;
    areaScore?: number;
    processCount?: number;
    areaSummary?: SummaryMeasure;
}