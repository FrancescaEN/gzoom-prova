import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";

/**
 * Model of a EmplPositionType.
 */
export class EmplPositionType {
    constructor(
        public emplPositionTypeId?: string,
        public description?: string,
        public descriptionLang?: string,
        public parentTypeId?: string,
        public templateId?: string
    ) { }

    public variableGridArray?: VariableGridArray
}