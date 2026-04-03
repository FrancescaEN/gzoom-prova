import { WorkEffort } from "app/api/model/work-effort";
import { WorkEffortAssoc } from "./workEffortAssoc";
import { WorkEffortAssocType } from "./workEffortAssocType";
import { WorkEffortMeasure } from "./workEffortMeasure";

export class WorkEffortAssocEx extends WorkEffortAssoc {
    public workEffortMeasure?: WorkEffortMeasure;
    public workEffortAssocType?: WorkEffortAssocType;
    public workEffort?: WorkEffort;
    public workEffort2?: WorkEffort;

    constructor(
        public workEffortIdFrom?: string,
        public workEffortIdTo?: string,
        public workEffortAssocTypeId?: string,
        public sequenceNum?: string,
        public fromDate?: Date,
        public thruDate?: Date,
        public assocWeight?: number,
        public comments?: string,
        public commentsLang?: string,
        public weMeasureEvalId?: string,

        public workEffortMeasureId?: string,
        public uomDescr?: string,
        public uomDescrLang?: string,
        public workEffortId?: string,
        public glAccountId?: string,
        public fromDateWEM?: Date,
        public thruDateWEM?: Date,
        public kpiScoreWeight?: number,
        public kpiOtherWeight?: number,
        public weMeasureTypeEnumId?: string,
        public sequenceId?: number,
        public periodTypeId?: string,
        public weScoreRangeEnumId?: string,
        public weScoreConvEnumId?: string,
        public uomRangeId?: string,
        public weWithoutPerf?: string,
        public commentsWEM?: string,
        public commentsLangWEM?: string,

        public workEffortAssocTypeIdWEAT?: string,
        public descriptionWEAT?: string,
        public parentTypeIdWEAT?: string,

        public workEffortIdWEV?: string,
        public workEffortNameWEV?: string,
        public workEffortNameLangWEV?: string,
        public weEtchWEV?: string


    ) {
        super(
            workEffortIdFrom,
            workEffortIdTo,
            workEffortAssocTypeId,
            sequenceNum,
            fromDate,
            thruDate,
            assocWeight,
            comments,
            commentsLang,
            weMeasureEvalId
        );
        this.workEffortMeasure = new WorkEffortMeasure(
            workEffortMeasureId,
            uomDescr,
            uomDescrLang,
            workEffortId,
            glAccountId,
            fromDateWEM,
            thruDateWEM,
            kpiScoreWeight,
            kpiOtherWeight,
            weMeasureTypeEnumId,
            sequenceId,
            periodTypeId,
            weScoreRangeEnumId,
            weScoreConvEnumId,
            uomRangeId,
            weWithoutPerf,
            commentsWEM,
            commentsLangWEM,
        );

        this.workEffortAssocType = new WorkEffortAssocType(
            workEffortAssocTypeIdWEAT,
            descriptionWEAT,
            parentTypeIdWEAT,
        );

        this.workEffort = new WorkEffort(
            workEffortIdWEV,
            workEffortNameWEV,
            workEffortNameLangWEV
        );
    }
}