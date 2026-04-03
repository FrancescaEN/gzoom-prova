import {FormField} from "./FormField";

export class FormFieldGroup {
  label?: string; // Label to be displayed for the form field group
  fields: FormField[]; // Array of form fields in the group
  groupClass?: any; // Class to be applied to the group for styling
  showTooltip?: boolean;
  // Required if showTooltip is true
  tooltipDescription?: string;
  groupColumnPosition?: number;
  style?: any;
  labelStyle?: any;


  constructor(data?: FormFieldGroup) {
    this.label = data?.label ?? '';
    this.fields = data?.fields ?? [];
    this.groupClass = data?.groupClass ?? 'flex_column';
    this.groupColumnPosition = (data?.groupColumnPosition ?? 1);
    this.showTooltip = data?.showTooltip ?? false;
    this.tooltipDescription = data?.tooltipDescription ?? '';
    this.style = data?.style ?? null;
    this.labelStyle = data?.labelStyle ?? "font-weight: bold; margin-bottom: 1rem; color: #64748B; font-size: 14px;";
  }
}
