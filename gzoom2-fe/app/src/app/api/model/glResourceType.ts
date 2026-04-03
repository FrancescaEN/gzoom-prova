import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';

/**
 * Model of a GlResourceType.
 */
export class GlResourceType {
    constructor(
        public glResourceTypeId?: string,
        public description?: string,
        public descriptionLang?: string
    ) { }

    public variableGridArray?: VariableGridArray;


}
