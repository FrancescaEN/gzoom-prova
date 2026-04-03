import { MenuItem } from "primeng/api";

export interface HeadArray {
  head?;
  subHead?;
  fieldName?;
  actionInput?;
  actionOutput?;
  display?;
  filter?;
  width?;
  colorHeader?;
  flag?: boolean;
  pathIconFlag?;
  sortIcon?: boolean;
  content?;
  tooltip?;
  required?: boolean; //if is true set the attribute input required
  dropdown?: Dropdown;
  textLength?: number;
  unique?: boolean;
  hours?: number;
  labelHours?: string;
}

export interface FooterArray {
  head?;
  subHead?;
  fieldName?;
  actionInput?;
  actionOutput?;
  display?;
  filter?;
  width?;
  colorHeader?;
  flag?: boolean;
  pathIconFlag?;
  sortIcon?: boolean;
  content?;
  tooltip?;
  required?: boolean; //if is true set the attribute input required
  dropdown?: Dropdown;
  textLength?: number;
  unique?: boolean;
}

export interface Dropdown {
  item?: MenuItem[]; //This menu item can be used in the case of multiple dropdowns within the table
  clear?: boolean; // If true, it allows you to clean the dropdown
  key?;
  command?: (event?: any) => void;
}

export interface VariableGridArray {
  id?: string;
  updated?: boolean;
  buttonDetails?: boolean;
  buttonMultipleDetails?: boolean;
  dropdownData?: boolean;
  inputLabeldata?: boolean;
  inputLabelNumber?: boolean;
  inputNotes?: boolean;
  outputData?: boolean;
  inputNew?: boolean;
  icon?: { icon: string; value: any };
}

export enum HeadFilter {
  dropdownFilter = "dropdownFilter",
  dropdownFilterCustom = "dropdownFilterCustom",
  textFilter = "textFilter",
  popUpFilter = "popUpFilter",
  globalFilter = "globalFilter",
  null = "null",
}

export enum ActionInput {
  dropdownData = "dropdownData",
  inputLabeldata = "inputLabeldata",
  inputLabelNumber = "inputLabelNumber",
  inputNotes = "inputNotes",
  outputData = "outputData",
  inputDate = "inputDate",
  null = "null",
}

export enum ActionOutput {
  outputClickLabelData = "outputClickLabelData",
  outputLabelData = "outputLabelData",
  outputLabelNumber = "outputLabelNumber",
  outputDate = "outputDate",
  actionEmoticonCard = "actionEmoticonCard",
  actionAmount = "actionAmount",
  actionGauge = "actionGauge",
  actionDoubleRowString = "actionDoubleRowString",
  actionDoubleRowNumber = "actionDoubleRowNumber",
  actionMultipleRowString = "actionMultipleRowString",
  actionMultipleRowNumber = "actionMultipleRowNumber",
  actionDetails = "actionDetails",
  icon = "icon",
  outputNotes = "outputNotes",
  null = "null",
}
