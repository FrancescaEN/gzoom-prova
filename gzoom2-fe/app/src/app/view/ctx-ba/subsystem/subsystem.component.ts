import { Component, OnInit } from '@angular/core';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith, switchMap } from 'rxjs';
import { MenuItem } from "primeng/api";
import { Table } from 'primeng/table';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { DataSourceEx } from 'app/api/model/dataSourceEx';
import { ActivatedRoute, Router } from '@angular/router';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { LoaderService } from 'app/shared/loader/loader.service';
import { DataSourceService } from 'app/api/service/data-source.service';
import { DataSourceTypeService } from 'app/api/service/dataSourceType.service';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { DataSource } from 'app/api/model/dataSource';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-subsystem',
  templateUrl: './subsystem.component.html',
  styleUrls: ['./subsystem.component.css']
})
export class SubsystemComponent implements OnInit {

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

  gridArray: DataSourceEx[] = [];
  newRow: any;
  secondaryLang: boolean;

  dropdownDataSourceType: MenuItem[] = [];
  dropdownEnumeration: MenuItem[] = [];

  itemDWSelected: any;

  constructor(
    private route: ActivatedRoute,
    private readonly router: Router,
    private readonly dataStorageService: DataStorageService,
    private readonly usrPreferenceService: UserPreferenceService,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly dataSourceTypeService: DataSourceTypeService,
    private readonly enumerationService: EnumerationService,
    private readonly dataSourceService: DataSourceService,
    private readonly loaderService: LoaderService,
    private msgService: MsgService

  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit(): Promise<void> {
    this.loaderService.hide();
    this.secondaryLang = await this.languageService.secondaryLang();
    const reload = this._reload.pipe(
      mergeMap(() =>
        this.dataSourceService.getDataSourceEx()
      )
    )

    this.setHeadArray();

    const dataSourceEx = this.route.data.pipe(
      map((data: { obss: DataSourceEx[] }) => data.obss),
      mergeWith(reload)
    );

    await this.setEnumerationDropdown();
    await this.setDataSourceTypeDropdown();

    dataSourceEx.subscribe((data) => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);

      data.forEach((e, index) => {

        let ID = e.dataSourceId;

        this.gridArray.push({
          dataSourceId: e.dataSourceId,
          description: e.description,
          dataSourceTypeId: e.dataSourceTypeId,
          dataSourceTypeIdDesc: e.dataSourceType.description,
          valModId: e.valModId,
          valModIdDesc: this.secondaryLang ? e.enumeration.descriptionLang : e.enumeration.description,

          variableGridArray: {
            id: ID,
            updated: false,
            buttonDetails: true,
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
        required: true,
        unique: true,
        width: '10vw'
      },
      {
        head: "Description",
        fieldName: "description",
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        textLength: 255,
        required: true,
        width: '25vw'
      },
      {
        head: "Tipology",
        fieldName: "dataSourceTypeIdDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        required: true,
        textLength: 255,
        width: '11vw',
        dropdown: {
          item: this.dropdownDataSourceType,
          clear: true,
          key: "dataSourceTypeId",
          command: async () => {
            this.dropdownDataSourceType = [];
            this.headArray.filter(x => x.fieldName == 'dataSourceTypeIdDesc').forEach(y => y.dropdown.loading = true);
            await this.setDataSourceTypeDropdown();
            this.headArray
              .filter((x) => x.fieldName == "dataSourceTypeIdDesc")
              .forEach((y) => (y.dropdown.item = this.dropdownDataSourceType));
            this.headArray.filter(x => x.fieldName == 'dataSourceTypeIdDesc').forEach(y => y.dropdown.loading = false);
          },
        },
      },
      {
        head: "Editable values",
        fieldName: "valModIdDesc",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        textLength: 255,
        width: '11vw',
        dropdown: {
          item: this.dropdownEnumeration,
          clear: true,
          key: "valModId",
          command: async () => {
            this.dropdownEnumeration = [];
            this.headArray.filter(x => x.fieldName == 'valModIdDesc').forEach(y => y.dropdown.loading = true);
            await this.setEnumerationDropdown();
            this.headArray
              .filter((x) => x.fieldName == "valModIdDesc")
              .forEach((y) => (y.dropdown.item = this.dropdownEnumeration));
            this.headArray.filter(x => x.fieldName == 'valModIdDesc').forEach(y => y.dropdown.loading = false);
          },
        },
      },
      { head: '', fieldName: 'null', actionInput: ActionInput.null, actionOutput: ActionOutput.actionDetails, width: '5vw', filter: HeadFilter.null }
    );
  }

  async setDataSourceTypeDropdown() {
    this.dropdownDataSourceType = [];
    const obs$ = this.usrPreferenceService.getUserPreference("ORGANIZATION_PARTY").pipe(
      switchMap((data) =>
        this.dataSourceTypeService.getDataSourceType()
      )
    );

    await lastValueFrom(obs$)
      .then((data) => {
        data.forEach((x) => {
          this.dropdownDataSourceType.push({
            label: x.description,
            id: x.dataSourceTypeId,
          });
        });
      })
      .catch((error) => console.log(error));
  }

  async setEnumerationDropdown() {
    this.dropdownEnumeration = [];
    const obs$ = this.usrPreferenceService.getUserPreference("ORGANIZATION_PARTY").pipe(
      switchMap((data) =>
        this.enumerationService.enumerations("VAL_MOD")
      )
    );

    await lastValueFrom(obs$)
      .then((data) => {
        data.forEach((x) => {
          this.dropdownEnumeration.push({
            label: this.secondaryLang ? x.descriptionLang : x.description,
            id: x.enumId,
          });
        });
      })
      .catch((error) => console.log(error));
  }

  openNew() {
    let ID = "new" + Math.random();
    let tmpElAdd: DataSourceEx = {
      dataSourceId: null,
      description: null,
      dataSourceTypeId: null,
      dataSourceTypeIdDesc: null,
      valModId: null,
      valModIdDesc: null,
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
      let obj = new DataSource();

      obj.dataSourceId = e.dataSourceId;
      obj.description = e.description;
      obj.dataSourceTypeId = e.dataSourceTypeId;
      obj.valModId = e.valModId;

      await this.dataSourceService.createDataSource(obj)
        .then((x) => {
          this.msgService.successCreateWithId(obj.dataSourceId);
          let ID = x.dataSourceId;
          e.variableGridArray.id = ID;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;

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
      let obj = new DataSource();

      obj.dataSourceId = e.dataSourceId;
      obj.description = e.description;
      obj.dataSourceTypeId = e.dataSourceTypeId;
      obj.valModId = e.valModId;

      await this.dataSourceService.updateDataSource(obj)
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

  delete(listGridElement: DataSourceEx[]) {
    if (listGridElement.filter(x => x.variableGridArray.id.includes("new")).length > 0) {
      this.newRow = listGridElement.filter(x => x.variableGridArray.id.includes("new"));
      listGridElement = listGridElement.filter(x => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(x => !x.variableGridArray.id.includes("new"));
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      listGridElement.forEach(e => {

        this.dataSourceService.deleteDataSource(e.dataSourceId)
          .then(() => {
            this.msgService.successDeleteWithId(e.dataSourceId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.dataSourceId);
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

  toSubsystemDetails(itemClick) {

    let encodedURI = encodeURIComponent(itemClick.dataSourceId);
    this.router.navigate([`${encodedURI}`], { relativeTo: this.route });
  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }


  resetAllElement(){
    this._reload.next();
  }


}
