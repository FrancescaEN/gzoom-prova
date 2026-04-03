import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DataSourceTypeService } from "app/api/service/dataSourceType.service";
import { I18NService } from "app/i18n/i18n.service";
import { ConfirmationService, MessageService } from "primeng/api";
import { Observable, Subject, map, mergeMap, mergeWith } from "rxjs";
import { Message as MessageError } from "primeng/api";
import { DataSourceType } from "app/api/model/dataSourceType";
import { Message } from "app/commons/model/message";
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";
import { MsgService } from "app/commons/service/message.service";

@Component({
  selector: "app-subsystem-types",
  templateUrl: "./subsystem-types.component.html",
  styleUrls: ["./subsystem-types.component.css"],
})
export class SubsystemTypesComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;

  msgs: Message[] = [];
  elementToAdd: DataSourceType;
  loading: boolean = true;

  headArray: HeadArray[] = [
    {
      head: "",
      fieldName: "id",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.outputLabelData,
      display: "none",
    },
    {
      head: this.i18nService.translate("Code"),
      fieldName: "dataSourceTypeId",
      actionInput: ActionInput.outputData,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      required: true,
      width: "15%",
    },
    {
      head: this.i18nService.translate("Description"),
      fieldName: "description",
      actionInput: ActionInput.inputLabeldata,
      actionOutput: ActionOutput.outputLabelData,
      filter: HeadFilter.textFilter,
      required: true,
    }
  ];

  gridArray: DataSourceType[] = [];
  newRow: any;
  er: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private readonly dataResourceTypeService: DataSourceTypeService,
    private readonly i18nService: I18NService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  ngOnInit() {


    const reloadWES = this._reload.pipe(
      mergeMap(() => this.dataResourceTypeService.getDataSourceType()),
    );

    const w$ = this.route.data.pipe(
      map((data: { dataSourceType: DataSourceType[] }) => data.dataSourceType),
      mergeWith(reloadWES),
    );

    w$.subscribe((y) => {

      this.gridArray = [];
      if (this.newRow) this.gridArray.push(this.newRow);
      y.forEach((e, index) => {
        this.gridArray.push({
          dataSourceTypeId: e.dataSourceTypeId,
          description: e.description,

          variableGridArray: {
            id: e.dataSourceTypeId,
            updated: false,
            buttonDetails: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: false,
            outputData: true,
            inputNew: false,
          },
        });
        this.loading = false;
      });
    });
  }

  openNew() {

    this.elementToAdd = {
      dataSourceTypeId: "",
      description: "",
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
      let obj = new DataSourceType(
        e.dataSourceTypeId,
        e.description
      );

      await this.dataResourceTypeService.createDataSourceType(obj)
        .then(() => {
          this.msgService.successCreateWithId(obj.dataSourceTypeId);
          e.variableGridArray.id = obj.dataSourceTypeId;
          e.variableGridArray.updated = false;
          e.variableGridArray.inputNew = false;
          e.variableGridArray.outputData = true;
          e.variableGridArray.buttonDetails = true;
          if (this.reload) this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.dataSourceTypeId);
          this.er = true;
        });

    })

    return !this.er;

  }

  update(gridElement) {
    gridElement.forEach(async e => {
      let obj = new DataSourceType(
        e.dataSourceTypeId,
        e.description
      );

      await this.dataResourceTypeService.updateDataSourceType(obj)
        .then(() => {
          this.msgService.successUpdateWithId(obj.dataSourceTypeId);
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.errorWithId(error, obj.dataSourceTypeId);
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

        this.dataResourceTypeService.deleteDataSourceType(e.dataSourceTypeId)
          .then(() => {
            this.msgService.successDeleteWithId(e.dataSourceTypeId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.dataSourceTypeId);
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
