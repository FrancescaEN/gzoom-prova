import { Component, OnInit } from '@angular/core';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith, switchMap } from 'rxjs';
import { MenuItem } from "primeng/api";
import { Table } from 'primeng/table';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { StandardImportFieldConfigEx } from 'app/api/model/standardImportFieldConfigEx';
import { ActivatedRoute } from '@angular/router';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { StandardImportFieldConfigService } from 'app/api/service/standard-import-field-config.service';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { LoaderService } from 'app/shared/loader/loader.service';
import { StandardImportFieldConfig } from 'app/api/model/standardImportFiledConfig';
import { MsgService } from 'app/commons/service/message.service';
import { DataSourceService } from 'app/api/service/data-source.service';
import { DataSource } from 'app/api/model/dataSource';

@Component({
  selector: 'app-subsystem-details',
  templateUrl: './subsystem-details.component.html',
  styleUrls: ['./subsystem-details.component.css']
})
export class SubsystemDetailsComponent implements OnInit {

  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  dataTable: Table;
  flag: boolean = false;
  loading: boolean = true;
  er: boolean = false;

  headArray: HeadArray[] = [
    {
      head: "",
      fieldName: "id",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      display: "none",
    },
  ];

  gridArray: StandardImportFieldConfigEx[] = [];
  newRow: any;
  secondaryLang: boolean;

  dropdownEnumeration: MenuItem[] = [];

  itemDWSelected: any;

  dataSourceId: string;
  selectedDataSource: DataSource;
  title: string = "";

  constructor(
    private route: ActivatedRoute,
    private readonly usrPreferenceService: UserPreferenceService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly tbService: TableEditingCellService,
    private readonly standardImportFieldConfigService: StandardImportFieldConfigService,
    private readonly enumerationService: EnumerationService,
    private readonly loaderService: LoaderService,
    private readonly dataSourceService: DataSourceService,
    private msgService: MsgService

  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit(): Promise<void> {
    this.loaderService.hide();
    this.secondaryLang = await this.languageService.secondaryLang();

    this.route.paramMap.subscribe(paramMap => {
      this.dataSourceId = paramMap.get('dataSourceId');
    });


    this.selectedDataSource = await lastValueFrom(this.dataSourceService.getDataSourceById(this.dataSourceId));
    this.title = this.selectedDataSource.dataSourceId + " - " + this.selectedDataSource.description;

    const reload = this._reload.pipe(
      mergeMap(() =>
        this.standardImportFieldConfigService.getStandardImportFieldConfigEx(this.dataSourceId)
      )
    )

    this.setHeadArray();

    const dataSourceEx = this.route.data.pipe(
      map((data: { obss: StandardImportFieldConfigEx[] }) => data.obss),
      mergeWith(reload)
    );

    await this.setEnumerationDropdown();

    dataSourceEx.subscribe((data) => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      data.forEach((e) => {

        let ID = e.dataSourceId + e.standardInterface + e.interfaceSeq + e.internalFieldName;

        this.gridArray.push({
          dataSourceId: e.dataSourceId,
          standardInterface: e.standardInterface,
          standardInterfaceDesc: this.secondaryLang ? e.enumeration.descriptionLang : e.enumeration.description,
          interfaceSeq: e.interfaceSeq,
          internalFieldName: e.internalFieldName,
          externalFieldName: e.externalFieldName,
          defaultValue: e.defaultValue,

          variableGridArray: {
            id: ID,
            updated: false,
            buttonDetails: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: false,
            outputData: true,
            inputNew: false,
            dropdownData: true,
          },
        });
      });
      this.loading = false;
    });
  }

  async setHeadArray() {

    this.headArray.push(
      {
        head: "Code",
        fieldName: "dataSourceId",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 20,
        unique: true,
        display: 'none',
      },
      {
        head: "Interface",
        fieldName: "standardInterfaceDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        required: true,
        unique: true,
        textLength: 255,
        width: '10vw',
        dropdown: {
          item: this.dropdownEnumeration,
          clear: true,
          key: "standardInterface",
          command: async () => {
            this.dropdownEnumeration = [];
            this.headArray.filter(x => x.fieldName == 'standardInterfaceDesc').forEach(y => y.dropdown.loading = true);
            await this.setEnumerationDropdown();
            this.headArray
              .filter((x) => x.fieldName == "standardInterfaceDesc")
              .forEach((y) => (y.dropdown.item = this.dropdownEnumeration));
            this.headArray.filter(x => x.fieldName == 'standardInterfaceDesc').forEach(y => y.dropdown.loading = false);
          },
        },
      },
      {
        head: "Sequence",
        fieldName: "interfaceSeq",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelNumber,
        filter: HeadFilter.popUpFilter,
        content: "center",
        unique: true,
        required: true,
        width: '5vw'
      },
      {
        head: "Interface column",
        fieldName: "internalFieldName",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 255,
        unique: true,
        required: true,
      },
      {
        head: "ExternalFieldNameTitle",
        fieldName: "externalFieldName",
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 255,
      },
      {
        head: "Default",
        fieldName: "defaultValue",
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 255,
      },
      {
        head: "",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.null,
        filter: HeadFilter.null,
        width:'0.5vw'
      }
    );
  }

  async setEnumerationDropdown() {
    this.dropdownEnumeration = [];
    const obs$ = this.usrPreferenceService.getUserPreference("ORGANIZATION_PARTY").pipe(
      switchMap((data) =>
        this.enumerationService.enumerations("STD_INT")
      )
    );

    await lastValueFrom(obs$)
      .then((data) => {
        data.forEach((x) => {
          this.dropdownEnumeration.push({
            label: this.secondaryLang ? x.descriptionLang : x.description,
            id: x.enumCode,
          });
        });
      })
      .catch((error) => console.log(error));
  }

  openNew() {
    let ID = "new" + Math.random();
    let tmpElAdd: StandardImportFieldConfigEx = {
      dataSourceId: this.dataSourceId,
      standardInterface: null,
      standardInterfaceDesc: null,
      interfaceSeq: null,
      internalFieldName: null,
      externalFieldName: null,
      defaultValue: null,
      variableGridArray: {
        id: ID,
        updated: true,
        buttonDetails: false,
        inputLabeldata: true,
        inputLabelNumber: true,
        inputNotes: true,
        outputData: false,
        inputNew: true,
        dropdownData: true,
      }

    }

    this.elementToAdd = tmpElAdd;
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
      let obj = new StandardImportFieldConfig();

      obj.dataSourceId = this.dataSourceId,
        obj.standardInterface = e.standardInterface,
        obj.interfaceSeq = e.interfaceSeq,
        obj.internalFieldName = e.internalFieldName,
        obj.externalFieldName = e.externalFieldName,
        obj.defaultValue = e.defaultValue,

        await this.standardImportFieldConfigService.createStandardImportFieldConfig(obj)
          .then((x) => {
            this.msgService.successCreateWithId(obj.dataSourceId);
            let ID = x.dataSourceId + x.standardInterface + x.interfaceSeq + x.internalFieldName;
            e.variableGridArray.id = ID;
            e.variableGridArray.updated = false;
            e.variableGridArray.inputNew = false;
            e.variableGridArray.outputData = true;

            if (this.reload) this._reload.next()
          })
          .catch((error) => {
            this.msgService.errorWithId(error, obj.dataSourceId);
            this.er = true;
          });
    })

    return !this.er;
  }

  update(gridElement) {

    gridElement.forEach(async e => {
      let obj = new StandardImportFieldConfig();

      obj.dataSourceId = e.dataSourceId,
        obj.standardInterface = e.standardInterface,
        obj.interfaceSeq = e.interfaceSeq,
        obj.internalFieldName = e.internalFieldName,
        obj.externalFieldName = e.externalFieldName,
        obj.defaultValue = e.defaultValue,

        await this.standardImportFieldConfigService.updateStandardImportFieldConfig(obj)
          .then(() => {
            this.msgService.successUpdateWithId(obj.dataSourceId);
            this._reload.next()
          })
          .catch((error) => {
            this.msgService.errorWithId(error, obj.dataSourceId);
            this.er = true;
          });
    });
    return !this.er;
  }

  delete(listGridElement: StandardImportFieldConfigEx[]) {
    if (listGridElement.filter(x => x.variableGridArray.id.includes("new")).length > 0) {
      this.newRow = listGridElement.filter(x => x.variableGridArray.id.includes("new"));
      listGridElement = listGridElement.filter(x => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(x => !x.variableGridArray.id.includes("new"));
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      listGridElement.forEach(e => {

        this.standardImportFieldConfigService.deleteStandardImportFieldConfig(e.dataSourceId, e.standardInterface, e.internalFieldName, e.interfaceSeq)
          .then(() => {
            this.msgService.successDelete();
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.error(error.message);
            this._reload.next();
          });

      });

    }
    else {
      this.msgService.successDelete();
    }
  }

  shareDropdownItemRow(item) {
    this.itemDWSelected = item;
  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  
  resetAllElement(){
    this._reload.next();
  }



}
