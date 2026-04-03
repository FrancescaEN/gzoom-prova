import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";

/**
 * Model of a WorkEffortAssocType.
 */
export class WorkEffortAssocType {
    public workEffortAssocTypeId?: string;
    public description?: string;
    public parentTypeId?: string;
    public parentWorkEffortAssocType?: {
        description?: string;
    };
    public parentDescription?: string;
    public variableGridArray?: VariableGridArray

    constructor(
        workEffortAssocTypeId?: string,
        description?: string,
        parentTypeId?: string,
        parentWorkEffortAssocType?: {
            description?: string;
        },
        parentDescription?: string,
        variableGridArray?: VariableGridArray
    ) {
        this.workEffortAssocTypeId = workEffortAssocTypeId;
        this.description = description;
        this.parentTypeId = parentTypeId;
        this.parentWorkEffortAssocType = parentWorkEffortAssocType;
        this.parentDescription = parentDescription;
        this.variableGridArray = variableGridArray;
    }
}