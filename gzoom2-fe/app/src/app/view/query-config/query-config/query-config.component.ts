import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import {
  UntypedFormControl,
  UntypedFormGroup,
  UntypedFormBuilder,
  UntypedFormArray,
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
} from "@angular/forms";
import { lastValueFrom, Subject } from "rxjs";
import { map, mergeWith, mergeMap } from "rxjs/operators";
import { ConfirmationService, MenuItem } from "primeng/api";
import { SelectItem } from "../../../commons/model/selectitem";
import { Message } from "../../../commons/model/message";
import { I18NService } from "../../../i18n/i18n.service";
import { QueryConfig } from "../../../api/model/queryConfig";
import { QueryConfigService } from "../../../api/service/query-config.service";
import {
  HeadArray,
  HeadFilter,
  ActionInput,
  ActionOutput,
} from "app/layout/tables/table/table-configuration"
import { Context } from "app/commons/enum/context";
import { EnumerationService } from "app/api/service/enumeration.service";
import { LanguageService } from "app/api/service/language.service";
import { dropdownExpMimeType } from "app/commons/model/expMimeType";

/** Convert from queryConfig[] to SelectItem[] */
function queryConfig2SelectItems(types: QueryConfig[]): SelectItem[] {
  return types.map((qc: QueryConfig) => {
    return { label: qc.queryName, value: qc.queryId };
  });
}

@Component({
  selector: "app-query-config",
  templateUrl: "./query-config.component.html",
  styleUrls: ["./query-config.component.css"],
})
export class QueryConfigComponent implements OnInit {
  _reload: Subject<void>;
  displayDialog: boolean;
  error = "";
  msgs: Message[] = [];
  newQueryConfig: boolean = false;
  selectedIndex = -1;
  selectedQueryConfig: QueryConfig;
  queryConfig: QueryConfig = new QueryConfig();
  queryConfigs: QueryConfig[];
  queryConfigSelectItem: SelectItem[] = [];
  queryPreview: string;
  parentTypeId: string;
  queryType: string;
  classSelected: string;
  itemsButtonSlideMenu: MenuItem[];
  gridArray: any[] = [];
  form: {}

  dropdownQueryType: MenuItem[] = [];

  headArray: HeadArray[] = [];

  filterArray = [
    "queryCode",
    "queryName",
    "queryComm",
    "queryType",
    "queryCtx",
  ];


  secondaryLang: boolean;

  constructor(
    private readonly queryConfigService: QueryConfigService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly languageService: LanguageService,
    private readonly enumerationService: EnumerationService,
    private fb: FormBuilder
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    this.form = new FormGroup({
      queryCode: new FormControl(''),
      queryName: new FormControl(''),
      queryComm: new FormControl(''),
      queryType: new FormControl(''),
      queryCtx: new FormControl('')
    });

    this.parentTypeId = this.route.parent.snapshot.data.context;    

    this.queryType = this.route.snapshot.paramMap.get("id");

    this.setHeadArray();

    console.log("Query parameter: " + this.parentTypeId + this.queryType);

    this.dropdownQueryType = await this.setEnumerationDropdown("QUERY_TYPE");

    const reloadedQuerys = this._reload.pipe(
      mergeMap(() =>
        this.queryConfigService.queryConfigs(
          this.parentTypeId === Context.CTX_WE ? null : this.parentTypeId,
          this.queryType,
        ),
      ),
    );

    const queryConfigObs = this.route.data.pipe(
      map((data: { queryConfigs: QueryConfig[] }) => data.queryConfigs),
      mergeWith(reloadedQuerys),
    );

    queryConfigObs.subscribe((data) => {
      this.queryConfigs = data;
      console.log("@QUERY CONFIG", data);

      this.gridArray = [];
      data.forEach((element, index) => {
        this.gridArray.push({
          id: element.queryCode,
          idNumber: index,
          queryId: element.queryId,
          queryCode: element.queryCode,
          queryName: element.queryName,
          queryComm: element.queryComm,
          queryType: element.queryType,
          queryTypeDesc: this.dropdownQueryType.find(
            (x) => x.id == element.queryType,
          )?.label,

          exportMimeTypeDesc: dropdownExpMimeType.find(x => x.id === element.exportMimeType)?.label,
          queryCtx: element.queryCtx,
          buttonDetails: true,
        });
      });

      // var tempArray = String[20] = [];
      // data.forEach(element => {
      //   if(!(tempArray.includes(element.queryType))){
      //     tempArray.push(element.queryType);
      //     this.queryConfigType = [{ label: element.queryType, value: element.queryType }, ...this.queryConfigType];
      //   }
      // })
      // tempArray = [];
    });
  }

  setHeadArray() {
    this.headArray.push(
      {
        head: "",
        fieldName: "id",
        actionInput: ActionInput.null,
        actionOutput: ActionOutput.outputLabelData,
        display: "none",
        filter: HeadFilter.null,
      },
      {
        head: "Code",
        fieldName: "queryCode",
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        sortIcon: true,
        width: "10vw",
      },
      {
        head: "Operation Type",
        fieldName: "queryTypeDesc",
        actionInput: ActionInput.dropdownData,
        filter: HeadFilter.dropdownFilter,
        actionOutput: ActionOutput.outputLabelData,
        sortIcon: true,
        width: "12vw",
        dropdown: {
          item: this.dropdownQueryType,
          clear: false,
          loading: false,
          key: "workEffortId",
          virtualScrollItemSize: 10,
          virtualScroll: true,
          disabled: false,
        }
      },
      {
        head: "Export Type",
        fieldName: "exportMimeTypeDesc",
        filter: HeadFilter.dropdownFilter,
        actionOutput: ActionOutput.outputLabelData,
        sortIcon: true,
        width: "12vw",
        dropdown: {
          item: dropdownExpMimeType,
        },
        display: !(this.queryType == 'E')? "none" : null
      },
      {
        head: "Name",
        fieldName: "queryName",
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        sortIcon: true,
        width: "10vw",
      },
      {
        head: "Description",
        fieldName: "queryComm",
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        sortIcon: true,
        width: "10vw",
      },
      {
        head: "Contesto",
        fieldName: "queryCtx",
        actionInput: ActionInput.inputLabeldata,
        actionOutput: ActionOutput.outputLabelData,
        filter: HeadFilter.textFilter,
        sortIcon: true,
        width: "10vw",
        display: !(this.parentTypeId===Context.CTX_WE)? "none": "table-cell"
      }
    )
    
  }

  async setEnumerationDropdown(enumTypeId: string): Promise<MenuItem[]> {
    let tmpDropdown: MenuItem[] = [];
    await lastValueFrom(this.enumerationService.enumerations(enumTypeId))
      .then((e) => {
        e.forEach((item) =>
          tmpDropdown.push({
            label: !this.secondaryLang
              ? item.description
              : item.descriptionLang,
            id: item.enumId,
          }),
        );
        return tmpDropdown;
      })
      .catch((error) => console.log(error));

    return tmpDropdown;
  }

  onRowSelect(data) {
    // const query = querys && querys.length ? querys[0] : null;
    if (data.data.queryCode) {
      this.selectedIndex = data.data.idNumber;
      this.classSelected = "rowSelected";
      this.router.navigate([data.data.queryId], { relativeTo: this.route });
    } else {
      this.selectedIndex = -1;
      this.classSelected = "";
    }
  }

  closeRow() {
    console.log("@ CLOSE");

    this.selectedIndex = -1;
    this.classSelected = "";
    if (this.parentTypeId && this.queryType) {
      this.router.navigate([`../${this.queryType}`], {
        relativeTo: this.route.parent,
      });
    } else {
      this.router.navigate(["../queryconfig"], {
        relativeTo: this.route.parent,
      });
    }
  }

  showDialog(ri: number) {
    this.error = "";
    this.displayDialog = true;
    this.queryConfig = this.queryConfigs[ri];
  }
}
