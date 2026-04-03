import { WorkEffort } from "./work-effort";
import { WorkEffortType } from "./workEffortType";

export class WorkEffortMeasure {
    workEffort?: WorkEffort;
    workEffortType?: WorkEffortType;
    workEffortType2?: WorkEffortType
    constructor(
        public workEffortMeasureId?: string,
        public uomDescr?: string,
        public uomDescrLang?: string,
        public workEffortId?: string,
        public glAccountId?: string,
        public fromDate?: Date,
        public thruDate?: Date,
        public kpiScoreWeight?: number,
        public kpiOtherWeight?: number,
        public weMeasureTypeEnumId?: string,
        public sequenceId?: number,
        public periodTypeId?: string,
        public weScoreRangeEnumId?: string,
        public weScoreConvEnumId?: string,
        public uomRangeId?: string,
        public weWithoutPerf?: string,
        public comments?: string,
        public commentsLang?: string,
    ) { }

}