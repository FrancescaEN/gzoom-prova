import { Component, OnInit } from '@angular/core';
import { UntypedFormArray, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { WorkEffortAnalysisService } from 'app/api/service/work-effort-analysis.service';
import { WorkEffortAnalysis } from 'app/api/model/workEffortAnalysis';
import { I18NService } from 'app/i18n/i18n.service';
import { Subject } from 'rxjs';
import { map, mergeMap, mergeWith } from 'rxjs/operators';
import { DataStorageService } from 'app/commons/service/data-storage.service';

@Component({
  selector: 'app-list-analysis',
  templateUrl: './analysis-list.component.html',
  styleUrls: ['./analysis-list.component.css']
})
export class AnalysisListComponent implements OnInit {
  analyses: WorkEffortAnalysis[];

  _reload: Subject<void>;

  gridArray: any[] = [];

  context: string;

  /** Row index selected for uomRatingScale*/
  selectedIndex = -1;

  form: { [name: string]: UntypedFormGroup | UntypedFormControl | UntypedFormArray };

  currentAnalysis: WorkEffortAnalysis;
  analysisItem: any;

  /* ####### config for table component ########## */

  headArray = [
    { head: '', fieldName: 'idNumber', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null" },
    { head: this.i18nService.translate('Date'), fieldName: 'referenceDate', actionInput: 'null', actionOutput: 'outputClickLabelData', display: "table-cell", filter: "textFilter", width: "20em" },
    { head: this.i18nService.translate('Code'), fieldName: 'workEffortAnalysisId', actionInput: 'null', actionOutput: 'outputClickLabelData', display: "table-cell", filter: "textFilter", width: "20em", sortIcon: true },
    { head: this.i18nService.translate('Description'), fieldName: 'description', actionInput: 'null', actionOutput: 'outputClickLabelData', display: "table-cell", filter: "textFilter", sortIcon: true },
  ];


  constructor(
    private readonly workEffortAnalysisService: WorkEffortAnalysisService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly i18nService: I18NService,
    private readonly dataStorageService: DataStorageService
  ) {

    this._reload = new Subject<void>();
    this.context = this.route.parent.snapshot.data.context;
  }

  ngOnInit() {
    this.collapseSidebarFun();

    this.form = {
      /*'context': new FormControl(''),
      'description': new FormControl(''),*/
    };

    this.route.paramMap.subscribe(paramMap => {
      this.gridArray = [];
      this.analyses = [];
    })

    const reloadedAnalyses = this._reload.pipe(mergeMap(() => this.workEffortAnalysisService.getWorkEffortAnalysisWithContext(this.context)));

    const analysesObs = this.route.data.pipe(
      map((data: { analyses: WorkEffortAnalysis[] }) => data.analyses),
      mergeWith(reloadedAnalyses)
    );


    analysesObs.subscribe((data) => {
      this.analyses = data;

      data.forEach((element, index) => {
        let date = element.referenceDate? new Date(element.referenceDate) : null;

        this.gridArray.push({
          id: index,
          idNumber: index,
          referenceDate: date.toLocaleDateString(),
          analysisRefDate: date,
          workEffortAnalysisId: element.workEffortAnalysisId,
          description: element.description,
          buttonDetails: false,
        })
      })

    });

  }

  clickSelectEvent(item: WorkEffortAnalysis) {
    this.currentAnalysis = item;

  }

  routingDetail(data) {

    this.dataStorageService.setData(this.router.url + `/${data.workEffortAnalysisId}`, 'desc', data.description);
    this.dataStorageService.setData(this.router.url + `/${data.workEffortAnalysisId}`, 'refDate', data.analysisRefDate);
    this.dataStorageService.removeData(this.router.url + `/${data.workEffortAnalysisId}`, 'weId');
    this.router.navigate([`${data.workEffortAnalysisId}`], { relativeTo: this.route });
  }

  collapseSidebarFun() {
    const dom: any = document.querySelector('body');
    const menu: any = document.querySelector('#sidebar');
    dom.classList.add('push-right');
    menu.classList.add('collapse');
  }

}
