import { MenuItem } from "primeng/api";

export interface HeadArray {

        head?,
        subHead?,
        fieldName?,
        actionInput?,
        actionOutput?,
        display?,
        filter?,
        width?,
        colorHeader?,
        flag?: boolean,
        pathIconFlag?,
        sortIcon?: boolean
        tooltip?,
        align?,
        gaugeConfig?,
        linkImg?,
        value?,
        dropdown?: Dropdown;

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

export enum HeadFilter {
        dropdownFilter = "dropdownFilter",
        dropdownFilterCustom = "dropdownFilterCustom",
        textFilter = "textFilter",
        popUpFilter = "popUpFilter",
        globalFilter = "globalFilter",
        null = "null"

}

export enum ActionInput {
        actionEditing = "actionEditing",
        actionDetails = "actionDetails",
        dropdownData = "dropdownData",
        inputLabeldata = "inputLabeldata",
        inputLabelNumber = "inputLabelNumber",
        inputNotes = "inputNotes",
        outputData = "outputData",
        null = "null"
}


export enum ActionOutput {
        outputClickLabelData = "outputClickLabelData",
        outputLabelData = "outputLabelData",
        outputLabelNumber = "outputLabelNumber",
        actionEmoticonCard = "actionEmoticonCard",
        actionAmount = "actionAmount",
        actionGauge = "actionGauge",
        actionGaugeFlex = "actionGaugeFlex",
        actionDoubleRowString = "actionDoubleRowString",
        actionDoubleRowNumber = "actionDoubleRowNumber",
        actionMultipleRowString = "actionMultipleRowString",
        actionMultipleRowNumber = "actionMultipleRowNumber",
        isEditable = "isEditable",
        null = "null"
}
