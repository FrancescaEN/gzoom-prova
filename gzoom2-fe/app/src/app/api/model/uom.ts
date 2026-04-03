import { UomType } from '../../view/ctx-ac/uom-type/uom_type';
import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { UomRatingScale } from './uomRatingScale';

/**
 * Model of a Uom.
 */
export class Uom {

  uomRatingScale?: UomRatingScale;
  constructor(
    public uomId?: string,
    public uomTypeId?: string,
    public uomType?: string,
    public abbreviation?: string,
    public description?: string,
    public descriptionLang?: string,
    public abbreviationLang?: string,
    public decimalScale?: number,
    public minValue?: number,
    public maxValue?: number,
    public variableGridArray?: VariableGridArray) { }
}
