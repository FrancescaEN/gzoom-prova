import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { RateType } from "./rateType";

/**
 * Model of a WorkEffortAssignmentRate.
 */
export class WorkEffortAssignmentRate {
    constructor(public rateTypeId?: string,
        public workEffortId?: string,
        public description?: string,
        public rateType?: RateType,
        public partyId?: string,
        public rate?: number) { }
}
