import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WorkEffortAnalysisEx } from 'app/api/model/workEffortAnalysisEx';
import { WorkEffortAnalysisService } from 'app/api/service/work-effort-analysis.service';
import { WorkEffortTypeService } from 'app/api/service/work-effort-type.service';
import { MsgService } from 'app/commons/service/message.service';
import { ToolbarService } from 'app/commons/service/toolbar.service';
import { I18NService } from 'app/i18n/i18n.service';
import { ActionInput, ActionOutput, HeadArray, HeadFilter } from 'app/layout/tables/table-editing-cell/table-editing-cell-configuration';
import { Subject, Subscription, map, mergeMap, mergeWith, tap } from 'rxjs';

@Component({
  selector: 'app-analysis-type',
  templateUrl: './analysis-type.component.html',
  styleUrls: ['./analysis-type.component.css']
})
export class AnalysisTypeComponent implements OnInit {

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
  context: string;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly router: Router,
    private workEffortAnalysisService: WorkEffortAnalysisService,
    private workEffortTypeService: WorkEffortTypeService,
    private toolbarService: ToolbarService,
    private msgService: MsgService
  ) {
    this._reload = new Subject<void>();
    this.secondaryLang = this.i18nService.getIsSecondaryLang();
    this.toolbarService.setPrimaryBoardComponentButton();
    this.context = this.route.snapshot.parent.data.context;
  }

  ngOnInit(): void {
    const reload = this._reload.pipe(mergeMap(() => this.workEffortAnalysisService.getWorkEffortAnalysisExList(this.context)));
    const w$ = this.route.data.pipe(

      map((data: { obss: WorkEffortAnalysisEx[] }) => data.obss),
      mergeWith(reload)
    );

    this.setHeadArray();





    w$.pipe(
      tap(() => {
        this.gridArray = [];
        if (this.newRow) this.gridArray.push(this.newRow);
      }),
      map((array: WorkEffortAnalysisEx[]) => {

        array.map((x) => {

          this.gridArray.push({
            workEffortAnalysisId: x.workEffortAnalysisId,
            description: this.secondaryLang ? x.descriptionLang : x.description,
            context: this.secondaryLang ? x.parentWorkEffortType.descriptionLang : x.parentWorkEffortType.description,
            workEffortType: this.secondaryLang ? x.workEffortType.descriptionLang : x.workEffortType.description,
            referenceDate: x.referenceDate ? new Date(x.referenceDate) : null,

            variableGridArray: {
              id: x.workEffortAnalysisId,
              updated: false,
              inputLabeldata: true,
              inputLabelNumber: true,
              inputNotes: true,
              outputData: true,
              inputNew: false,
              dropdownData: true,
              buttonDetails: true
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
      { head: 'Code', fieldName: 'workEffortAnalysisId', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, required: true, unique: true, width: '11vw', textLength: 20 },
      { head: 'Description', fieldName: 'description', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, readonly: true, width: '25vw', textLength: 255 },
      { head: 'Context', fieldName: 'context', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, readonly: true, width: '15vw' },
      { head: 'Analysis', fieldName: 'workEffortType', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputLabelData, filter: HeadFilter.textFilter, readonly: true, width: '20vw' },
      { head: 'Date', fieldName: 'referenceDate', actionInput: ActionInput.outputData, actionOutput: ActionOutput.outputDate, filter: HeadFilter.dateFilter, readonly: true, width: '12vw' },
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

  openNew() {
    this.router.navigate([`add/new`], { relativeTo: this.route });
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

        this.workEffortAnalysisService.deleteWorkEffortAnalysis(e.workEffortAnalysisId)
          .then(() => {
            this.msgService.successDeleteWithId(e.workEffortAnalysisId);
            let tmpGrid = this.gridArray;
            tmpGrid = tmpGrid.filter(r => (r.variableGridArray.id != e.variableGridArray.id));
            this.gridArray = tmpGrid;
          })
          .catch((error) => {
            this.msgService.errorWithId(error.message, e.workEffortAnalysisId);
            this._reload.next();
          });

      });

    }
    else {
      this.msgService.successDelete();
    }

  }

  toDetail(itemClick) {
    this.router.navigate([`${itemClick.workEffortAnalysisId}`], { relativeTo: this.route });
  }


  canDeactivate(): boolean {
    return (this.gridArray.filter(x => x.variableGridArray.updated == true).length > 0);
  }

  resetAllElement() {
    this._reload.next();
  }
}
