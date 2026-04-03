import { Component, OnInit, computed, input } from '@angular/core';
import { Subject, lastValueFrom, map, mergeMap, mergeWith } from 'rxjs';
import { MenuItem } from 'primeng/api';
import { ActionInput, ActionOutput, HeadArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { HeadFilter } from 'app/layout/tables/table/table-configuration';
import { QueryConfigService } from 'app/api/service/query-config.service';
import { QueryConfig } from 'app/api/model/queryConfig';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { WorkEffortTypeService } from 'app/api/service/work-effort-type.service';
import { MsgService } from 'app/commons/service/message.service';
import { ToolbarService } from 'app/commons/service/toolbar.service';

@Component({
  selector: 'app-query-configuration',
  templateUrl: './query-configuration.component.html',
  styleUrls: ['./query-configuration.component.css']
})
export class QueryConfigurationComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  loading: boolean = true;
  er: boolean = false;

  headArray: HeadArray[] = [
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: any[] = [];
  newRow: any;
  secondaryLang: boolean;
  queryType = input<string>();
  stateKey = computed(() => {
    const stateRoot = 'query-configuration';
    return this.queryType() ? `${stateRoot}-${this.queryType()}` : stateRoot;
  });

  dropdownY_N: MenuItem[] = [
    { label: this.i18nService.translate('Y'), id: 'Y' },
    { label: this.i18nService.translate('N'), id: 'N' }];
  dropdownQueryType: MenuItem[] = [];
  dropdownExpMimeType: MenuItem[] = [
    { label: 'CSV', id: 'text/csv' },
    { label: 'PDF', id: 'application/pdf' },
    { label: 'XLSX', id: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' },
    { label: 'HTML2PDF', id: 'text/html' },
  ];

  dropdownOrientation: MenuItem[] = [
    { label: this.i18nService.translate('Vertical'), id: 'VERTICAL' },
    { label: this.i18nService.translate('Horizontal'), id: 'HORIZONTAL' },
  ];

  dropdownContext: MenuItem[] = [];

  itemsButtonSlideMenu = [
    {
      label: this.i18nService.translate("Query"),
      icon: "pi pi-angle-right",
      command: () => this.toDetail("query"),
    },
    {
      label: this.i18nService.translate("Conditions"),
      icon: "pi pi-angle-right",
      command: () => this.toDetail("conditions"),
    },
  ];

  selectedIndex;

  constructor(
    private route: ActivatedRoute,
    private readonly queryConfigService: QueryConfigService,
    private readonly i18nService: I18NService,
    private readonly router: Router,
    private readonly languageService: LanguageService,
    private readonly enumerationService: EnumerationService,
    private readonly workEffortTypeService: WorkEffortTypeService,
    private msgService: MsgService,
    private toolbarService: ToolbarService
  ) {
    this._reload = new Subject<void>();
    this.toolbarService.setPrimaryBoardComponentButton();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();

    this.dropdownQueryType = await this.setEnumerationDropdown('QUERY_TYPE');
    await this.setContextDropdown();

    const reload = this._reload.pipe(mergeMap(() => this.queryConfigService.getQueryConfigList(this.queryType())));
    const w$ = this.route.data.pipe(
      map((data: { obss: QueryConfig[] }) => data.obss),
      mergeWith(reload)
    );
    this.setHeadArray();


    w$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      y.forEach((e) => {

        this.gridArray.push({
          queryId: e.queryId,
          queryCode: e.queryCode,
          queryName: e.queryName,
          queryType: e.queryType,
          queryTypeDesc: this.dropdownQueryType.find(x => x.id == e.queryType)?.label,
          exportMimeType: e.exportMimeType,
          exportMimeTypeDesc: this.dropdownExpMimeType.find(x => x.id == e.exportMimeType)?.label,
          queryCtx: e.queryCtx,
          queryCtxDesc: this.dropdownContext.find(x => x.id == e.queryCtx)?.label,
          queryPublic: e.queryPublic,
          queryPublicDesc: ((e.queryPublic) ? this.i18nService.translate(e.queryPublic) : null),
          queryActive: e.queryActive,
          queryActiveDesc: ((e.queryActive) ? this.i18nService.translate(e.queryActive) : null),
          queryColumnsFormatParam: e.queryColumnsFormatParam,
          queryComm: e.queryComm,
          queryInfo: e.queryInfo,
          orientation: e.orientation,
          orientationDesc: this.dropdownOrientation.find(x => x.id == e.orientation)?.label,


          variableGridArray: {
            id: e.queryId,
            updated: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: true,
            buttonMultipleDetails: true
          }

        });
      });
      this.loading = false;

    })

  }

  setHeadArray() {
    this.headArray.push(

      { head: 'Code', fieldName: 'queryCode', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, width: '10vw', textLength: 20 },
      { head: 'Name', fieldName: 'queryName', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, width: '17vw', textLength: 255, },
      {
        head: 'Operation Type', fieldName: 'queryTypeDesc', actionInput: (this.queryType()) ? ActionInput.null : ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, required: true, readonly: (this.queryType() != null), width: '7vw',
        dropdown: {
          item: this.dropdownQueryType,
          clear: false,
          disableSort: false,
          key: 'queryType',
        }
      },
      {
        head: 'Export Type', fieldName: 'exportMimeTypeDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, required: true, width: '3vw',
        dropdown: {
          item: this.dropdownExpMimeType,
          clear: false,
          disableSort: false,
          key: 'exportMimeType'
        }
      },
      {
        head: this.i18nService.translate('Orientation') + " (HTML2PDF)", fieldName: 'orientationDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, required: false, width: '7vw',
        dropdown: {
          item: this.dropdownOrientation,
          clear: true,
          disableSort: false,
          key: 'orientation',
          disableDropdownCommand(itemGridArray) {
            return itemGridArray.exportMimeTypeDesc != "HTML2PDF";
          },
        }
      },
      {
        head: 'Context', fieldName: 'queryCtxDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, required: true, width: '7vw',
        dropdown: {
          item: this.dropdownContext,
          clear: false,
          disableSort: false,
          key: 'queryCtx',
        }
      },
      { head: 'Public', fieldName: "queryPublicDesc", actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, width: '6vw', dropdown: { item: this.dropdownY_N, clear: false, key: 'queryPublic' }, sortIcon: false },
      { head: 'Active', fieldName: "queryActiveDesc", actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.popUpFilter, width: '6vw', dropdown: { item: this.dropdownY_N, clear: false, key: 'queryActive' }, sortIcon: false },
      { head: 'Description', fieldName: 'queryComm', actionInput: ActionInput.inputNotes, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, },
      { head: '', fieldName: 'null', actionInput: ActionInput.null, actionOutput: ActionOutput.actionDetails, width: '5vw', filter: HeadFilter.null },

    );

  }

  shareItemEvent(data) {
    this.selectedIndex = data;
  }

  toDetail(component: string) {
    this.router.navigate([`${this.selectedIndex.queryId}/${component}`], { relativeTo: this.route });
  }


  async setEnumerationDropdown(enumTypeId: string): Promise<MenuItem[]> {
    let tmpDropdown: MenuItem[] = []
    await lastValueFrom(this.enumerationService.enumerations(enumTypeId))
      .then(e => {
        e.forEach(item => tmpDropdown.push({ label: (!this.secondaryLang) ? item.description : item.descriptionLang, id: item.enumId }));
        return tmpDropdown
      }
      ).catch((error) => console.log(error));

    return tmpDropdown;
  }

  async setContextDropdown() {
    this.dropdownContext = [];
    const obs$ = this.workEffortTypeService.getLikeWorkEffortTypeId('CTX%');

    await lastValueFrom(obs$)
      .then((data) => {
        data.forEach((x) => {
          let description = (!this.secondaryLang ? x.description : x.descriptionLang)
          this.dropdownContext.push({
            label: x.workEffortTypeId + ((description) ? " (" + description + ")" : ""),
            id: x.workEffortTypeId,
          });
        });
      })
      .catch((error) => console.log(error));
  }


  openNew() {
    this.elementToAdd = {
      queryId: null,
      queryCode: null,
      queryName: null,
      queryType: this.queryType(),
      queryTypeDesc: this.dropdownQueryType.find(x => x.id == this.queryType())?.label,
      exportMimeType: null,
      exportMimeTypeDesc: null,
      queryCtx: null,
      queryCtxDesc: null,
      queryPublic: 'N',
      queryPublicDesc: 'N',
      queryActive: 'N',
      queryActiveDesc: 'N',
      queryColumnsFormatParam: null,
      queryComm: null,
      queryInfo: null,

      variableGridArray: {
        id: "new" + Math.random(),
        updated: true,
        inputLabeldata: true,
        inputLabelNumber: true,
        outputData: false,
        inputNew: true,
        dropdownData: true,
        inputNotes: true,
      }

    }

    this.gridArray = [this.elementToAdd, ...this.gridArray];


  }

  saveNewAndOpen(gridElement) {
    if (this.create(gridElement)) {
      this.openNew();
    }
    else this.er = false;
  }

  saveAllElement(elementUpdated) {
    let newElement = elementUpdated.filter(x => x.variableGridArray.id.includes("new"));
    if (newElement.length > 0) {
      this.reload = true;
      this.create(newElement);
      this.reload = false;
    }
    elementUpdated = elementUpdated.filter(x => !x.variableGridArray.id.includes("new"));
    if (elementUpdated.length > 0) {

      this.update(elementUpdated);
    }

  }


  create(gridElement): boolean {
    gridElement.forEach(async e => {
      let obj = new QueryConfig(
        e.queryId,
        e.queryCode,
        e.queryName,
        e.queryComm,
        e.queryType,
        e.queryCtx,
        e.queryPublic,
        e.queryActive,
        e.exportMimeType,
        null,
        null,
        e.orientation
      );

      await this.queryConfigService.createQueryConfig(obj)
        .then((newId) => {
          this.msgService.successCreateWithId(obj.queryCode);
          e.queryId = newId
          e.variableGridArray.id = newId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonMultipleDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.queryCode);
          this.er = true;
        });

    })
    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new QueryConfig(
        e.queryId,
        e.queryCode,
        e.queryName,
        e.queryComm,
        e.queryType,
        e.queryCtx,
        e.queryPublic,
        e.queryActive,
        e.exportMimeType,
        null,
        null,
        e.orientation
      );

      await this.queryConfigService.updateQueryConfigInfoBase(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.queryCode);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.queryCode);
          this.er = true;
        });
    });

    return !this.er;


  }

  delete(listGridElement) {
    if (listGridElement.filter(x => x.variableGridArray.id.includes("new")).length > 0) {
      this.newRow = listGridElement.filter(x => x.variableGridArray.id.includes("new"));
      listGridElement = listGridElement.filter(x => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(x => !x.variableGridArray.id.includes("new"));
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      listGridElement.forEach(e => {

        this.queryConfigService.deleteQueryConfig(e.queryId)
          .then(() => {
            this.msgService.successDeleteWithId(e.queryCode);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.queryCode);
            this._reload.next();
          });

      });

    }
    else {
      this.msgService.successDelete();

    }

  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  resetAllElement() {
    this._reload.next();
  }


}
