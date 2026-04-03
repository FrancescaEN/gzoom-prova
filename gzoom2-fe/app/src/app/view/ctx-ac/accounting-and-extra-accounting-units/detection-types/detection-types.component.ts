import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith } from 'rxjs';
import { Message as MessageError } from 'primeng/api';
import { Message } from 'app/commons/model/message';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { MenuItem } from 'primeng/api';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { GlAccountTypeGlFiscalTypeService } from 'app/api/service/gl-account-type-gl-fiscal-type.service';
import { GlAccountTypeGlFiscalType } from 'app/api/model/glAccoutTypeGlFiscalType';
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import { MsgService } from 'app/commons/service/message.service';
import { GlAccountTypeService } from 'app/api/service/gl-account-type.service';
import { GlAccountType } from 'app/api/model/glAccountType';

@Component({
  selector: 'app-detection-types',
  templateUrl: './detection-types.component.html',
  styleUrls: ['./detection-types.component.css']
})
export class DetectionTypesComponent {
  _reload: Subject<void>;
  reload: boolean = false;

  elementToAdd: any;
  flag: boolean = false;
  loading: boolean = true;
  er: boolean = false;
  backLink = '../../'

  headArray: HeadArray[] = [
    { head: '', fieldName: 'id', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, display: "none" }
  ]

  gridArray: any[] = [];
  newRow: any;
  secondaryLang: boolean;
  languages: [] = [];

  glAccountTypeId;

  title: string;

  dropdownGlFiscalType: MenuItem[] = [];

  menuDetails: boolean = true;
  itemsn: MenuItem[];
  activeIndexTabView: number = 0;

  selectedGlAccountType: GlAccountType;

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly dataStorageService: DataStorageService,
    private readonly router: Router,
    private readonly glAccountTypeService: GlAccountTypeService,
    private readonly glAccountTypeGlFiscalTypeService: GlAccountTypeGlFiscalTypeService,
    private readonly glFiscalTypeService: GlFiscalTypeService,
    private readonly tbService: TableEditingCellService,
    private msgService: MsgService

  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    const routeParams = this.route.parent.snapshot.paramMap;
    this.glAccountTypeId = routeParams.get('glAccountTypeId');    

    this.selectedGlAccountType = await lastValueFrom(this.glAccountTypeService.getGlAccountTypeId(this.glAccountTypeId));
    this.title = this.glAccountTypeId + " - " + ( this.secondaryLang? this.selectedGlAccountType.descriptionLang: this.selectedGlAccountType.description)

    const reload = this._reload.pipe(mergeMap(() => this.glAccountTypeGlFiscalTypeService.getGlAccountTypeGlFiscalTypeList(this.glAccountTypeId)));
    const data$ = this.route.data.pipe(
      map((data: { obss: GlAccountTypeGlFiscalType[] }) => data.obss),
      mergeWith(reload)
    );

    await this.setGlFiscalTypeDropdown();

    this.setHeadArray();


    data$.subscribe(y => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach((e, index) => {
        this.gridArray.push({
          glAccountTypeId: e.glAccountTypeId,
          glFiscalTypeId: e.glFiscalTypeId,
          glFiscalTypeIdDesc: this.dropdownGlFiscalType.find(x => x.id == e.glFiscalTypeId)?.label,
          sequenceId: e.sequenceId,

          variableGridArray: {
            id: e.glFiscalTypeId,
            updated: false,
            buttonDetails: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: true,
          }

        })
      });
      this.loading = false;
    });


    this.itemsn = [
      {
        label: this.i18nService.translate("Detection Type"),
        routerLink: `./`,
        routerLinkActiveOptions: { exact: true },
      },
      {
        label: this.i18nService.translate("Natura"),
        routerLink: `../../nature/${this.glAccountTypeId}`,
        routerLinkActiveOptions: { exact: true },
      }

    ];

  }

  setHeadArray() {

    this.headArray.push(
      {
        head: 'Detection Type', fieldName: 'glFiscalTypeIdDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, unique: true, required: true,
        dropdown: {
          item: this.dropdownGlFiscalType,
          clear: true,
          disableSort: true,
          loading: false,
          key: 'glFiscalTypeId',
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'glFiscalTypeIdDesc').forEach(y => y.dropdown.loading = true);
            await this.setGlFiscalTypeDropdown();
            this.headArray.filter(x => x.fieldName == 'glFiscalTypeIdDesc').forEach(y => y.dropdown.item = this.dropdownGlFiscalType);
            this.headArray.filter(x => x.fieldName == 'glFiscalTypeIdDesc').forEach(y => y.dropdown.loading = false);
          }
        },
      },
      { head: 'Sequence', fieldName: 'sequenceId', actionInput: ActionInput.inputLabelNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.popUpFilter, content: 'center', width: '10vw' },

    );
  }

  async setGlFiscalTypeDropdown() {
    this.dropdownGlFiscalType = [];

    await lastValueFrom(this.glFiscalTypeService.getGlFiscalType()).then(data => {

      data.sort((a, b) => this.tbService.sortDataDW(a, b, ((!this.secondaryLang) ? 'description' : 'descriptionLang')));

      data.forEach(x => {
        let lab = ((!this.secondaryLang) ? x.description : x.descriptionLang);

        this.dropdownGlFiscalType.push({ label: lab, id: x.glFiscalTypeId });
      })
    }
    ).catch((error) => console.log(error));
  }

  openNew() {
    this.elementToAdd = {
      glAccountTypeId: this.glAccountTypeId,
      glFiscalTypeId: null,
      glFiscalTypeIdDesc: null,
      sequenceId: null,

      variableGridArray: {
        id: "new" + Math.random(),
        updated: true,
        buttonDetails: false,
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
    console.log(this.gridArray)
    gridElement.forEach(async e => {
      let obj = new GlAccountTypeGlFiscalType(
        e.glAccountTypeId,
        e.glFiscalTypeId,
        e.sequenceId,
      );

      await this.glAccountTypeGlFiscalTypeService.createGlAccountTypeGlFiscalType(obj)
        .then(() => {
          this.msgService.successCreateWithId(this.dropdownGlFiscalType.find(x => x.id == obj.glFiscalTypeId)?.label);
          e.variableGridArray.id = obj.glFiscalTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, this.dropdownGlFiscalType.find(x => x.id == obj.glFiscalTypeId)?.label);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new GlAccountTypeGlFiscalType(
        e.glAccountTypeId,
        e.glFiscalTypeId,
        e.sequenceId,
      );
      await this.glAccountTypeGlFiscalTypeService.updateGlAccountTypeGlFiscalType(obj)
        .then(() => {
          this.msgService.successUpdateWithId(this.dropdownGlFiscalType.find(x => x.id == obj.glFiscalTypeId)?.label);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, this.dropdownGlFiscalType.find(x => x.id == obj.glFiscalTypeId)?.label);
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
        this.glAccountTypeGlFiscalTypeService.deleteGlAccountTypeGlFiscalType(e.glAccountTypeId, e.glFiscalTypeId)
          .then(() => {
            this.msgService.successDeleteWithId(this.dropdownGlFiscalType.find(x => x.id == e.glFiscalTypeId)?.label);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, this.dropdownGlFiscalType.find(x => x.id == e.glFiscalTypeId)?.label)
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

  resetAllElement(){
    this._reload.next();
  }

  tabChangeEvent(event){
    this.router.navigate([this.itemsn[event.index].routerLink], { queryParams: this.itemsn[event.index].queryParams, relativeTo: this.route });
  }

}
