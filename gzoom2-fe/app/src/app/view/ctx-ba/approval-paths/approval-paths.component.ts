import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subject, Subscription, combineLatest, lastValueFrom, map, mergeMap, mergeWith, tap } from 'rxjs';
import { MenuItem, Message as MessageError } from 'primeng/api';
import { Message } from 'app/commons/model/message';
import { ActionInput, ActionOutput, HeadArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { HeadFilter } from 'app/layout/tables/table/table-configuration';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { GlAccountTypeService } from 'app/api/service/gl-account-type.service';
import { GlAccountType } from 'app/api/model/glAccountType';
import { StatusTypeService } from 'app/api/service/status-type.service';
import { StatusType } from 'app/api/model/statusType';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-approval-paths',
  templateUrl: './approval-paths.component.html',
  styleUrls: ['./approval-paths.component.css']
})
export class ApprovalPathsComponent implements OnInit, OnDestroy {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  loading: boolean = true;
  er: boolean = false;
  secondaryLang: boolean;
  headArray: HeadArray[] = [];

  gridArray: any[] = [];
  newRow: any;
  obs$: Subscription;

  itemsButtonSlideMenu = [
    {
      label: this.i18nService.translate("Predicted States"),
      icon: "pi pi-angle-right",
      command: () => this.toDetail("predicted-states"),
    },
    {
      label: this.i18nService.translate("Passes Allowed"),
      icon: "pi pi-angle-right",
      command: () => this.toDetail("passes-allowed"),
    },
  ];
  selectedIndex;

  dropdownStatusType: MenuItem[] = [];
  dropdownPortal: MenuItem[] = [];


  constructor(
    private readonly route: ActivatedRoute,
    private readonly statusTypeService: StatusTypeService,
    private readonly i18nService: I18NService,
    private readonly router: Router,
    private readonly dataStorageService: DataStorageService,
    private readonly enumerationService: EnumerationService,
    private readonly languageService: LanguageService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  ngOnDestroy(): void {
    this.obs$.unsubscribe();
  }

  async ngOnInit() {
    this.languageService.secondaryLang().then(result => this.secondaryLang = result);
    await this.setPortalDropdown();
    this.setHeadArray();

    const reload = this._reload.pipe(mergeMap(() => this.statusTypeService.getStatusTypeList()));
    const w$ = this.route.data.pipe(
      map((data: { obss: StatusType[] }) => data.obss),
      mergeWith(reload)
    );

    this.obs$ = w$.pipe(
      tap(() => {
        this.gridArray = [];
        if (this.newRow) this.gridArray.push(this.newRow);
      }),
      map((array: StatusType[]) => {
        array.map((x) => this.dropdownStatusType.push({ label: x.description, id: x.statusTypeId }));

        array.map((x) => {

          this.gridArray.push({
            statusTypeId: x.statusTypeId,
            parentTypeId: x.parentTypeId,
            parentTypeDesc: array.find((a) => a.statusTypeId == x.parentTypeId)?.description,
            description: x.description,
            portalTypeId: x.portalTypeId,
            portalTypeDesc: this.dropdownPortal.find((y) => y.id == x.portalTypeId)?.label,

            variableGridArray: {
              id: x.statusTypeId,
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
        })
      })
    ).subscribe(() => {
      this.loading = false;
    })




  }

  setHeadArray() {
    this.headArray.push(
      { head: 'Code', fieldName: 'statusTypeId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, width: '12vw', textLength: 20 },
      {
        head: 'Classification', fieldName: 'parentTypeDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,
        dropdown: {
          item: this.dropdownStatusType,
          clear: true,
          key: 'parentTypeId',
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.loading = true);

            this.dropdownStatusType = [];
            await lastValueFrom(this.statusTypeService.getStatusTypeList()).then(x => {
              x.map((x) => this.dropdownStatusType.push({ label: x.description, id: x.statusTypeId }));

            }).catch((error) => console.log(error));
            this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.item = this.dropdownStatusType);
            this.headArray.filter(x => x.fieldName == 'parentTypeDesc').forEach(y => y.dropdown.loading = false);

          }
        }, width: '15vw'
      },
      { head: 'Description', fieldName: 'description', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, width: '30vw', textLength: 255, },
      {
        head: 'Viewing Portal', fieldName: 'portalTypeDesc', actionInput: ActionInput.dropdownData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.dropdownFilter,
        dropdown: {
          item: this.dropdownPortal,
          clear: true,
          key: 'portalTypeId',
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'portalTypeDesc').forEach(y => y.dropdown.loading = true);
            await this.setPortalDropdown();
            this.headArray.filter(x => x.fieldName == 'portalTypeDesc').forEach(y => y.dropdown.item = this.dropdownPortal);
            this.headArray.filter(x => x.fieldName == 'portalTypeDesc').forEach(y => y.dropdown.loading = false);

          }
        }, width: '15vw'
      },
      {
        head: "",
        width: '5vw',
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.actionDetails,
        filter: HeadFilter.null,
      }
    );

  }

  async setPortalDropdown() {
    this.dropdownPortal = [];

    await lastValueFrom(this.enumerationService.enumerations("STATUS_TO_PORTAL")).then(data => {

      data.forEach(x => {
        let lab = ((!this.secondaryLang) ? x.description : x.descriptionLang);

        this.dropdownPortal.push({ label: lab, id: x.enumId });
      })
    }
    ).catch((error) => console.log(error));
  }

  shareItemEvent(data) {
    this.selectedIndex = data;
  }

  toDetail(component: string) {
    this.router.navigate([`${this.selectedIndex.statusTypeId}/${component}`], { relativeTo: this.route });
  }

  openNew() {
    this.elementToAdd = {
      statusTypeId: null,
      parentTypeId: null,
      parentTypeDesc: null,
      description: null,
      portalTypeId: null,
      portalTypeDesc: null,

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
      let obj = new StatusType(
        e.statusTypeId,
        e.parentTypeId,
        e.description,
        e.portalTypeId
      );

      await this.statusTypeService.createStatusType(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.statusTypeId);
          e.variableGridArray.id = obj.statusTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonMultipleDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.statusTypeId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new StatusType(
        e.statusTypeId,
        e.parentTypeId,
        e.description,
        e.portalTypeId
      );

      await this.statusTypeService.updateStatusType(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.statusTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.statusTypeId);
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



      this.statusTypeService.deleteStatusType(listGridElement.map(x => x.statusTypeId))
        .then(() => {
          this.msgService.successDelete();
          let tmpGrid = this.gridArray;
          listGridElement.forEach(e => tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id))
          )
          this.gridArray = tmpGrid;
        })
        .catch((error) => {
          this.msgService.error(error.message);
          this._reload.next();
        });

    }
    else {
      this.msgService.successDelete();
    }

  }

  resetAllElement() {
    this._reload.next();
  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }
}