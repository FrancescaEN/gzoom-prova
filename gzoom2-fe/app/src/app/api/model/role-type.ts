import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";

/**
 * Model of a RoleType.
 */
export class RoleType {
  constructor(
    public roleTypeId?: string,
    public description?: string,
    public descriptionLang?: string,
    public shortLabel?: string,
    public shortLabelLang?: string,
    public parentTypeId?: string,
    public prevPartyTypeId?: string,
    public workEffortTypeId?: string,
    public workEffortAssocTypeId?: string,
    public workEffortPeriodId?: string,
    public parentTypeIdDescription?: string,
    public prevPartyTypeIdDescription?: string,
    public workEffortTypeIdDescription?: string,
    public workEffortAssocTypeIdDescription?: string,
    public workEffortPeriodIdDescription?: string,
    public variableGridArray?: VariableGridArray
  ) { }
}
