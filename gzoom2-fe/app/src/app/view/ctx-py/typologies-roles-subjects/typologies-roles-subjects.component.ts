import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Enumeration } from "app/api/model/enumeration";
import { PartyType } from "app/api/model/party-type";
import { RoleType } from "app/api/model/role-type";
import { WorkEffortAssocType } from "app/api/model/workEffortAssocType";
import { EnumerationService } from "app/api/service/enumeration.service";
import { LanguageService } from "app/api/service/language.service";
import { PartyTypeService } from "app/api/service/party-type.service";
import { RoleTypeService } from "app/api/service/role-type.service";
import { WorkEffortAssocTypeService } from "app/api/service/work-effort-assoc-type.service";
import { WorkEffortTypeService } from "app/api/service/work-effort-type.service";
import { I18NService } from "app/i18n/i18n.service";
import {
  ActionInput,
  ActionOutput,
  HeadArray,
  HeadFilter,
} from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import {MenuItem} from "primeng/api";
import {
  Observable,
  Subject,
  lastValueFrom,
  map,
  mergeMap,
  mergeWith,
} from "rxjs";
import { MsgService } from "app/commons/service/message.service";

@Component({
  selector: "app-typologies-roles-subjects",
  templateUrl: "./typologies-roles-subjects.component.html",
  styleUrls: ["./typologies-roles-subjects.component.css"],
})
export class TypologiesRolesSubjectsComponent implements OnInit {
  reload: boolean = false;
  _reload: Subject<void>;
  dataTable: any;
  gridArray: RoleType[] = [];
  selectedOn: boolean = true;
  buttonNew: boolean = true;
  buttonDelete: boolean = false;
  buttonSave: boolean = false;
  elementToAdd: RoleType;
  editingKeyId: string;
  selectionRoleTypes: RoleType[];
  langType: string;
  languages: string[] = [];
  flag: boolean = false;
  loading: boolean = true;

  parentTypeIdArray: MenuItem[] = [];
  prevPartyTypeIdArray: MenuItem[] = [];
  workEffortTypeIdArray: MenuItem[] = [];
  workEffortAssocTypeIdArray: MenuItem[] = [];
  workEffortPeriodIdArray: MenuItem[] = [];

  er: boolean = false;
  newRow: any;

  headArray: HeadArray[] = [
    {
      head: this.i18nService.translate("Role Type"),
      fieldName: "roleTypeId",
      textLength: 20,
      actionInput: ActionInput.outputData,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      width: "8vw",
      sortIcon: true,
      required: true,
    },
    {
      head: this.i18nService.translate("Description"),
      fieldName: "description",
      textLength: 255,
      actionInput: ActionInput.inputLabeldata,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      width: "5vw",
      flag: true,
      sortIcon: true,
      required: true,
    },
    {
      head: this.i18nService.translate("Description"),
      fieldName: "descriptionLang",
      textLength: 255,
      actionInput: ActionInput.inputLabeldata,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      width: "5vw",
      flag: true,
      sortIcon: true,
    },
    {
      head: this.i18nService.translate("Etichetta Breve"),
      fieldName: "shortLabel",
      textLength: 20,
      actionInput: ActionInput.inputLabeldata,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      width: "5vw",
      flag: true,
      sortIcon: true,
      required: true,
    },
    {
      head: this.i18nService.translate("Etichetta Breve"),
      fieldName: "shortLabelLang",
      textLength: 20,
      actionInput: ActionInput.inputLabeldata,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      width: "5vw",
      flag: true,
      sortIcon: true,
    },
    {
      head: this.i18nService.translate("Classification"),
      fieldName: "parentTypeIdDescription",
      actionInput: ActionInput.dropdownData,
      actionOutput: ActionOutput.outputLabelData,
      dropdown: {
        item: this.parentTypeIdArray,
        clear: true,
        key: "parentTypeId",
        command: async () => {
          this.parentTypeIdArray = [];
          await lastValueFrom(this.roleTypeService.roleTypes())
            .then((x) => {              
              x.forEach((y) => {             
                this.parentTypeIdArray.push({
                  label: y.description,
                  id: y.roleTypeId,
                });
              });              
            })
            .catch((error) => console.log(error));
          this.headArray
            .filter((x) => x.fieldName == "parentTypeIdDescription")
            .forEach((y) => (y.dropdown.item = this.parentTypeIdArray));
        },
      },
      width: "8vw",
      sortIcon: true,
      filter: HeadFilter.dropdownFilter
    },
    {
      head: this.i18nService.translate("Tipo soggetto prevalente"),
      fieldName: "prevPartyTypeIdDescription",
      actionInput: ActionInput.dropdownData,
      actionOutput: ActionOutput.outputLabelData,
      dropdown: {
        item: this.prevPartyTypeIdArray,
        clear: true,
        key: "prevPartyTypeId",
        command: async () => {
          this.prevPartyTypeIdArray = [];
          await lastValueFrom(this.partyTypeService.partyTypes())
            .then((x) => {
              x.forEach((y) => {
                this.prevPartyTypeIdArray.push({
                  label: y.description,
                  id: y.partyTypeId,
                });
              });
            })
            .catch((error) => console.log(error));
          this.headArray
            .filter((x) => x.fieldName == "prevPartyTypeIdDescription")
            .forEach((y) => (y.dropdown.item = this.prevPartyTypeIdArray));
        },
      },
      width: "8vw",
      sortIcon: true,
      filter: HeadFilter.dropdownFilter
    },
    {
      head: this.i18nService.translate("Scheda Interfaccia"),
      fieldName: "workEffortTypeIdDescription",
      actionInput: ActionInput.dropdownData,
      actionOutput: ActionOutput.outputLabelData,
      dropdown: {
        item: this.workEffortTypeIdArray,
        clear: true,
        key: "workEffortTypeId",
        command: async () => {
          this.workEffortTypeIdArray = [];
          await lastValueFrom(
            this.workEffortTypeService.workEffortTypesIsRoot("Y")
          )
            .then((x) => {              
              x.forEach((y) => {
                this.workEffortTypeIdArray.push({
                  label: y.description,
                  id: y.workEffortTypeId,
                });
              });
            })
            .catch((error) => console.log(error));
          this.headArray
            .filter((x) => x.fieldName == "workEffortTypeIdDescription")
            .forEach((y) => (y.dropdown.item = this.workEffortTypeIdArray));
        },
      },
      width: "8vw",
      sortIcon: true,
      filter: HeadFilter.dropdownFilter
    },
    {
      head: this.i18nService.translate("Association Interface"),
      fieldName: "workEffortAssocTypeIdDescription",
      actionInput: ActionInput.dropdownData,
      actionOutput: ActionOutput.outputLabelData,
      dropdown: {
        item: this.workEffortAssocTypeIdArray,
        clear: true,
        key: "workEffortAssocTypeId",
        command: async () => {
          this.workEffortAssocTypeIdArray = [];
          await lastValueFrom(
            this.workEffortAssocTypeService.getWorkEffortAssocType()
          )
            .then((x) => {
              x.forEach((y) => {
                this.workEffortAssocTypeIdArray.push({
                  label: y.description,
                  id: y.workEffortAssocTypeId,
                });
              });
            })
            .catch((error) => console.log(error));
          this.headArray
            .filter((x) => x.fieldName == "workEffortAssocTypeId")
            .forEach(
              (y) => (y.dropdown.item = this.workEffortAssocTypeIdArray)
            );
        },
      },
      width: "10vw",
      sortIcon: true,
      filter: HeadFilter.dropdownFilter
    },
    {
      head: this.i18nService.translate("Root Period"),
      fieldName: "workEffortPeriodIdDescription",
      actionInput: ActionInput.dropdownData,
      actionOutput: ActionOutput.outputLabelData,
      dropdown: {
        item: this.workEffortPeriodIdArray,
        clear: true,
        key: "workEffortPeriodId",
        command: async () => {
          this.workEffortPeriodIdArray = [];
          await lastValueFrom(this.enumeration.enumerations("WE_PERIOD_TYPE"))
            .then((x) => {
              x.forEach((y) => {
                this.workEffortPeriodIdArray.push({
                  label: y.description,
                  id: y.enumId,
                });
              });
            })
            .catch((error) => console.log(error));
          this.headArray
            .filter((x) => x.fieldName == "workEffortPeriodIdDescription")
            .forEach((y) => (y.dropdown.item = this.workEffortPeriodIdArray));
        },
      },
      width: "10vw",
      sortIcon: true,
      filter: HeadFilter.dropdownFilter
    },{
      head: "",
      fieldName: "null",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.null,
      width:'0.5vw',
    }
  ];

  constructor(
    private readonly roleTypeService: RoleTypeService,
    private readonly route: ActivatedRoute,
    private readonly workEffortAssocTypeService: WorkEffortAssocTypeService,
    private readonly workEffortTypeService: WorkEffortTypeService,
    private readonly enumeration: EnumerationService,
    private readonly partyTypeService: PartyTypeService,
    private languageService: LanguageService,
    private readonly i18nService: I18NService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  ngOnInit(): void {
    this.loading = true;

    this.collapseSidebar();

    // Language control
    this.langType = this.i18nService.getLanguageType();
    this.languageService.language().subscribe((data) => {
      this.languages = data;
      if (this.languages.length > 1 && this.langType != "NONE") {
        this.flag = true;
        this.headArray[1].pathIconFlag = this.languages[0];
        this.headArray[2].pathIconFlag = this.languages[1];
        this.headArray[3].pathIconFlag = this.languages[0];
        this.headArray[4].pathIconFlag = this.languages[1];
      }
    });

    if (this.langType == "NONE") {
      this.headArray[2].display = "none";
      this.headArray[4].display = "none";
    }

    const reloadedRoleTypeService = this._reload.pipe(
      mergeMap(() => this.roleTypeService.roleTypes())
    );
    const reloadedPartyTypeService = this._reload.pipe(
      mergeMap(() => this.partyTypeService.partyTypes())
    );
    const reloadedWorkEffortAssocTypeService = this._reload.pipe(
      mergeMap(() => this.workEffortAssocTypeService.getWorkEffortAssocType())
    );
    const reloadedEnumerationsService = this._reload.pipe(
      mergeMap(() => this.enumeration.enumerations("WE_PERIOD_TYPE"))
    );
    const reloadedWorkEffortTypeService = this._reload.pipe(
      mergeMap(() => this.workEffortTypeService.workEffortTypesIsRoot("Y"))
    );

    const partyTypesObs = this.route.data.pipe(
      map((data: { partyTypes: PartyType[] }) => data.partyTypes),
      mergeWith(reloadedPartyTypeService)
    );

    partyTypesObs.subscribe((data) => {
      data.forEach((element) => {
        if (
          this.prevPartyTypeIdArray.filter((x) => x.id === element.partyTypeId)
            .length == 0
        ) {
          this.prevPartyTypeIdArray.push({
            label: element.description,
            id: element.partyTypeId,
          });
        }
      });
    });

    const workEffertoAssocTypesObs = this.route.data.pipe(
      map(
        (data: { workEffortAssocTypes: WorkEffortAssocType[] }) =>
          data.workEffortAssocTypes
      ),
      mergeWith(reloadedWorkEffortAssocTypeService)
    );

    workEffertoAssocTypesObs.subscribe((data) => {
      data.forEach((element) => {
        if (
          this.workEffortAssocTypeIdArray.filter(
            (x) => x.id === element.workEffortAssocTypeId
          ).length == 0
        ) {
          this.workEffortAssocTypeIdArray.push({
            label: element.description,
            id: element.workEffortAssocTypeId,
          });
        }
      });
    });

    const enumerationsObs = this.route.data.pipe(
      map((data: { enumerations: Enumeration[] }) => data.enumerations),
      mergeWith(reloadedEnumerationsService)
    );

    enumerationsObs.subscribe((data) => {
      data.forEach((element) => {
        if (
          this.workEffortPeriodIdArray.filter((x) => x.id === element.enumId)
            .length == 0
        ) {
          this.workEffortPeriodIdArray.push({
            label: element.description,
            id: element.enumId,
          });
        }
      });
    });

    const reloadedWorkEffortTypeObs = this.route.data.pipe(
      map((data: { workEffortTypes: WorkerType[] }) => data.workEffortTypes),
      mergeWith(reloadedWorkEffortTypeService)
    );

    reloadedWorkEffortTypeObs.subscribe((data) => {
      data.forEach((element) => {
        if (
          this.workEffortTypeIdArray.filter(
            (x) => x.id === element.workEffortTypeId
          ).length == 0
        ) {
          this.workEffortTypeIdArray.push({
            label: element.description,
            id: element.workEffortTypeId,
          });
        }
      });
    });

    const roleTypesObs = this.route.data.pipe(
      map((data: { roleTypes: RoleType[] }) => data.roleTypes),
      mergeWith(reloadedRoleTypeService)
    );

    roleTypesObs.subscribe((data) => {
      this.gridArray = [];

      data.forEach((y) => {
        this.parentTypeIdArray.push({ label: y.description, id: y.roleTypeId });
      });

      data.forEach((element) => {        
        this.gridArray.push({
          description: element.description,
          descriptionLang: element.descriptionLang,
          roleTypeId: element.roleTypeId,
          shortLabel: element.shortLabel,
          shortLabelLang: element.shortLabelLang,
          parentTypeId: element.parentTypeId,
          prevPartyTypeId: element.prevPartyTypeId,
          workEffortTypeId: element.workEffortTypeId,
          workEffortAssocTypeId: element.workEffortAssocTypeId,
          workEffortPeriodId: element.workEffortPeriodId,
          parentTypeIdDescription: this.parentTypeIdArray.filter(
            (x) => x.id === element.parentTypeId
          )[0]?.label,
          prevPartyTypeIdDescription: this.prevPartyTypeIdArray.filter(
            (x) => x.id === element.prevPartyTypeId
          )[0]?.label,
          workEffortTypeIdDescription: this.workEffortTypeIdArray.filter(
            (x) => x.id === element.workEffortTypeId
          )[0]?.label,
          workEffortAssocTypeIdDescription:
            this.workEffortAssocTypeIdArray.filter(
              (x) => x.id === element.workEffortAssocTypeId
            )[0]?.label,
          workEffortPeriodIdDescription: this.workEffortPeriodIdArray.filter(
            (x) => x.id === element.workEffortPeriodId
          )[0]?.label,

          variableGridArray: {
            id: element.roleTypeId,
            inputNew: false,
            inputLabeldata: true,
            outputData: true,
            updated: false,
            dropdownData: true,
          },
        });
      });
      this.loading = false;
    });
  }

  shareDescriptorTable(data) {
    this.dataTable = data;
  }

  notifyInputChanges(id) {
    this.gridArray.forEach((x) => {
      if (x.variableGridArray.id == id) x.variableGridArray.updated = true;
    });
    this.buttonSave = true;
  }

  openNew() {
    this.elementToAdd = {
      roleTypeId: null,
      description: null,
      descriptionLang: null,
      shortLabel: null,
      shortLabelLang: null,
      parentTypeId: null,
      prevPartyTypeId: null,
      workEffortTypeId: null,
      workEffortAssocTypeId: null,
      workEffortPeriodId: null,
      parentTypeIdDescription: null,
      prevPartyTypeIdDescription: null,
      workEffortTypeIdDescription: null,
      workEffortAssocTypeIdDescription: null,
      workEffortPeriodIdDescription: null,
      variableGridArray: {
        inputNew: true,
        id: "new" + Math.random(),
        inputLabeldata: true,
        outputData: false,
        dropdownData: true,
      },
    };

    this.gridArray = [this.elementToAdd, ...this.gridArray];
  }

  saveNewAndOpen(gridElement) {
    if (this.create(gridElement)) {
      this.openNew();
    } else this.er = false;
  }

  saveAllElement(elementUpdated) {
    let newElement = elementUpdated.filter((x) =>
      x.variableGridArray.id.includes("new")
    );
    if (newElement.length > 0) {
      this.reload = true;
      this.create(newElement);
      this.reload = false;
    }
    elementUpdated = elementUpdated.filter(
      (x) => !x.variableGridArray.id.includes("new")
    );
    if (elementUpdated.length > 0) {
      this.update(elementUpdated);
    }
  }

  create(gridElement): boolean {
    gridElement.forEach(async (e) => {
      let obj = new RoleType(
        e.roleTypeId,
        e.description,
        e.descriptionLang,
        e.shortLabel,
        e.shortLabelLang,
        e.parentTypeId,
        e.prevPartyTypeId,
        e.workEffortTypeId,
        e.workEffortAssocTypeId,
        e.workEffortPeriodId
      );

      await this.roleTypeService
        .createRoleTypes(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.roleTypeId);
          e.variableGridArray.id = obj.roleTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next();
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.roleTypeId);
          this.er = true;
        });
    });

    return !this.er;
  }

  update(gridElement) {
    gridElement.forEach(async (e) => {
      let obj = new RoleType(
        e.roleTypeId,
        e.description,
        e.descriptionLang,
        e.shortLabel,
        e.shortLabelLang,
        e.parentTypeId,
        e.prevPartyTypeId,
        e.workEffortTypeId,
        e.workEffortAssocTypeId,
        e.workEffortPeriodId
      );

      await this.roleTypeService
        .updateRoleTypes(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.roleTypeId);
          this._reload.next();
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.roleTypeId);
          this.er = true;
        });
    });

    return !this.er;
  }

  delete(listGridElement) {
    if (
      listGridElement.filter((x) => x.variableGridArray.id.includes("new"))
        .length > 0
    ) {
      this.newRow = listGridElement.filter((x) =>
        x.variableGridArray.id.includes("new")
      );
      listGridElement = listGridElement.filter((x) => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(
        (x) => !x.variableGridArray.id.includes("new")
      );
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      this.roleTypeService
        .deleteRoleTypes(listGridElement.map((x) => x.roleTypeId))
        .then(() => {
          this.msgService.successDelete();
          let tmpGrid = this.gridArray;
          listGridElement.forEach((w) => {
            tmpGrid = tmpGrid.filter((r) => r.roleTypeId != w.roleTypeId);
          });
          this.gridArray = tmpGrid;
        })
        .catch((error) => {
          this.msgService.error(error.message);
          this._reload.next();
        });
    } else {
      this.msgService.successDelete();
    }
  }

  shareSelectionItem(data) {
    this.selectionRoleTypes = data;
    if (this.selectionRoleTypes.length == 0) {
      setTimeout(() => {
        this.buttonDelete = false;
      }, 0);
    } else {
      setTimeout(() => {
        this.buttonDelete = true;
      }, 0);
    }
  }

  canDeactivate(): Observable<boolean> | boolean {
    return this.buttonSave;
  }

  hasDuplicateIds(array: RoleType[]): boolean {
    let ctrl: boolean = true;
    let oldArrayId = array
      .filter((x) => !x.variableGridArray.id.includes("new"))
      .map((x) => x.roleTypeId);
    array
      .filter((x) => x.variableGridArray.id.includes("new"))
      .map((x) => x.roleTypeId)
      .forEach((newId) =>
        oldArrayId.forEach((old) => {
          if (old == newId) ctrl = false;
        })
      );

    return ctrl;
  }

  collapseSidebar() {
    const dom: any = document.querySelector("body");
    const menu: any = document.querySelector("#sidebar");
    dom.classList.add("push-right");
    menu.classList.add("collapse");
  }

  
  resetAllElement(){
    this._reload.next();
  }

}
