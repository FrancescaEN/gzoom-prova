import { MenuItem } from "primeng/api";


export interface InfoPage {
  offset?: Number,
  limit?: Number,
  filter?: Filter[]
  filterGenericLabel?: Filter[]
  sortOrder?: Number;
  sortField?: string;
  organizationId?: string;
  secondaryLang?: boolean;
  matchModeSearch?: string;
}


export interface HeadArray {
  head?;
  subHead?;
  fieldName?;
  actionInput?;
  actionOutput?;
  display?;
  filter?;
  width?;
  inputWidth?;
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
  clearCalendar?: boolean;
  readonly?: boolean;
  iconDetail?: string;
}

export interface Dropdown {
  id?: string;
  fieldName?: string;
  item?: MenuItem[]; //This menu item can be used in the case of multiple dropdowns within the table
  clear?: boolean; // If true, it allows you to clean the dropdown
  key?;
  loading?: boolean;
  disableSort?: boolean;
  virtualScrollItemSize?: number;
  virtualScroll?: boolean;
  disabled?: boolean;
  disableDropdownCommand?: (event?: any) => boolean;
  command?: (event?: any) => void;
}

export interface VariableGridArray {
  id?: string;
  updated?: boolean;
  buttonDetails?: boolean;
  loadingButtonDetails?: boolean;
  buttonMultipleDetails?: boolean;
  buttonMultipleLinkMenu?: boolean;
  dropdownData?: boolean;
  inputLabeldata?: boolean;
  inputLabelNumber?: boolean;
  inputLabelDecimalNumber?: boolean;
  inputLabelPercentDecimalNumber?: boolean;
  inputNotes?: boolean;
  outputData?: boolean;
  inputNew?: boolean;
  icon?: { icon: string; value: any };
  isHtml?: boolean;
  maxFractionDigits?: number;
  minFractionDigits?: number;
  upload?: boolean;
  selectFile?: boolean;
  stateSeverity?: TagSeverity;
  stateValue?: string;
  dateFormat?: string;
  
}

export enum TagSeverity {
  SUCCESS = "success",
  INFO = "info",
  WARNING = "warning",
  DANGER = "danger"
}

export enum HeadFilter {
  dropdownFilter = "dropdownFilter",
  dropdownFilterCustom = "dropdownFilterCustom",
  textFilter = "textFilter",
  popUpFilter = "popUpFilter",
  globalFilter = "globalFilter",
  dateFilter = "dateFilter",
  null = "null",
}

export enum ActionInput {
  dropdownData = "dropdownData",
  autoCompleteDropdown = "autoCompleteDropdown",
  inputLabeldata = "inputLabeldata",
  inputLabelNumber = "inputLabelNumber",
  inputLabelDecimalNumber = "inputLabelDecimalNumber",
  inputLabelPercentDecimalNumber = "inputLabelPercentDecimalNumber",
  inputNotes = "inputNotes",
  outputData = "outputData",
  inputDate = "inputDate",
  inputHTML = "inputHTML",
  outputList = "outputList",
  null = "null",
}

export enum ActionOutput {
  outputClickLabelData = "outputClickLabelData",
  outputLabelData = "outputLabelData",
  outputLabelNumber = "outputLabelNumber",
  outputLabelPercentNumber= "outputLabelPercentNumber",
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
  outputHTML = "outputHTML",
  fileUpload = "fileUpload",
  selectFile = "selectFile",
  tag = "tag",
  outputList = "outputList",
  outputDialogNote = "outputDialogNote",
  null = "null"
}


export class Filter {
  field?: string;
  value?: string;
  matchMode?: string;
  secondValue?: string;


  constructor(field: string, value: string, matchMode: string, secondValue?: string) {
    this.field = field;
    this.value = value;
    this.matchMode = matchMode;
    this.secondValue = secondValue;
  }
}
