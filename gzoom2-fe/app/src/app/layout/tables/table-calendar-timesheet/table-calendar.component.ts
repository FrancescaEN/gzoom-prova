import { ViewportScroller } from "@angular/common";
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
} from "@angular/core";
import { FormArray, FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { EditableColumn, Table } from "primeng/table";
import {
  ActionInput,
  ActionOutput,
  HeadArray,
  HeadFilter,
} from "./table-calendar-configuration";
import { I18NService } from "app/i18n/i18n.service";
import { Head } from "rxjs";

@Component({
  selector: "app-table-calendar",
  templateUrl: "./table-calendar.component.html",
  styleUrls: ["./table-calendar.component.css"],
})
export class TableCalendarComponent implements OnInit, OnChanges {
  // Boolean variable indicating the editing status of a row
  editNow: boolean = false;
  // Array containing the available languages
  languages: string[] = [];
  // Array containing the selected rows
  selectedItem: any[] = [];
  // Boolean variable that indicates if I'm selecting all rows
  selectAll: boolean = false;
  // Total number of rows
  totalRecords: number;
  // Array containing the elements of the automatic dropdown filter
  typeDropdownFilterArray = new Map();

  headFilter = HeadFilter;

  actionInput = ActionInput;

  actionOutput = ActionOutput;

  dateFormat = String;
  dateFormatLabel = String;

  // Array containing the elements of the head of the table
  @Input() headWorkEffortArray: HeadArray[] = [];
  @Input() headRateTypeArray: HeadArray[] = [];

  @Input() numberArray: HeadArray[] = [];

  @Input() totalHoursRowArray: HeadArray[] = [];

  @Input() footerArray: HeadArray[] = [];
  // Array containing the elements of the table body
  @Input() gridArray: any[] = [];
  // Filter names array (optional)
  @Input() filterArray: string[] = [];
  // Boolean variable to enable table editing
  @Input() isAction: boolean;
  // Array containing the elements of the custom dropdown filter
  @Input() typeDropdownInputArray: Map<string, any>;
  // Array containing the elements of the custom details menu specific for each row enabled
  @Input() itemsButtonSlideMenu: any[] = [];
  // Boolean variables indicating whether we are adding a new line
  @Input() isNewItem: boolean = false;
  // Variable that indicates whether the selection is enabled for single or multiple lines
  @Input() selectionMode: string;
  // Variable that indicates which page to go to on click
  @Input() clickFrameToGo: string;
  // Variable indicating whether the row is expanded or not
  @Input() rowExpand: boolean = false;
  // Variable indicating the id of the row to modify or of the new row
  @Input() editingKeyId: string;
  // Variable that indicates whether to display the icon flag in the head
  @Input() flag: boolean;
  // Boolean variable that indicates if the selection is active
  @Input() selectedOn: boolean;
  // Variable to disable the filter head
  @Input() headDisplay: string;
  // Boolean variable to enable double click on the line
  @Input() editRowDblClick: boolean;
  // Boolean variable to enable load spinner in the body
  @Input() loading: boolean;
  // Variable to set scrollHeight
  @Input() scrollHeight: any;

  // item change event
  @Output() buttonEditEvent = new EventEmitter<any>();
  // item deletion event
  @Output() buttonDeleteRowEvent = new EventEmitter<any>();
  // item save event
  @Output() buttonEditSaveEvent = new EventEmitter<any>();
  // element modification cancellation event
  @Output() buttonEditCancelEvent = new EventEmitter<any>();
  // line number sharing event
  @Output() buttonShareItemEvent = new EventEmitter<any>();
  // link click event
  @Output() clickLinkEvent = new EventEmitter<any>();
  // click event on the selected row
  @Output() clickRowSelectEvent = new EventEmitter<any>();
  // unclick event on selected row
  @Output() clickRowUnselectEvent = new EventEmitter<any>();
  // table descriptor sharing event
  @Output() shareDescriptorTable = new EventEmitter<any>();
  // selected items sharing event
  @Output() shareSelectionItem = new EventEmitter<any>();
  // double click event on the selected row
  @Output() dblclickRowEvent = new EventEmitter<any>();
  // user input write start notification event
  @Output() notifyInputChanges = new EventEmitter<any>();
  // user input write start notification event
  @Output() clickButtonDetailsEvent = new EventEmitter<any>();

  @Output() selectedItemDropdown = new EventEmitter<any>();
  @Output() shareDropdownItemRow = new EventEmitter<any>();

  @Output() onBlurCell = new EventEmitter<any>();

  itemDropdown: any[] = [];

  @ViewChild("dt") private dataTable: Table;

  constructor(private readonly i18nService: I18NService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes) {
      if (changes["gridArray"]) {
        this.selectedItem = this.selectedItem.filter(
          (x) =>
            this.gridArray.filter(
              (y) => y.variableGridArray.id == x.variableGridArray.id
            ).length > 0
        );

        this.shareSelectionItem.emit(this.selectedItem);
        this.itemDropdown = new Array(this.gridArray.length);
      }
    }
  }

  ngOnInit(): void {
    this.dateFormat = this.i18nService.getFormat("date-short-pcalendar");
    this.dateFormatLabel = this.i18nService.getFormat("date-short");
    this.totalRecords = this.gridArray.length;

  }

  // onEditComplete(event) {
  //   this.onEditCompleteCell.emit(event);
  // }

  onBlur(id, item) {
    this.onBlurCell.emit({ id, item });
  }

  reloadDropdown(headFieldName, item) {
    this.shareDropdownItemRow.emit(item);
    this.headRateTypeArray.forEach((x) => {
      if (x.fieldName == headFieldName && x.dropdown.command)
        x.dropdown.command();
    });
  }

  dropdownInputChanges(index, row) {
    this.selectedItemDropdown.emit(this.itemDropdown[row]);
    this.selectedItemDropdown.emit(index);
  }

  dropdownChanges(fieldName, row) {
    if (fieldName == "workEffortName") {
      let keyName = this.headWorkEffortArray
        .filter((x) => x.fieldName == fieldName)
        .map((y) => y.dropdown.key)[0];

      let label = this.headWorkEffortArray
        .filter((x) => x.fieldName == fieldName)
        .map((y) => y.dropdown.item)[0]
        .filter((w) => w.id == this.gridArray[row][keyName])
        .map((z) => z.label)[0];

      this.gridArray[row][fieldName] = label;
    } else if (fieldName == "rateTypeIdDescription") {
      let keyName = this.headRateTypeArray
        .filter((x) => x.fieldName == fieldName)
        .map((y) => y.dropdown.key)[0];

      let label = this.headRateTypeArray
        .filter((x) => x.fieldName == fieldName)
        .map((y) => y.dropdown.item)[0]
        .filter((w) => w.id == this.gridArray[row][keyName])
        .map((z) => z.label)[0];

      this.gridArray[row][fieldName] = label;
    }
  }

  onTimeChange(index, item) {
    this.notifyInputChanges.emit({ index, item });
  }

  onSelectionChange(values) {
    if (this.selectedOn) {
      this.selectAll = values.length === this.totalRecords;
      this.selectedItem = values;
      this.shareSelectionItem.emit(values);
    }
  }

  onSelectAllChange(event) {
    if (this.selectedOn) {
      const checked = event.checked;
      if (checked) {
        this.selectedItem = this.gridArray;
        this.selectAll = true;
        this.shareSelectionItem.emit(this.selectedItem);
      } else {
        this.selectedItem = [];
        this.selectAll = false;
        this.shareSelectionItem.emit(this.selectedItem);
      }
    }
  }

  onRowSelect(data) {
    if (this.rowExpand) {
      for (let i = 0; i < this.dataTable.rows; i++) {
        if (data.data.idNumber != i) {
          this.dataTable.expandedRowKeys = { [i]: false };
        } else {
          this.dataTable.expandedRowKeys = { [i]: true };
        }
      }
    }
    this.clickRowSelectEvent.emit(data);
  }

  onRowUnselect(data) {
    this.clickRowUnselectEvent.emit(data);
  }

  dblclickRow(data1, data2) {
    this.dblclickRowEvent.emit([data1, data2]);
  }

  onClickButtonDetails(data) {
    this.clickButtonDetailsEvent.emit(data);
  }

  ngAfterViewInit() {
    setTimeout(() => this.shareDescriptorTable.emit(this.dataTable), 10);
  }

  customSort(event) {
    event.data.sort((data1, data2) => {
      if (
        data1.variableGridArray.id.includes("new") ||
        data2.variableGridArray.id.includes("new")
      ) {
        return 0;
      } else {
        if (event.order === -1) {
          if (
            typeof data1[event.field] === "string" &&
            typeof data2[event.field] === "string"
          ) {
            return data1[event.field].toLowerCase() >
              data2[event.field].toLowerCase()
              ? 1
              : -1;
          } else if (data1[event.field] != null && data2[event.field] != null) {
            return data1[event.field] > data2[event.field] ? 1 : -1;
          } else if (data1[event.field] == null && data2[event.field] != null) {
            return 1;
          } else if (data1[event.field] != null && data2[event.field] == null) {
            return -1;
          }
        }
        if (event.order === 1) {
          if (
            typeof data1[event.field] === "string" &&
            typeof data2[event.field] === "string"
          ) {
            return data1[event.field].toLowerCase() <
              data2[event.field].toLowerCase()
              ? 1
              : -1;
          } else if (data1[event.field] != null && data2[event.field] != null) {
            return data1[event.field] < data2[event.field] ? 1 : -1;
          } else if (data1[event.field] == null && data2[event.field] != null) {
            return -1;
          } else if (data1[event.field] != null && data2[event.field] == null) {
            return 1;
          }
        }
      }
    });
  }

  onClickShareItemEvent(data) {
    this.buttonShareItemEvent.emit(data);
  }

  clickShareDropdownItemRow(data) {
    //this.shareDropdownItemRow.emit(data);
  }

  isColumnFrozen(head): boolean {
    if (head.fieldName == "workEffortName") {
      return true;
    }
    if (head.fieldName == "rateTypeIdDescription") {
      return true;
    }
    return false;
  }
}
