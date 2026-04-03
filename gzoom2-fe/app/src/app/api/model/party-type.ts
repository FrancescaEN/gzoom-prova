import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";

/**
 * Model of a PartyType.
 */
export class PartyType {
    constructor(public partyTypeId?: string,
        public description?: string,
        public hasTable?: string,
        public parentTypeId?: string,
        public variableGridArray?: VariableGridArray) { }
}
