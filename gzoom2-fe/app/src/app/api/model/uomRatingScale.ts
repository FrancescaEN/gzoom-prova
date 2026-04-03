import { VariableGridArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { Uom } from 'app/api/model/uom';

/**
 * Model of a UomRatingScale.
 */
export class UomRatingScale {
  constructor(public uomId?: string,
    public uom?: Uom,
    public uomRatingValue?: number,
    public description?: string,
    public descriptionLang?: string,
    public variableGridArray?: VariableGridArray) { }



}
