import { AfterContentChecked, ChangeDetectorRef, Component, EventEmitter, Input, InputSignal, OnInit, Output, SimpleChanges, input } from '@angular/core';
import { Message as MessageError } from 'primeng/api';
import { Message } from 'app/commons/model/message';
import { Table } from 'primeng/table';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { HeadArray } from '../tables/table-editing-cell/table-editing-cell-configuration';
import { shareFile } from '../upload/upload';
import { Filter, InfoPage } from '../tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';
import { DataForButton } from '../toolbar-data-table/toolbar-data-table-configuration';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { MsgService } from 'app/commons/service/message.service';
import { ToolbarService } from 'app/commons/service/toolbar.service';

@Component({
  selector: 'app-primary-board',
  templateUrl: './primary-board.component.html',
  styleUrls: ['./primary-board.component.css']
})
export class PrimaryBoardComponent implements OnInit, AfterContentChecked {
  /************************************************** TABLE-EDITING-CELL INPUT start */
  // Array containing the elements of the head of the table
  @Input() headArray: HeadArray[] = [];
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
  @Input() selectedOn: boolean = true;
  // Variable to disable the filter head
  @Input() headDisplay: string;
  // Boolean variable to enable double click on the line
  @Input() editRowDblClick: boolean
  // Boolean variable to enable load spinner in the body
  @Input() loading: boolean = false;
  // Boolean variable to enable load spinner of table lazy mode in the body
  @Input() loadingLazy: boolean = false;
  // Variable to set scrollHeight
  @Input() scrollHeight: any;

  @Input() selectFile: any;
  @Input() paginationEnabled: Boolean = false;

  @Input() totalRecords: Number;

  stateKey = input<string>();

  /*------------------------------------------------- TABLE-EDITING-CELL INPUT end */

  /************************************************** TABLE-EDITING-CELL OUTPUT start */
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

  @Output() myUpload = new EventEmitter<shareFile>();

  @Output() selectFileLoad = new EventEmitter<shareFile>();

  @Output() selectedImage = new EventEmitter<any>();
  /*------------------------------------------------- TABLE-EDITING-CELL OUTPUT end */

  /************************************************** PRIMARY-BOARD INPUT start */
  @Input() collapseSidebar: boolean = false; // Variable to set collapse side bar
  // @Input() messagesError: MessageError[] = [];
  //  @Input() messages: Message[] = [];
  @Input() elementToAdd: any;
  @Input() buttonBack: boolean = false;
  @Input() buttonNew: boolean = true;
  @Input() buttonReset: boolean = true;
  @Input() title: string;
  @Input() bldx: DataForButton;
  @Input() backLink: string;
  @Input() backLinkRelToThisRoute: string;
  @Input() menuDetails: boolean = false;
  /*------------------------------------------------- PRIMARY-BOARD INPUT end */
  /************************************************** PRIMARY-BOARD OUTPUT start */
  @Output() openNew = new EventEmitter<any>();
  @Output() saveNewAndOpen = new EventEmitter<any>();
  @Output() saveAllElement = new EventEmitter<any>();
  @Output() resetAllElement = new EventEmitter<any>();
  @Output() delete = new EventEmitter<any>();
  @Output() shareDropdownItemRow = new EventEmitter<any>();
  @Output() shareInfoPaginationEvent = new EventEmitter<any>();
  @Output() loadData = new EventEmitter<any>();
  @Output() bldxEvent = new EventEmitter<any>();
  /*------------------------------------------------- PRIMARY-BOARD OUTPUT end */


  messagesErrorPBoard: MessageError[] = [];
  buttonDelete: boolean = true;
  buttonSave: boolean = true;
  msgs: Message[] = [];
  selectionList: any[] = [];
  dataTable: Table;
  mandatoryFieldName: string[] = [];
  uniqueFieldName: string[] = [];
  newRow: any[] = [];

  filters: Filter[] = [];
  infoCurrentPage: InfoPage = { offset: 50, limit: 0 };

  resetTable: boolean = false;

  constructor(
    private readonly tbService: TableEditingCellService,
    private confirmDialogService: ConfirmDialogService,
    private msgService: MsgService,
    private toolbarService: ToolbarService,
    private cd: ChangeDetectorRef
  ) { }

  ngAfterContentChecked(): void {
    this.cd.detectChanges();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes) {
      if (changes["gridArray"]) {
        if (this.gridArray.filter(x => x.variableGridArray.updated == true).length == 0) { this.toolbarService.setDisabledSave(true) }
        else this.toolbarService.setDisabledSave(false);
        this.loadingLazy = false;
        this.resetTable = false;
      }

      /* if (changes["messages"]) {
         if (this.messages.length > 0) this.msgService.success(this.messages[0].detail);
         if (this.gridArray.filter(x => x.variableGridArray.updated == true).length == 0) { this.toolbarService.setDisabledSave(true) }
       }*/

      if (changes["elementToAdd"]) {
        if (this.elementToAdd) {
          this.editingKeyId = this.elementToAdd.variableGridArray.id;
          this.dataTable.editingRowKeys = { [this.editingKeyId]: true };
        }

      }
    }
  }

  ngOnInit(): void {
    if (this.collapseSidebar) this.collapseSidebarFun();

  }


  loadGrid(data) {

    this.loadingLazy = true;
    this.gridArray = [];
    this.filters = []

    if (data.field) {

      if(data.$event instanceof  Date){
        this.filters.push({ field: data.field, value:  data.$event ?? null, matchMode: null })
      }else{
        this.filters.push({ field: data.field, value: data.$event.value ?? null, matchMode: null })
      }
      
      this.infoCurrentPage.filter = this.filters;
    }
    else if (data.$event) {

      if (data.$event.sortOrder) {
        this.infoCurrentPage.sortOrder = data.$event.sortOrder;
        this.infoCurrentPage.sortField = data.$event.sortField;
      }

      if (data.$event.filters) {
        this.infoCurrentPage.limit = data.$event.rows;
        this.infoCurrentPage.offset = data.$event.first;

        this.headArray.forEach(item => {
          if (data.$event.filters[item.fieldName]) {
            if (!!data.$event.filters[item.fieldName].value) {
              this.filters.push({ field: item.fieldName, value: data.$event.filters[item.fieldName].value ?? null, matchMode: data.$event.filters[item.fieldName].matchMode })
            } else {
              this.filters.push({ field: item.fieldName, value: null, matchMode: data.$event.filters[item.fieldName].matchMode })
            }
          }
        })

        this.infoCurrentPage.filter = this.filters;

      } else {

        this.infoCurrentPage.filter = [];
        this.infoCurrentPage.limit = data.$event.rows;
        this.infoCurrentPage.offset = data.$event.first;
      }
    } else {
      this.infoCurrentPage.filter = [];
      this.infoCurrentPage.limit = 50;
      this.infoCurrentPage.offset = 0;
    }
    this.loadData.emit(this.infoCurrentPage);
  }

  shareDescriptorTablePBoard(data: Table) {
    this.dataTable = data;
    this.shareDescriptorTable.emit(data);
  }

  shareInfoPagination(data) {
    this.shareInfoPaginationEvent.emit(data);
  }

  notifyInputChangesPBoard(id) {
    this.gridArray.forEach(x => {
      if (x.variableGridArray.id == id) {
        x.variableGridArray.updated = true;
      }
    });

    setTimeout(() => { this.toolbarService.setDisabledSave(false) }, 0);

    this.notifyInputChanges.emit(id);
  }

  shareSelectionItemPBoard(data) {
    this.selectionList = data;

    if (this.selectionList.length > 0) {
      setTimeout(() => this.toolbarService.setDisabledDelete(false), 0);
      //setTimeout(() => { this.buttonDelete = true; }, 0);
    }
    else
      setTimeout(() => this.toolbarService.setDisabledDelete(true), 0);
    //setTimeout(() => { this.buttonDelete = false; }, 0);

    this.shareSelectionItem.emit(data);
  }

  collapseSidebarFun() {
    const dom: any = document.querySelector('body');
    const menu: any = document.querySelector('#sidebar');
    dom.classList.add('push-right');
    menu.classList.add('collapse');
  }

  openNewPBoard() {
    //Retrieves required fields that have the required attribute equal to true from the headArray
    this.mandatoryFieldName = this.headArray.filter(x => x.required == true).map(y => (y.dropdown) ? y.dropdown.key : y.fieldName);
    //Retrieves unique fields that have the unique attribute equal to true from the headArray
    this.uniqueFieldName = this.headArray.filter(x => x.unique == true).map(y => (y.dropdown) ? y.dropdown.key : y.fieldName);

    //If a new item already exists save it before adding another one
    let newElement = this.gridArray.filter(x => x.variableGridArray.id.includes("new"));
    if (newElement.length > 0) {
      //If all required fields have been filled in
      if (this.tbService.validGridArray(newElement, ...this.mandatoryFieldName)) {
        //if there are no duplicate IDs
        if (this.tbService.hasDuplicateIds(this.gridArray, ...this.uniqueFieldName)) {

          this.saveNewAndOpen.emit(newElement);

        }
        else this.msgService.errorExistingRecord();

      }
      else
        this.msgService.errorFieldsRequired();


    } else this.openNew.emit();


  }

  saveAllElementPBoard() {
    //Retrieves required fields that have the required attribute equal to true from the headArray
    this.mandatoryFieldName = this.headArray.filter(x => x.required == true).map(y => (y.dropdown) ? y.dropdown.key : y.fieldName);
    //Retrieves unique fields that have the unique attribute equal to true from the headArray
    this.uniqueFieldName = this.headArray.filter(x => x.unique == true).map(y => (y.dropdown) ? y.dropdown.key : y.fieldName);

    let elementUpdated = this.gridArray.filter(x => x.variableGridArray.updated);
    if (this.tbService.validGridArray(elementUpdated, ...this.mandatoryFieldName)) {

      if (this.tbService.hasDuplicateIds(this.gridArray, ...this.uniqueFieldName)) {
        this.saveAllElement.emit(elementUpdated)
      }
      else this.msgService.errorExistingRecord();
    }
    else this.msgService.errorFieldsRequired();
  }

  deleteRowSelected() {
    this.confirmDialogService.delete().then(x => {
      if (x) {
        this.delete.emit(this.selectionList);
        this.dataTable.updateEditingCell(null, null, null, null);
      }
    });
  }

  resetAllAction() {
    this.resetTable = true
    this.resetAllElement.emit()
  }

  shareDropdownItemRowPBoard(data) {
    this.shareDropdownItemRow.emit(data);
  }

  buttonShareItemEventPBoard(data) {
    this.buttonShareItemEvent.emit(data);
  }

  onClickButtonDetailsPBoard(data) {
    this.clickButtonDetailsEvent.emit(data);
  }

  myUploader(value: shareFile) {
    this.myUpload.emit(value);
  }

  selectFileLoadPBoard(event) {
    this.selectFileLoad.emit(event);
  }

  selectedImageFun(event: { image: { src: string, alt: string }, item }) {
    this.notifyInputChangesPBoard(event.item.variableGridArray.id)
    this.selectedImage.emit(event);
  }

  bldxEventPBoard() {
    this.bldxEvent.emit();
  }

}
