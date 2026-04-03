import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { EmplPositionType } from "app/api/model/emplPositionType";
import { EmplPositionTypeService } from "app/api/service/empl-position-type.service";
import { LanguageService } from "app/api/service/language.service";
import { WorkEffortService } from "app/api/service/work-effort.service";
import { MsgService } from "app/commons/service/message.service";
import { I18NService } from "app/i18n/i18n.service";
import {
  ActionInput,
  ActionOutput,
  HeadArray,
  HeadFilter,
} from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { MenuItem } from "primeng/api";

import {
  Observable,
  Subject,
  lastValueFrom,
  map,
  mergeMap,
  mergeWith,
} from "rxjs";

@Component({
  selector: "app-positions-economics",
  templateUrl: "./positions-economics.component.html",
  styleUrls: ["./positions-economics.component.css"],
})
export class PositionsEconomicsComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;
  emplPositionTypes: EmplPositionType[] = [];
  gridArray: any[] = [];
  elementToAdd: any;
  selectionEmplPositionTypes: EmplPositionType[];
  langType: string;
  languages: string[] = [];
  flag: boolean = false;
  emplPositionIdArray: MenuItem[] = [];
  templateIdArray: MenuItem[] = [];
  loading: boolean = true;
  secondaryLang: boolean;

  headArray: HeadArray[] = [];
  newRow: any;
  er: boolean = false;

  constructor(
    private readonly emplPositionTypeService: EmplPositionTypeService,
    private readonly route: ActivatedRoute,
    private readonly workEffortService: WorkEffortService,
    private languageService: LanguageService,
    private readonly i18nService: I18NService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit(): Promise<void> {
    this.loading = true;
    this.secondaryLang = await this.languageService.secondaryLang();
    let languageType = this.i18nService.getLanguageType();

    this.headArray.push(
      {
        head: this.i18nService.translate("Code"),
        fieldName: "emplPositionTypeId",
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        textLength: 20,
        filter: HeadFilter.popUpFilter,
        sortIcon: true,
        required: true,
        unique: true,
        width: "5vw",
      },
      {
        head: this.i18nService.translate("Description"),
        fieldName: "description",
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        textLength: 255,
        filter: HeadFilter.textFilter,
        flag: true,
        sortIcon: true,
        required: true,
      });
    if (languageType == "BILING") this.headArray.push(
      {
        head: this.i18nService.translate("Description"),
        fieldName: "descriptionLang",
        textLength: 255,
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        flag: true,
        sortIcon: true,
        required: true,
      });
    this.headArray.push(
      {
        head: this.i18nService.translate("Classification"),
        fieldName: "parentTypeIdDescription",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        textLength: 255,
        dropdown: {
          item: this.emplPositionIdArray,
          clear: true,
          key: "parentTypeId",
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'parentTypeIdDescription').forEach(y => y.dropdown.loading = true);

            this.emplPositionIdArray = [];
            await lastValueFrom(
              this.emplPositionTypeService.emplPositionTypes()
            )
              .then((x) => {
                x.forEach((y) => {
                  this.emplPositionIdArray.push({
                    label: this.secondaryLang
                      ? y.descriptionLang
                      : y.description,
                    id: y.emplPositionTypeId,
                  });
                });
              })
              .catch((error) => console.log(error));

            this.headArray
              .filter((x) => x.fieldName == "parentTypeIdDescription")
              .forEach((y) => (y.dropdown.item = this.emplPositionIdArray));
            this.headArray.filter(x => x.fieldName == 'parentTypeIdDescription').forEach(y => y.dropdown.loading = false);

          },
        },
        sortIcon: true,
      },
      {
        head: this.i18nService.translate("Template"),
        fieldName: "templateIdDescription",
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        textLength: 20,
        dropdown: {
          item: this.templateIdArray,
          clear: true,
          key: "templateId",
          loading: false,
          command: async () => {
            this.headArray.filter(x => x.fieldName == 'templateIdDescription').forEach(y => y.dropdown.loading = true);

            this.templateIdArray = [];
            await lastValueFrom(
              this.workEffortService.workEffortsIsRootIsTemplate()
            ).then((element) => {
              element.forEach((item) => {
                this.templateIdArray.push({
                  label: ((!this.secondaryLang) ? item.workEffortName : item.workEffortNameLang),
                  id: item.workEffortId
                });

              });
            })
              .catch((error) => console.log(error));

            this.headArray
              .filter((x) => x.fieldName == "templateIdDescription")
              .forEach((y) => (y.dropdown.item = this.templateIdArray));
            this.headArray.filter(x => x.fieldName == 'templateIdDescription').forEach(y => y.dropdown.loading = false);

          },
        },
        sortIcon: true,
      },{
        head: "",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.null,
        filter: HeadFilter.null,
        width:'0.5vw'
      }
    );
    // Language control
    if (this.i18nService.getLanguageType() == "BILING") {
      this.flag = true;
      let lang = await lastValueFrom(this.languageService.language());

      this.headArray[1].pathIconFlag = lang[0];
      this.headArray[2].pathIconFlag = lang[1];
    }

    await lastValueFrom(
      this.workEffortService.workEffortsIsRootIsTemplate()
    ).then((element) => {
      element.forEach((item) => {
        this.templateIdArray.push({
          label: ((!this.secondaryLang) ? item.workEffortName : item.workEffortNameLang),
          id: item.workEffortId
        });

      });
    });

    const reloadedPositionsEconomicsTypeService = this._reload.pipe(
      mergeMap(() => this.emplPositionTypeService.emplPositionTypes())
    );

    const positionsEconomicsTypesObs = this.route.data.pipe(
      map(
        (data: { emplPositionType: EmplPositionType[] }) =>
          data.emplPositionType
      ),
      mergeWith(reloadedPositionsEconomicsTypeService)
    );

    positionsEconomicsTypesObs.subscribe((data) => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      this.emplPositionIdArray = [];
      data.forEach((element) => {
        this.emplPositionIdArray.push({
          label: element.description,
          id: element.emplPositionTypeId,
        });
      });
      data.forEach((element) => {
        this.gridArray.push({
          description: element.description,
          descriptionLang: element.descriptionLang,
          emplPositionTypeId: element.emplPositionTypeId,
          parentTypeId: element.parentTypeId,
          parentTypeIdDescription: this.emplPositionIdArray
            .filter((x) => x.id === element.parentTypeId)
            .map((y) => y.label)[0],
          templateId: element.templateId,
          templateIdDescription: this.templateIdArray
            .filter((x) => x.id === element.templateId)
            .map((y) => y.label)[0],
          variableGridArray: {
            id: element.emplPositionTypeId,
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

  openNew() {

    this.elementToAdd = {
      description: null,
      descriptionLang: null,
      emplPositionTypeId: null,
      parentTypeId: null,
      templateId: null,
      variableGridArray: {
        inputNew: true,
        id: "new" + Math.random(),
        inputLabeldata: true,
        outputData: false,
        dropdownData: true,
        updated: true
      },
    };

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
      let obj = new EmplPositionType(
        e.emplPositionTypeId,
        e.description,
        e.descriptionLang,
        e.parentTypeId,
        e.templateId
      );

      await this.emplPositionTypeService.createEmplPositionTypes(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.emplPositionTypeId);
          e.variableGridArray.id = obj.emplPositionTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.emplPositionTypeId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new EmplPositionType(
        e.emplPositionTypeId,
        e.description,
        e.descriptionLang,
        e.parentTypeId,
        e.templateId
      );

      await this.emplPositionTypeService.updateEmplPositionTypes(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.emplPositionTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.emplPositionTypeId);
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

        this.emplPositionTypeService.deleteEmplPositionTypes(e.emplPositionTypeId)
          .then(() => {
            this.msgService.successDeleteWithId(e.emplPositionTypeId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error, e.emplPositionTypeId);
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

}
