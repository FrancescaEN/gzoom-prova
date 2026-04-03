import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router, Params } from "@angular/router";
import { Observable, Subject } from "rxjs";
import { map, mergeWith, switchMap } from "rxjs/operators";
import { ConfirmationService } from "primeng/api";
import { I18NService } from "../../../i18n/i18n.service";
import { UomType } from "./uom_type";
import { UomService } from "../../../api/service/uom.service";
import { Message as MessageError } from "primeng/api";
import { Message } from "app/commons/model/message";
import {
  ActionInput,
  ActionOutput,
  HeadArray,
  HeadFilter,
} from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { Table } from "primeng/table";

@Component({
  selector: "app-uom-type",
  templateUrl: "./uom-type.component.html",
  styleUrls: ["./uom-type.component.scss"],
})
export class UomTypeComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;
  /** Info message in Toast*/
  msgs: Message[] = [];
  /** Grid with row of the table*/
  gridArray: any[] = [];
  /** Header of the table*/
  headArray: HeadArray[] = [
    {
      head: "",
      fieldName: "id",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      display: "none",
      filter: HeadFilter.null,
    },
    {
      head: this.i18nService.translate("Uom Type Id"),
      fieldName: "uomTypeId",
      actionInput: ActionInput.outputData,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      width: "10vw",
      required: true,
      unique: true,
      textLength: 20,
    },
    {
      head: this.i18nService.translate("Description"),
      fieldName: "description",
      actionInput: ActionInput.inputLabeldata,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      required: true,
      textLength: 255,
      width: "40vw",
    },
  ];

  messagesError: MessageError[] = [];
  elementToAdd: any;
  loading: boolean = true;
  er: boolean = false;
  selectionList: any[] = [];
  newRow: any;
  dataTable: Table;

  constructor(
    private readonly uomService: UomService,
    private readonly confirmationService: ConfirmationService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly i18nService: I18NService
  ) {
    this._reload = new Subject<void>();
  }

  ngOnInit() {
    const reloadedUomTypes = this._reload.pipe(
      switchMap(() => this.uomService.uomTypes())
    );

    this.route.data
      .pipe(
        map((data: { uomTypes: UomType[] }) => data.uomTypes),
        mergeWith(reloadedUomTypes)
      )
      .subscribe((data) => {
        this.gridArray = [];
        data.forEach((element, index) => {
          this.gridArray.push({
            uomTypeId: element.uomTypeId,
            description: element.description,
            variableGridArray: {
              id: element.uomTypeId,
              updated: false,
              buttonDetails: false,
              inputLabeldata: true,
              inputLabelNumber: true,
              inputNotes: false,
              outputData: true,
              inputNew: false,
            },
          });
        });
        this.loading = false;
      });
  }

  canDeactivate(): Observable<boolean> | boolean {

    return (
      this.gridArray.filter((x) => x.variableGridArray.updated == true).length >
      0
    )
  }

  openNew() {
    this.elementToAdd = {
      uomTypeId: "",
      description: "",
      variableGridArray: {
        id: "new" + Math.random(),
        updated: true,
        buttonDetails: false,
        inputLabeldata: true,
        inputLabelNumber: true,
        inputNotes: false,
        outputData: false,
        inputNew: true,
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
      let obj = new UomType(e.uomTypeId, e.description);

      await this.uomService
        .createUomType(obj)
        .then(() => {
          this.msgs = [
            {
              severity: this.i18nService.translate("info"),
              summary: this.i18nService.translate("Confirmed"),
              detail:
                this.i18nService.translate("New item added") +
                " [" +
                obj.uomTypeId +
                "]",
            },
          ];
          e.variableGridArray.id = e.uomTypeId;
          e.variableGridArray.updated = false;
          if (this.reload) this._reload.next();
        })
        .catch((error) => {
          this.messagesError = [
            {
              severity: "error",
              summary: "Error",
              detail: error + " [" + obj.uomTypeId + "]",
            },
          ];
          this.er = true;
        });
    });

    return !this.er;
  }

  update(gridElement) {
    gridElement.forEach(async (e) => {
      let obj = new UomType(e.uomTypeId, e.description);

      await this.uomService
        .updateUomType(obj.uomTypeId, obj)
        .then(() => {
          this.msgs = [
            {
              severity: this.i18nService.translate("info"),
              summary: this.i18nService.translate("Confirmed"),
              detail:
                this.i18nService.translate("Update Confirmation") +
                " [" +
                obj.uomTypeId +
                "]",
            },
          ];
          this._reload.next();
        })
        .catch((error) => {
          this.messagesError = [
            {
              severity: "error",
              summary: "Error",
              detail: error + " [" + obj.uomTypeId + "]",
            },
          ];
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
      listGridElement.forEach((listElement) => {
        this.uomService
          .deleteUomType(listElement.uomTypeId)
          .then(() => {
            this.msgs = [
              {
                severity: this.i18nService.translate("info"),
                summary: this.i18nService.translate("Confirmed"),
                detail:
                  this.i18nService.translate("Delete confirmation") +
                  " [" +
                  listElement.uomTypeId +
                  "]",
              },
            ];

            let tmpGrid = this.gridArray;
            this.gridArray = tmpGrid.filter(
              (x) => x.uomTypeId != listElement.uomTypeId
            );
          })
          .catch((error) => {
            this.messagesError = [
              {
                severity: "error",
                summary: "Error",
                detail: error.message + " [" + listElement.uomTypeId + "]",
              },
            ];
          });
      });
    } else {
      this.msgs = [
        {
          severity: this.i18nService.translate("info"),
          summary: this.i18nService.translate("Confirmed"),
          detail: this.i18nService.translate("Delete confirmation"),
        },
      ];
    }
    this.dataTable.updateEditingCell(null, null, null, null);
  }

  
  resetAllElement(){
    this._reload.next();
  }

}
