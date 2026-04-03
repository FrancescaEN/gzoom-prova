import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Observable, Subject, lastValueFrom, map, mergeMap, mergeWith, switchMap, tap } from 'rxjs';
import { Message as MessageError } from 'primeng/api';
import { Message } from 'app/commons/model/message';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { MenuItem } from 'primeng/api';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { GlAccountResourceService } from 'app/api/service/gl-account-resource.service';
import { GlAccountResource } from 'app/api/model/glAccountResource';
import { GlResourceTypeService } from 'app/api/service/gl-resource-type.service';
import { MsgService } from 'app/commons/service/message.service';
import { GlAccountTypeService } from 'app/api/service/gl-account-type.service';
import { GlAccountType } from 'app/api/model/glAccountType';

@Component({
  selector: 'app-nature',
  templateUrl: './nature.component.html',
  styleUrls: ['./nature.component.css']
})
export class NatureComponent {
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

  selectedGlAccountType: GlAccountType;

  dropdownGlResourceType: MenuItem[] = [];
  // menuDetails: boolean = true;
  // itemsn: MenuItem[];
  // activeIndexTabView: number = 1;

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly dataStorageService: DataStorageService,
    private readonly router: Router,
    private readonly glAccountTypeService: GlAccountTypeService,
    private readonly glAccountResourceService: GlAccountResourceService,
    private readonly glResourceTypeService: GlResourceTypeService,
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
    

    const reload = this._reload.pipe(mergeMap(() => this.glAccountResourceService.getGlAccountResourceList(this.glAccountTypeId)));
    const data$ = this.route.data.pipe(
      map((data: { obss: GlAccountResource[] }) => data.obss),
      mergeWith(reload)
    );

    await this.setGlResourceTypeDropdown();

    this.setHeadArray();


    data$.subscribe(y => {      
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach((e, index) => {
        this.gridArray.push({
          glAccountTypeId: e.glAccountTypeId,
          glResourceTypeId: e.glResourceTypeId,
          glResourceTypeIdDesc: this.dropdownGlResourceType.find(x => x.id == e.glResourceTypeId)?.label,
          sequenceId: e.sequenceId,

          variableGridArray: {
            id: e.glResourceTypeId,
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

  }

  setHeadArray() {

    this.headArray.push(
      {
        head: 'Natura', fieldName: 'glResourceTypeIdDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter, unique: true, required: true,
        dropdown: {
          item: this.dropdownGlResourceType,
          clear: true,
          disableSort: true,
          loading: false,
          key: 'glResourceTypeId',
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'glResourceTypeIdDesc').forEach(y => y.dropdown.loading = true);
            await this.setGlResourceTypeDropdown();
            this.headArray.filter(x => x.fieldName == 'glResourceTypeIdDesc').forEach(y => y.dropdown.item = this.dropdownGlResourceType);
            this.headArray.filter(x => x.fieldName == 'glResourceTypeIdDesc').forEach(y => y.dropdown.loading = false);
          }
        },
      },
      { head: 'Sequence', fieldName: 'sequenceId', actionInput: ActionInput.inputLabelNumber, actionOutput: ActionOutput.outputLabelNumber, filter: HeadFilter.popUpFilter, content: 'center', width: '10vw' },

    );
  }

  async setGlResourceTypeDropdown() {
    this.dropdownGlResourceType = [];

    await lastValueFrom(this.glResourceTypeService.getGlResourceType()).then(data => {

      data.sort((a, b) => this.tbService.sortDataDW(a, b, ((!this.secondaryLang) ? 'description' : 'descriptionLang')));

      data.forEach(x => {
        let lab = ((!this.secondaryLang) ? x.description : x.descriptionLang);

        this.dropdownGlResourceType.push({ label: lab, id: x.glResourceTypeId });
      })
    }
    ).catch((error) => console.log(error));
  }

  openNew() {
    this.elementToAdd = {
      glAccountTypeId: this.glAccountTypeId,
      glResourceTypeId: null,
      glResourceTypeIdDesc: null,
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
    gridElement.forEach(async e => {
      let obj = new GlAccountResource(
        e.glAccountTypeId,
        e.glResourceTypeId,
        e.sequenceId,
      );

      await this.glAccountResourceService.createGlAccountResource(obj)
        .then(() => {
          this.msgService.successCreateWithId(this.dropdownGlResourceType.find(x => x.id == obj.glResourceTypeId)?.label);
          e.variableGridArray.id = obj.glResourceTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, this.dropdownGlResourceType.find(x => x.id == obj.glResourceTypeId)?.label);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new GlAccountResource(
        e.glAccountTypeId,
        e.glResourceTypeId,
        e.sequenceId,
      );
      await this.glAccountResourceService.updateGlAccountResource(obj)
        .then(() => {
          this.msgService.successUpdateWithId(this.dropdownGlResourceType.find(x => x.id == obj.glResourceTypeId)?.label);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, this.dropdownGlResourceType.find(x => x.id == obj.glResourceTypeId)?.label);
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
        this.glAccountResourceService.deleteGlAccountResource(e.glAccountTypeId, e.glResourceTypeId)
          .then(() => {
            this.msgService.successDeleteWithId(this.dropdownGlResourceType.find(x => x.id == e.glResourceTypeId)?.label);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, this.dropdownGlResourceType.find(x => x.id == e.glResourceTypeId)?.label);
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

  // tabChangeEvent(event){
  //   this.router.navigate([this.itemsn[event.index].routerLink], { queryParams: this.itemsn[event.index].queryParams, relativeTo: this.route });
  // }
}
