import { Component, OnInit } from '@angular/core';
import { Observable, Subject, map, mergeMap, mergeWith, tap } from 'rxjs';
import { ActionInput, ActionOutput, HeadArray } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { LanguageService } from 'app/api/service/language.service';
import { HeadFilter } from 'app/layout/tables/table/table-configuration';
import { QueryConfigService } from 'app/api/service/query-config.service';
import { QueryConfig } from 'app/api/model/queryConfig';
import { DataForButton } from 'app/layout/toolbar-data-table/toolbar-data-table-configuration';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'app-detail-conditions',
  templateUrl: './detail-conditions.component.html',
  styleUrls: ['./detail-conditions.component.css']
})
export class DetailConditionsComponent implements OnInit {
  _reload: Subject<void>;
  reload: boolean = false;
  elementToAdd: any;
  loading: boolean = true;
  er: boolean = false;
  title: string;
  backLink: string;

  headArray: HeadArray[] = [
    { head: 'Index', fieldName: 'index', actionInput: ActionInput.null, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.null, readonly: true, content: 'center', width: '5vw', sortIcon: false },
    { head: 'Name', fieldName: 'condName', actionInput: ActionInput.inputLabeldata, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, textLength: 100 },
    { head: 'Instructions', fieldName: 'condComm', actionInput: ActionInput.inputNotes, actionOutput: ActionOutput.outputNotes, filter: HeadFilter.textFilter, textLength: 255 },
    { head: 'Condition', fieldName: 'condInfo', actionInput: ActionInput.inputNotes, actionOutput: ActionOutput.outputNotes, filter: HeadFilter.textFilter, },
    {
      head: "",
      fieldName: "null",
      actionInput: ActionInput.null,
      actionOutput: ActionOutput.null,
      filter: HeadFilter.null,
      width: '0.5vw'
    }
  ]

  gridArray: any[] = [];
  secondaryLang: boolean;
  queryId: string;
  queryType: string;
  queryConfig: QueryConfig;

  buttonToQuery: DataForButton = {
    icon: 'pi pi-angle-right',
    titleLabel: 'Query',
    booleanShow: true
  }

  constructor(
    private route: ActivatedRoute,
    private readonly queryConfigService: QueryConfigService,
    private readonly i18nService: I18NService,
    private readonly router: Router,
    private readonly languageService: LanguageService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
  }

  async ngOnInit() {
    this.secondaryLang = await this.languageService.secondaryLang();
    this.queryId = this.route.snapshot.parent.params.id;

    this.queryType = this.route.snapshot.parent.params.queryType;
    this.backLink = '../../'   

    const reload = this._reload.pipe(mergeMap(() => this.queryConfigService.getQueryConfig(this.queryId)));
    const w$ = this.route.data.pipe(
      map((data: { obss: QueryConfig }) => data.obss),
      mergeWith(reload),
      tap(qc =>  this.title = qc.queryCode + ". " + qc.queryName)
    );

    w$.subscribe(y => {      
      this.gridArray = [];
      this.queryConfig = y;

      for (let index = 0; index < 8; index++) {
        this.gridArray.push({
          index: index.toString(),
          condName: y["cond" + index + "Name"],
          condComm: y["cond" + index + "Comm"],
          condInfo: y["cond" + index + "Info"],

          variableGridArray: {
            id: index.toString(),
            updated: false,
            inputLabeldata: true,
            inputLabelNumber: true,
            inputNotes: true,
            outputData: true,
            inputNew: false,
            dropdownData: true,
            buttonMultipleDetails: true
          }
        });
      }
      this.loading = false;
    })   

  }

  async saveAllElement(gridElement) {
    gridElement.forEach(async e => {
      for (let index = 0; index < 8; index++) {
        if (e.index == index) {
          this.queryConfig["cond" + index + "Comm"] = e.condComm;
          this.queryConfig["cond" + index + "Info"] = e.condInfo;
          this.queryConfig["cond" + index + "Name"] = e.condName;
        }
      }
    });

    await this.queryConfigService.updateConditions(this.queryConfig)
      .then(() => {
        this.msgService.successUpdate()
        this._reload.next()
      })
      .catch((error) => {
        this.msgService.error(error);
        this.er = true;
      });

    return !this.er;

  }

  toQuery(event) {
    if (this.queryType) {
      this.router.navigate([`/c/CTX_WE/interoperability/query-configuration-query-type/${this.queryType}/query/${this.queryId}`]);
    }
    else {
      this.router.navigate([`/c/CTX_WE/interoperability/query-configuration/query/${this.queryId}`]);
    }
  }

  delete(listGridElement) {
    if (listGridElement) {
      let id: string[] = listGridElement.map(x => x.index);

      for (let index = 0; index < this.gridArray.length; index++) {
        const element = this.gridArray[index];
        if (id.includes(element['index'])) {
          element.condName = null;
          element.condComm = null;
          element.condInfo = null;
        }
      }

      this.saveAllElement(listGridElement);
      let tmpGrid = this.gridArray;
      tmpGrid = tmpGrid.filter(r => !id.includes(r['index']));
      this.gridArray = tmpGrid;
    }
  }

  canDeactivate(): boolean {    
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }
}
