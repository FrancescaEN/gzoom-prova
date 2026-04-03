import {
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  InputSignal,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
  computed,
  input,
} from "@angular/core";
import { Table } from "primeng/table";
import {
  HeadFilter,
  ActionInput,
  ActionOutput,
} from "./table-editing-cell-configuration";
import { I18NService } from "app/i18n/i18n.service";
import { TableEditingCellService } from "app/commons/service/table-editing-cell.service";
import { shareFile } from "../../upload/upload";
import { FilterService } from "primeng/api";
import { doNothing } from "app/commons/utils/doNothing";

@Component({
  selector: "app-table-editing-cell",
  templateUrl: "./table-editing-cell.component.html",
  styleUrls: ["./table-editing-cell.component.scss"],
})
export class TableEditingCellComponent implements OnInit, OnChanges, AfterViewInit {
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

  filteredGridArray: any[] = [];

  dateFormat = String;
  dateFormatLabel = String;

  // Contiene l'indice della prima colonna che contiene un campo di input
  firstIndexInput: number;
  // Contiene l'indice dell'ultima colonna che contiene un campo di input
  lastIndexInput: number;

  textOutputDialog: string;
  titleOutputDialog: string;

  // Array containing the elements of the head of the table
  @Input() headArray: any[] = [];
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
  // Array containing the elements of the custom link menu specific for each row enabled
  @Input() itemsButtonLinkMenu: any[] = [];
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

  @Input() placeholder: string;

  @Input() selectFile: any;

  @Input() resetTable: boolean = false;

  stateKey = input<string>();

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
  @Output() shareDescriptorTable = new EventEmitter<Table>();
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
  @Output() myUpload = new EventEmitter<shareFile>();
  @Output() selectFileLoad = new EventEmitter<shareFile>();
  @Output() selectedImage = new EventEmitter<any>();


  itemDropdown: any[] = [];

  @ViewChild("dtec") private dataTable: Table;

  constructor(
    private filterService: FilterService,
    private readonly i18nService: I18NService,
    private readonly tableEditingCellService: TableEditingCellService
  ) { }

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
        this.totalRecords = this.gridArray.length;
      }
      if (changes["headArray"]) {
        if (this.headArray.length > 0) {
          //SORT DROPDOWN
          this.headArray.forEach((x) => {
            if (x.dropdown && !x.dropdown.disableSort)
              x.dropdown.item.sort((a, b) => {
                if (!!a.label && !!b.label) {
                  const nameA = a.label.toUpperCase(); // ignore upper and lowercase
                  const nameB = b.label.toUpperCase(); // ignore upper and lowercase
                  if (nameA < nameB) {
                    return -1;
                  }
                  if (nameA > nameB) {
                    return 1;
                  }
                  // names must be equal
                  return 0;
                } else return 0;
              });
          });
        }
      }

      if (changes["resetTable"]) {
        if (this.resetTable && this.dataTable.editingCell) {
          this.dataTable.editingCell = null;
          this.resetTable = false;
        }
      }

      // Retrieving the indices of the first and last elements that an input can take
      const firstRow = this.gridArray[0];
      const indexColumnInput: number[] = this.headArray
        .map((x, index) => !(x.actionInput === ActionInput.null || (x.actionInput === ActionInput.outputData && firstRow.variableGridArray.outputData == true)) ? index : null)
        .filter(y => y != null);

      [this.firstIndexInput, this.lastIndexInput] = indexColumnInput.length > 0
        ? [indexColumnInput[0], indexColumnInput.pop()]
        : [null, null];
      // ----------------------------------------------------------------------------

    }
  }

  ngOnInit(): void {
    this.dateFormat = this.i18nService.getFormat("date-short-pcalendar");
    this.dateFormatLabel = this.i18nService.getFormat("date-short");


    this.totalRecords = this.gridArray.length;

    this.filterService.register("isNull", (value, filter): boolean => {

      if (filter === undefined || filter === null || filter.trim() === '') {
        return true;
      }

      if (filter === "isNull" && (value === undefined || value === null)) {
        return true;
      }

      return filter === value;

    });

  }

  onFilter(event) {

    let newElement = this.gridArray.filter(x => x.variableGridArray?.id.includes("new"));
    if (newElement.length > 0 && this.dataTable.filteredValue?.length > 0) {

      this.dataTable.filteredValue = [newElement[0], ... this.dataTable.filteredValue];
    }
  }

  reloadDropdown(headFieldName, item) {
    this.shareDropdownItemRow.emit(item);
    this.headArray.forEach((x) => {
      if (x.fieldName == headFieldName && x.dropdown.command)
        x.dropdown.command();

      if (x.filter == "dropdownFilter") {
        let tempArray = [];
        let tempArrayControl = [];

        this.gridArray.forEach((e) => {
          if (!tempArrayControl.includes(e[x.fieldName])) {
            this.typeDropdownFilterArray.set(x.fieldName, []);

            if (e[x.fieldName] != undefined) {
              tempArrayControl.push(e[x.fieldName]);
              tempArray.push({
                label: e[x.fieldName],
                value: e[x.fieldName],
              });
            }

          }
        });

        if (!x.required) {
          tempArray.push({ label: this.i18nService.translate('emptyField'), value: 'isNull' });
        }

        this.typeDropdownFilterArray.set(x.fieldName, tempArray);
      }
    });

  }

  reloadDropdownAutoComplete(headFieldName, item, event?) {
    this.shareDropdownItemRow.emit(item);
    if (!!event && !!event.query && event.query.length >= 3) {
      this.headArray.forEach((x) => {
        if (x.fieldName == headFieldName && x.dropdown.command)
          x.dropdown.command(event.query, headFieldName);
      });
    }
  }

  reloadDropdownRow(headFieldName, item) {
    this.shareDropdownItemRow.emit(item);
    this.gridArray
      .filter((x) => x.dropdown[headFieldName])[0]
      .dropdown[headFieldName].command();
  }

  dropdownInputChanges(index, row) {
    this.selectedItemDropdown.emit(this.itemDropdown[row]);
    this.selectedItemDropdown.emit(index);
  }

  dropdownChanges(fieldName, id) {
    let keyName = this.headArray
      .filter((x) => x.fieldName == fieldName)
      .map((y) => y.dropdown.key)[0];

    let label = this.headArray
      .filter((x) => x.fieldName == fieldName)
      .map((y) => y.dropdown.item)[0]
      .filter(
        (w) =>
          w.id ==
          this.gridArray.filter((x) => x.variableGridArray.id == id)[0][keyName]
      )
      .map((z) => z.label)[0];

    this.gridArray.filter((x) => x.variableGridArray.id == id)[0][fieldName] =
      label;
  }

  dropdownChangesRow(fieldName, id) {
    let keyName = this.gridArray.filter((x) => x.variableGridArray.id == id)[0]
      .dropdown[fieldName].key;
    let label = this.gridArray
      .filter((x) => x.variableGridArray.id == id)[0]
      .dropdown[fieldName].item.filter(
        (w) =>
          w.id ==
          this.gridArray.filter((x) => x.variableGridArray.id == id)[0][keyName]
      )
      .map((z) => z.label)[0];

    this.gridArray.filter((x) => x.variableGridArray.id == id)[0][fieldName] =
      label;
  }

  autoCompleteDropdownChanges(fieldName, id) {
    let keyName = this.headArray
      .filter((x) => x.fieldName == fieldName)
      .map((y) => y.dropdown.key)[0];

    let label = this.headArray
      .filter((x) => x.fieldName == fieldName)
      .map((y) => y.dropdown.item)[0]
      .filter(
        (w) =>
          w.id == this.gridArray.filter((x) => x.variableGridArray.id == id)[0][keyName]?.id
      )
      .map((z) => z.label)[0];

    this.gridArray.filter((x) => x.variableGridArray.id == id)[0][fieldName] = label;
  }

  onTimeChange(index) {
    this.notifyInputChanges.emit(index);
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

  dblclickRow(data) {
    this.dblclickRowEvent.emit(data);
  }

  onClickButtonDetails(data) {
    this.gridArray.filter(x => x.variableGridArray.id === data.variableGridArray.id)[0].variableGridArray.loadingButtonDetails = true;
    this.clickButtonDetailsEvent.emit(data);

  }

  ngAfterViewInit() {
    setTimeout(() => { this.shareDescriptorTable.emit(this.dataTable) }, 10);
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

  setFilterDate(value, id) {
    if (value != null)
      this.dataTable.filters[id][0].value = value;
  }

  onClickShareItemEvent(data) {
    this.buttonShareItemEvent.emit(data);
  }

  myUploader(file, item) {

    this.myUpload.emit({ file: file, item: item });
  }

  selectFileOpen(event) {
    this.selectFileLoad.emit(event);
  }

  selectedImageFun(image, item) {
    this.selectedImage.emit({ image: image, item: item })
  }

  doNothing(e: any) {
    return doNothing(e);
  }

  openOutputDialog(text, title) {
    this.textOutputDialog = text;
    this.titleOutputDialog = this.i18nService.translate(title);
  }
}
