import {FieldType} from "./enum/FieldType";
import {DynamicInputOptions} from "./DynamicInputOptions";
import {ValidatorFn} from "@angular/forms";

export class FormField {
  // Unique identifier for the form field
  id?: string;
  // Name of the form field, used for form submission, the same as on the DB
  name: string;
  // Label to be displayed for the form field
  label?: string; //Can be calculated from name vie ngx-translate
  // Placeholder to be displayed for the form field
  placeholder?: string;
  //Required - the type of the field (text, number, date, select, etc.)
  type: FieldType;
  //Eventual value of the field coming from the DB
  value?: any;
  // Options for select, multi-select, and radio fields
  //TBD: the structure of the options array
  // todo DynamicInputOptions is an empty class //
  // todo options?: DynamicInputOptions | DateOptions | DropdownOptions | RadioButtonOptions | MultiSelectOptions | any;
  options?: DynamicInputOptions | any;
  // Eventually validators to be passed to the form field
  validators?: ValidatorFn | ValidatorFn[];
  //endpoint?: string | Observable<any>;
  // Flag to show an help message
  showTooltip?: boolean;
  // Required if showTooltip is true
  tooltipDescription?: string;
  //custom style for the field
  style?: any;

  fieldClass?: any;

  visibility?: boolean;

  errorMessage?: string;

  errorMessageStyle?: any;

  errorMessageVisible?: boolean;

  labelLeftSpace?: boolean;

  date?: Date;

  disabled?: boolean;

  checked?: boolean;


  constructor(data?: FormField) {
    this.id = data?.id ?? '';
    this.name = data?.name ?? '';
    this.label = data?.label ?? '';
    this.placeholder = data?.placeholder ?? '';
    this.type = data?.type ?? FieldType.TEXT;
    this.value = data?.value ?? '';
    this.validators = data?.validators ?? [];
    this.showTooltip = data?.showTooltip ?? true;
    this.tooltipDescription = data?.tooltipDescription ?? '';
    this.style = data?.style ?? null;
    this.fieldClass = data?.fieldClass ?? null;
    this.options = this.buildOptions(this.type, data?.options);
    this.errorMessage = data?.errorMessage ?? '';
    this.errorMessageStyle = data?.errorMessageStyle ?? null;
    this.errorMessageVisible = data?.errorMessageVisible ?? false;
    this.visibility = data?.visibility ?? true;
    // If the field is part of a label group, it should be indented to the left
    this.labelLeftSpace = data?.labelLeftSpace ?? false;
    this.date = data?.date ?? null;
    this.disabled = data?.disabled ?? false;
    this.checked = data?.checked ?? false;
  }

  private buildOptions(type: FieldType, options: any): DynamicInputOptions /* todo | DateOptions | DropdownOptions | MultiSelectOptions */ {
    switch (type) {
      // todo case FieldType.DATE:
      //   return options ? new DateOptions(options) : new DateOptions();
      // todo case FieldType.DROPDOWN:
      //   return options ? new DropdownOptions(options) : new DropdownOptions();
      // todo case FieldType.MULTISELECT:
      //   return options ? new MultiSelectOptions(options) : new MultiSelectOptions();
      // todo case FieldType.RADIO:
      //   return options ? new RadioButtonOptions(options) : new RadioButtonOptions();
      case FieldType.TEXT:
      case FieldType.TEXTAREA:
        return null;

      default:
        throw new Error(`Unsupported field type: ${type}`);
    }
  }


}
