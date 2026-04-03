import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Observable, Subject } from "rxjs";
import { map, mergeMap, mergeWith } from "rxjs/operators";
import {
  ConfirmationService,
  MenuItem,
  MessageService,
} from "primeng/api";
import { SelectItem } from "../../../../commons/model/selectitem";
import { Message } from "../../../../commons/model/message";
import { I18NService } from "../../../../i18n/i18n.service";
import { Uom } from "../../../../api/model/uom";
import { UomType } from "../../uom-type/uom_type";
import { UomService } from "../../../../api/service/uom.service";
import { LanguageService } from "../../../../api/service/language.service";
import {
  HeadArray,
  HeadFilter,
  ActionInput,
  ActionOutput,
} from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { Message as MessageError } from "primeng/api";
import { TableEditingCellService } from "app/commons/service/table-editing-cell.service";
import { Table } from "primeng/table";
import { ConfirmDialogService } from "app/commons/service/confirm-dialog.service";
import { MsgService } from "app/commons/service/message.service";

/** Convert from UomType[] to SelectItem[] */
function uomTypes2SelectItems(types: UomType[]): SelectItem[] {
  return types.map((ut: UomType) => {
    return { label: ut.description, value: ut.uomTypeId };
  });
}

@Component({
  selector: "app-uom",
  templateUrl: "./uom.component.html",
  styleUrls: ["./uom.component.css"],
})
export class UomComponent implements OnInit {
  _reload: Subject<void>;
  defaultUomType: UomType;
  uoms: Uom[];
  buttonNew: boolean = true;
  buttonDelete: boolean = false;
  buttonSave: boolean = false;
  selectedUoms: Uom[];
  headArray: HeadArray[];
  error = "";
  msgs: Message[] = [];
  selectedIndex: string;
  selectedUomTypeId: string;
  newUomToAdd: Uom;
  uomTypeIds: MenuItem[] = [];
  uomTypeIdsDescription: any[] = [];
  itemsButtonSlideMenu: MenuItem[];
  gridArray: Uom[] = [];
  tempGridArray: any[] = [];
  first = 0;
  rows = 10;
  idTable: string;
  uomRatingScales: any[] = [];
  uomToDelete: Uom;
  errorString = "";
  editingKeyId: string;
  elementToAdd: any;
  IsInvalidUpdate: boolean = false;
  dataTable: Table;
  langType: string;
  languages: string[] = [];
  flag: boolean = false;
  messagesError: MessageError[] = [];
  secondaryLang: boolean;
  er: boolean = false;
  reload: boolean = false;
  newRow: any;
  loading: boolean = true;

  /* ####### config for table component ########## */

  constructor(
    private readonly uomService: UomService,
    private readonly confirmationService: ConfirmationService,
    private readonly tbService: TableEditingCellService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly i18nService: I18NService,
    private readonly msgService: MsgService,
    private languageService: LanguageService,
    private confirmDialogService: ConfirmDialogService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();

    // Manage Reload
    const reloadedUomTypes = this._reload.pipe(
      mergeMap(() => this.uomService.uomTypes())
    );
    const reloadedUoms = this._reload.pipe(
      mergeMap(() => this.uomService.uoms())
    );

    const uomTypesObs = this.route.data.pipe(
      map((data: { uomTypes: UomType[] }) => data.uomTypes),
      mergeWith(reloadedUomTypes)
    );

    uomTypesObs.subscribe((data: UomType[]) => {
      data.forEach((x) => {
        this.uomTypeIds = [
          { label: x.description, id: x.uomTypeId },
          ...this.uomTypeIds,
        ];
      });
    });

    this.headArray = [
      {
        head: this.i18nService.translate("Uom Type Id"),
        fieldName: "uomType",
        textLength: 20,
        actionInput: ActionInput.dropdownData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.dropdownFilter,
        dropdown: { item: this.uomTypeIds, clear: false, key: "uomTypeId" },
        width: "8vw",
        sortIcon: true,
        required: true,
      },
      {
        head: this.i18nService.translate("Uom Id"),
        fieldName: "uomId",
        unique: true,
        textLength: 20,
        actionInput: ActionInput.outputData,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        width: "15vw",
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
        width: this.secondaryLang ? "15vw" : '20vw',
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
        width: "15vw",
        flag: true,
        sortIcon: true,
        required: true,
      },
      {
        head: this.i18nService.translate("Abbreviation"),
        fieldName: "abbreviation",
        textLength: 60,
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        width: this.secondaryLang ? "10vw" : "20vw",
        flag: true,
        sortIcon: true,
        required: true,
      },
      {
        head: this.i18nService.translate("Abbreviation"),
        fieldName: "abbreviationLang",
        textLength: 60,
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        width: "10vw",
        flag: true,
        sortIcon: true,
        required: true,
      },
      {
        head: this.i18nService.translate("Decimal Scale"),
        fieldName: "decimalScale",
        actionInput: ActionInput.inputLabelNumber,
        actionOutput: ActionOutput.outputLabelNumber,
        filter: HeadFilter.popUpFilter,
        content: "center",
        width: "5vw",
        sortIcon: false,
      },
      {
        head: this.i18nService.translate("Minimum Value"),
        fieldName: "minValue",
        actionInput: ActionInput.inputLabelNumber,
        actionOutput: ActionOutput.outputLabelNumber,
        filter: HeadFilter.popUpFilter,
        content: "center",
        width: "5vw",
        sortIcon: false,
      },
      {
        head: this.i18nService.translate("Maximum Value"),
        fieldName: "maxValue",
        actionInput: ActionInput.inputLabelNumber,
        actionOutput: ActionOutput.outputLabelNumber,
        filter: HeadFilter.popUpFilter,
        content: "center",
        width: "5vw",
        sortIcon: false,
      },
      {
        head: "",
        fieldName: "null",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.actionDetails,
        filter: HeadFilter.null,
        content: 'center'
      },
    ];

    const uomsObs = this.route.data.pipe(
      map((data: { uoms: Uom[] }) => data.uoms),
      mergeWith(reloadedUoms)
    );

    uomsObs.subscribe((data) => {
      this.uoms = data;
      this.gridArray = [];
      data.forEach((element, index) => {
        this.gridArray.push({
          uomTypeId: element.uomTypeId,
          uomType: element.uomType["description"],
          uomId: element.uomId,
          description: element.description,
          descriptionLang: element.descriptionLang,
          abbreviation: element.abbreviation,
          abbreviationLang: element.abbreviationLang,
          decimalScale: element.decimalScale,
          maxValue: element.maxValue,
          minValue: element.minValue,
          variableGridArray: {
            id: element.uomId,
            updated: false,
            dropdownData: false,
            inputNew: false,
            inputLabeldata: true,
            outputData: true,
            inputLabelNumber: true,
            buttonDetails: element.uomTypeId == "RATING_SCALE",
          },
        });
      });
      this.loading = false;
    });

    // Language control
    this.langType = this.i18nService.getLanguageType();
    this.languageService.language().subscribe((data) => {
      this.languages = data;
      if (this.languages.length > 1 && this.langType != "NONE") {
        this.flag = true;
        this.headArray[2].pathIconFlag = this.languages[0];
        this.headArray[4].pathIconFlag = this.languages[0];
        this.headArray[3].pathIconFlag = this.languages[1];
        this.headArray[5].pathIconFlag = this.languages[1];
      }
    });

    if (this.langType == "NONE") {
      this.headArray[3].display = "none";
      this.headArray[3].required = false;
      this.headArray[5].display = "none";
      this.headArray[5].required = false;
    }

    this.itemsButtonSlideMenu = [
      {
        label: "Rating Scales",
        icon: "pi pi-search",
        command: () => this.onRowDetail(),
      },
    ];
  }

  toDetail(itemClick) {
    this.router.navigate([`${itemClick.uomId}`], { relativeTo: this.route });
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
      let obj = new Uom();
      obj.abbreviation = e.abbreviation;
      obj.abbreviationLang = e.abbreviationLang;
      obj.decimalScale = e.decimalScale;
      obj.description = e.description;
      obj.descriptionLang = e.descriptionLang;
      obj.maxValue = e.maxValue;
      obj.minValue = e.minValue;
      obj.uomType = e.uomType;
      obj.uomTypeId = e.uomTypeId;
      obj.uomId = e.uomId;
      if ((obj.uomId != null || obj.uomId != undefined)) {
        await this.uomService.createUom(obj)
          .then((uomId) => {
            this.msgService.successCreateWithId(uomId);
            let ID = uomId;
            e.variableGridArray.id = ID;
            e.uomId = uomId;
            e.variableGridArray.updated = false;
            e.variableGridArray.inputNew = false;
            e.variableGridArray.outputData = true;
            e.variableGridArray.inputLabelDecimalNumber = false;
            e.uomTypeId = obj.uomTypeId;
            e.variableGridArray.buttonDetails = (obj.uomTypeId === "RATING_SCALE");

            this.gridArray = [...this.gridArray];
          })
          .catch((error) => {
            this.msgService.error(error);
            this.er = true;
          });
      }
      else {
        this.msgService.errorFieldsRequired();
        this.er = true;
      }
    })
    return !this.er;
  }

  update(gridElement) {

    gridElement.forEach(async e => {
      let obj = new Uom();
      obj.abbreviation = e.abbreviation;
      obj.abbreviationLang = e.abbreviationLang;
      obj.decimalScale = e.decimalScale;
      obj.description = e.description;
      obj.descriptionLang = e.descriptionLang;
      obj.maxValue = e.maxValue;
      obj.minValue = e.minValue;
      obj.uomType = e.uomType;
      obj.uomTypeId = e.uomTypeId;
      obj.uomId = e.uomId;

      if ((obj.uomId != null || obj.uomId != undefined)) {
        await this.uomService.updateUom(obj.uomId, obj)
          .then(() => {
            this.msgService.successUpdateWithId(obj.uomId);
            this._reload.next()
          })
          .catch((error) => {
            this.msgService.error(error.message);
            this.er = true;
          });
      }
      else {
        this.msgService.errorFieldsRequired();
        this.er = true;
      }
    });
    return !this.er;
  }

  delete(listGridElement: Uom[]) {

    if (listGridElement.filter(x => x.variableGridArray.id.includes("new")).length > 0) {
      this.newRow = listGridElement.filter(x => x.variableGridArray.id.includes("new"));
      listGridElement = listGridElement.filter(x => !this.newRow.includes(x));
      this.gridArray = this.gridArray.filter(x => !x.variableGridArray.id.includes("new"));
      this.newRow = null;
    }
    if (listGridElement.length > 0) {
      listGridElement.forEach(e => {

        let obj = new Uom();
        obj.uomId = e.uomId;

        this.uomService.deleteUom(obj.uomId)
          .then(() => {
            this.msgService.successDeleteWithId(obj.uomId);
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

  shareItemEvent(data: Uom) {
    this.selectedIndex = data.uomId;
  }

  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  openNew() {
    let ID = "new" + Math.random();
    let tmpElAdd: Uom = {
      uomTypeId: "",
      uomId: "",
      uomType: "",
      abbreviation: "",
      abbreviationLang: "",
      description: "",
      descriptionLang: "",
      decimalScale: 0,
      minValue: 0,
      maxValue: 0,
      variableGridArray: {
        inputLabeldata: true,
        outputData: false,
        inputNew: true,
        inputLabelNumber: true,
        dropdownData: true,
        id: ID,
        buttonMultipleDetails: false,
        updated: true,
      }
    }

    this.elementToAdd = tmpElAdd;
    this.gridArray = [this.elementToAdd, ...this.gridArray];
  }



  onRowDetail() {
    this.router.navigate([`${this.selectedIndex}`], { relativeTo: this.route });
  }

  resetAllElement() {
    this._reload.next();
  }

}
