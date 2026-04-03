import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";

/**
 * Model of a RateType.
 */
export class RateType {
    constructor(public rateTypeId?: string,
        public description?: string,
        public descriptionLang?: string,
        public variableGridArray?: VariableGridArray) { }
}
