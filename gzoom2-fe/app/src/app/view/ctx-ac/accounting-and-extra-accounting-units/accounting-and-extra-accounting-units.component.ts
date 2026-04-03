import { Component, OnInit, computed, effect, input } from '@angular/core';
import { Subject, lastValueFrom, map, mergeMap, mergeWith } from 'rxjs';
import { MenuItem } from 'primeng/api';
import { ActionInput, ActionOutput, HeadArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { HeadFilter } from 'app/layout/tables/table/table-configuration';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { GlAccountTypeService } from 'app/api/service/gl-account-type.service';
import { GlAccountType } from 'app/api/model/glAccountType';
import { MsgService } from 'app/commons/service/message.service';
import { Table } from 'primeng/table';
import { PrimengTableService } from 'app/commons/service/primeng-table.service';

@Component({
  selector: 'app-accounting-and-extra-accounting-units',
  templateUrl: './accounting-and-extra-accounting-units.component.html',
  styleUrls: ['./accounting-and-extra-accounting-units.component.css']
})
export class AccountingAndExtraAccountingUnitsComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;

  elementToAdd: any;
  flag: boolean = false;
  loading: boolean = true;
  er: boolean = false;

  headArray: HeadArray[] = [
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: any[] = [];
  newRow: any;
  table: Table;
  secondaryLang: boolean;

  dropdownY_N: MenuItem[] = [
    { label: this.i18nService.translate('Y'), id: 'Y' },
    { label: this.i18nService.translate('N'), id: 'N' }];

  accountTypeEnumId = input.required<string>();

  stateTable: string;

  stateKey = computed(() => {
    const updatedStatekey = 'acc' + this.accountTypeEnumId()
    this.stateTable = window.sessionStorage.getItem(updatedStatekey);
    return updatedStatekey;
  });


  itemsButtonSlideMenu = [
    {
      label: this.i18nService.translate("Detection Type"),
      icon: "pi pi-angle-right",
      command: () => this.toDetail("detection-types"),
    },
    {
      label: this.i18nService.translate("Natura"),
      icon: "pi pi-angle-right",
      command: () => this.toDetail("nature"),
    },
  ];
  selectedIndex;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly glAccountTypeService: GlAccountTypeService,
    private readonly i18nService: I18NService,
    private readonly router: Router,
    private readonly dataStorageService: DataStorageService,
    private readonly languageService: LanguageService,
    private msgService: MsgService,
    private primengTableService: PrimengTableService
  ) {
    this._reload = new Subject<void>();

    effect(() => {
      if (this.stateKey() && this.table) {
        this.primengTableService.setTableState(this.table, this.stateTable);
      }
    })
  }

  async ngOnInit() {

    this.secondaryLang = await this.languageService.secondaryLang();
    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.setHeadArray();

      this.headArray[2].pathIconFlag = lang[0];
      this.headArray[3].pathIconFlag = lang[1];


    } else { this.setHeadArray(); }



    this.loading = true;

    const reload = this._reload.pipe(mergeMap(() => this.glAccountTypeService.getGlAccountTypeList(this.accountTypeEnumId())));
    const w$ = this.route.data.pipe(
      map((data: { obss: GlAccountType[] }) => data.obss),
      mergeWith(reload)
    );

    w$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      let tmpGrid = [];
      y.forEach((e, index) => {
        tmpGrid.push({
          accountTypeEnumId: e.accountTypeEnumId,
          glAccountTypeId: e.glAccountTypeId,
          description: e.description,
          descriptionLang: e.descriptionLang,
          isReservedAccount: e.isReservedAccount,
          isReservedAccountDesc: e.isReservedAccount ? this.i18nService.translate(e.isReservedAccount) : null,
          variableGridArray: {
            id: e.glAccountTypeId,
            updated: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: true,
            buttonMultipleDetails: true
          }

        })

      });
      this.gridArray = [...tmpGrid]
      this.loading = false;
    })




  }

  setHeadArray() {
    let languageType = this.i18nService.getLanguageType();
    this.headArray.push(

      { head: 'Code', fieldName: 'glAccountTypeId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, width: '12vw', textLength: 20 },
      { head: 'Description', fieldName: 'description', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, flag: this.flag, filter: HeadFilter.textFilter, required: true, width: (this.flag) ? '30vw' : '60vw', textLength: 255, },
    );
    if (languageType == "BILING") this.headArray.push({ head: 'Description', fieldName: 'descriptionLang', flag: true, actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, textLength: 255, width: '30vw' });
    this.headArray.push(
      { head: 'Reserved', fieldName: "isReservedAccountDesc", actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, required: true, dropdown: { item: this.dropdownY_N, clear: false, key: 'isReservedAccount' }, sortIcon: false, filter: HeadFilter.dropdownFilter },
      {
        head: "",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.actionDetails,
        filter: HeadFilter.null,
      },
    );

  }

  descriptionTable(dataTable: Table) {
    this.table = dataTable;
  }

  shareItemEvent(data) {
    this.selectedIndex = data;
  }

  toDetail(component: string) {
    this.router.navigate([`${this.selectedIndex.glAccountTypeId}/${component}`], { relativeTo: this.route });
  }


  openNew() {
    this.elementToAdd = {
      glAccountTypeId: null,
      description: null,
      descriptionLang: null,
      isReservedAccount: "N",
      isReservedAccountDesc: "N",
      accountTypeEnumId: this.accountTypeEnumId(),
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
    console.log(gridElement);
    gridElement.forEach(async e => {
      let obj = new GlAccountType(
        e.glAccountTypeId,
        e.description,
        e.descriptionLang,
        e.isReservedAccount,
        e.accountTypeEnumId
      );

      await this.glAccountTypeService.createGlAccountType(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.glAccountTypeId);
          e.variableGridArray.id = obj.glAccountTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonMultipleDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.glAccountTypeId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new GlAccountType(
        e.glAccountTypeId,
        e.description,
        e.descriptionLang,
        e.isReservedAccount,
        e.accountTypeEnumId
      );

      await this.glAccountTypeService.updateGlAccountType(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.glAccountTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.glAccountTypeId);
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

        this.glAccountTypeService.deleteGlAccountType(e.glAccountTypeId)
          .then(() => {
            this.msgService.successDeleteWithId(e.glAccountTypeId);
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

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  resetAllElement() {
    this._reload.next();
  }

}
