import { Area } from "./area";
import { ProcessLevel } from "./level";
import { SummaryMeasure } from "./summaryMeasure";

export class Process {
    processId: string;
    processEtch: string;
    processName?: string;
    processNameLang?: string;
    area?: Area;
    processLevel?: ProcessLevel;
    proScore?: number;
    processSummary?: SummaryMeasure;
}