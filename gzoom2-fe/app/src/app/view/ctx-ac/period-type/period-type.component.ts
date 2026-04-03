import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { PeriodTypeService } from "app/api/service/period-type.service";
import { I18NService } from "app/i18n/i18n.service";
import {
  ActionInput,
  ActionOutput,
  HeadArray,
  HeadFilter,
} from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { Observable, Subject, map, mergeMap, mergeWith } from "rxjs";
import { PeriodType } from "../../../api/model/period-type";
import { MsgService } from "app/commons/service/message.service";



@Component({
  selector: "app-period-type",
  templateUrl: "./period-type.component.html",
  styleUrls: ["./period-type.component.css"],
})
export class PeriodTypeComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;
  gridArray: PeriodType[] = [];
  elementToAdd: any;
  loading: boolean = true;
  selectionPeriodTypes: PeriodType[];

  headArray: HeadArray[] = [
    {
      head: this.i18nService.translate("Code"),
      fieldName: "periodTypeId",
      actionInput: ActionInput.outputData,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      sortIcon: true,
      required: true,
      unique: true,
      textLength: 20,
      width: "15vw",
    },
    {
      head: this.i18nService.translate("Description"),
      fieldName: "description",
      actionInput: ActionInput.inputLabeldata,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      sortIcon: true,
      required: true,
      textLength: 255,
      width: "15vw",
    },
    {
      head: "",
      fieldName: "null",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.null,
      filter: HeadFilter.null,
      width: '0.1vw'
    }
  ];

  newRow: any;
  er: boolean = false;

  constructor(
    private readonly periodTypeService: PeriodTypeService,
    private readonly route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  ngOnInit(): void {
    const reloadedPeriodTypes = this._reload.pipe(
      mergeMap(() => this.periodTypeService.periodTypes())
    );

    const periodTypesObs = this.route.data.pipe(
      map((data: { periodTypes: PeriodType[] }) => data.periodTypes),
      mergeWith(reloadedPeriodTypes)
    );

    periodTypesObs.subscribe((data) => {
      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      data.forEach((element) => {
        this.gridArray.push({
          description: element.description,
          periodTypeId: element.periodTypeId,
          variableGridArray: {
            id: element.periodTypeId,
            inputNew: false,
            inputLabeldata: true,
            outputData: true,
            updated: false,
            dropdownData: false,
          },
        });
        this.loading = false;
      });
    });
  }

  openNew() {

    this.elementToAdd = {
      description: null,
      periodTypeId: null,
      variableGridArray: {
        updated: true,
        inputNew: true,
        id: "new" + Math.random(),
        inputLabeldata: true,
        outputData: false,
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
      let obj = new PeriodType(
        e.periodTypeId,
        e.description
      );

      await this.periodTypeService.createPeriodType(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.periodTypeId);
          e.variableGridArray.id = obj.periodTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.periodTypeId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new PeriodType(
        e.periodTypeId,
        e.description
      );

      await this.periodTypeService.updatePeriodTypes(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.periodTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.periodTypeId);
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

        this.periodTypeService.deletePeriodType(e.periodTypeId)
          .then(() => {
            this.msgService.successDeleteWithId(e.periodTypeId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.periodTypeId);
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
