import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WorkEffortAnalysisService } from 'app/api/service/work-effort-analysis.service';
import { I18NService } from 'app/i18n/i18n.service';
import { MenuItem } from 'primeng/api';
import { WorkEffortAnalysis } from 'app/api/model/workEffortAnalysis';
import { WorkEffortAnalysisTarget } from 'app/api/model/workEffortAnalysisTarget'
import { map, mergeMap, mergeWith } from 'rxjs/operators';
import { Subject, Subscription, combineLatest, lastValueFrom } from 'rxjs';
import { UomRangeValuesService } from 'app/api/service/uom-range-values.service';
import { gaugeConfig } from 'app/layout/gauge/gaugeConfig';
import { UntypedFormArray, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { ChartDropdown, ChartType, chartConfig } from 'app/layout/chart/chartConfig';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { emoticonCard } from 'app/layout/emoticon-card/emoticonCard';
import { LanguageService } from 'app/api/service/language.service';
import { radioButton } from 'app/layout/radio-button/radioButton';
import { ActionInput, ActionOutput, HeadArray } from 'app/layout/tables/table/table-configuration';
import { Score } from 'app/api/model/score';
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { addDaysFromGiulianDate, getDate, setDateUtc } from 'app/commons/utils/dateUtils';

interface Chart {
  name: string,
  code: string
}

@Component({
  selector: 'app-target',
  templateUrl: './target.component.html',
  styleUrls: ['./target.component.css'],
  encapsulation: ViewEncapsulation.None
})

export class TargetComponent implements OnInit {


  workEffort: boolean = false;
  showPdoScore: boolean = false;
  showDialogTitle: string;
  secondaryLang: boolean;

  precision: number;

  items: MenuItem[];
  tmpItems: MenuItem[];
  home: MenuItem;
  header: boolean;

  analyses: WorkEffortAnalysis[];
  analysis: WorkEffortAnalysis;
  context: string;
  analysisId: string;
  analysisDesc: string;


  comments: Map<string, string> = new Map;
  detailScore: string;
  mainScore: string;
  nameDetail: string;
  nameKPI: string;
  noKPI: string;

  _reload: Subject<void>;
  targets: WorkEffortAnalysisTarget;

  detailKPI$: Subscription;
  workEffortList$: Subscription;
  workEffort$: Subscription;
  getWorkEffortAnalysis$: Subscription;
  analyses$: Subscription;
  header$: Subscription;
  uomRangeValues$: Subscription;
  uomRangeValuesPathEmoticon$: Subscription;
  minValue$: Subscription;
  pdoScore$: Subscription;
  KPIscore$: Subscription;
  detailPdoScore$: Subscription;
  pdoAccountWEMI$: Subscription;
  pdoAccountOrgUnit$: Subscription;
  pdoAccount$: Subscription;
  pdoScorekpi$: Subscription;

  maxValuePromise: Promise<number>;
  maxValue: number;

  /* gaugeConfig1..4 header gauge configuration variables */
  gaugeConfig1: gaugeConfig = { gaugeLabel: "", gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null };
  gaugeConfig2: gaugeConfig = { gaugeLabel: "", gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null };
  gaugeConfig3: gaugeConfig = { gaugeLabel: "", gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null };
  gaugeConfig4: gaugeConfig = { gaugeLabel: "", gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null };


  tmpGaugeConfig: gaugeConfig[] = [
    { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch1 header
    { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch2 header
    { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch3 header
    { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch4 header
    { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch1 table
    { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch2 table
    { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch3 table
    { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch4 table
  ];


  srcImg1: string;
  srcImg2: string;
  srcImg3: string;
  srcImg4: string;
  srcImgSC: string;
  srcImgST: string;
  srcImgSC2: string;
  srcImgST2: string;

  scoreEtch1: string;
  scoreEtch2: string;
  scoreEtch3: string;
  scoreEtch4: string;
  etchScore1: string;
  etchScore2: string;
  etchScore3: string;
  etchScore4: string;

  otherAnalysisEtch1: string;
  otherAnalysisEtch2: string;
  otherAnalysisEtch3: string;
  otherAnalysisEtch4: string;

  showEtchDescr: string;

  value1: number;
  value2: number;
  value3: number;
  value4: number;

  form: { [name: string]: UntypedFormGroup | UntypedFormControl | UntypedFormArray };
  formKPI: { [name: string]: UntypedFormGroup | UntypedFormControl | UntypedFormArray };

  gridArray: any[] = [];
  tmpGridArray: any[] = [];
  loadingWorkEffort: boolean = true;
  gridArrayKPI: any[] = [];
  tmpGridArrayKPI: any[] = [];
  itemKPI: any;
  loadingKPI: boolean = true;
  loadingPdoScore: boolean = true;

  chartItems: Chart[];
  selectedChart: Chart;

  chartItemsDialog: Chart[];
  selectedChartItemPdoScore: Chart;
  buttonSlideMenuKPI: MenuItem[] = [];

  radioButtonChart: radioButton = { selectedItem: null, items: null };

  disButtSliMenu: boolean = true;

  emoticonCardConftmp1: emoticonCard = { text: null, value: null, iconContentId: null };
  emoticonCardConftmp2: emoticonCard = { text: null, value: null, iconContentId: null };
  emoticonCardConftmp3: emoticonCard = { text: null, value: null, iconContentId: null };
  emoticonCardConftmp4: emoticonCard = { text: null, value: null, iconContentId: null };

  emoticonCardConf1: emoticonCard = { text: null, value: null, iconContentId: null };
  emoticonCardConf2: emoticonCard = { text: null, value: null, iconContentId: null };
  emoticonCardConf3: emoticonCard = { text: null, value: null, iconContentId: null };
  emoticonCardConf4: emoticonCard = { text: null, value: null, iconContentId: null };


  /* ####### config for table component ########## */

  headArray: HeadArray[] = [
    { head: '', fieldName: '', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", sortIcon: true },
    { head: '', fieldName: '', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", sortIcon: true },
    { head: '', fieldName: 'idNumber', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", gaugeConfig: null, sortIcon: false },
  ];
  headArrayKPI: HeadArray[] = [
    { head: '', fieldName: '', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", linkImg: null, value: null, sortIcon: false },
    { head: '', fieldName: '', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", sortIcon: false, align: "" },
    { head: '', fieldName: 'idNumber', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", gaugeConfig: null },
    { head: this.i18nService.translate('Indicator'), fieldName: 'indicator', actionInput: 'null', actionOutput: 'outputLabelData', display: "table-cell", filter: "textFilter", sortIcon: true },

  ];

  itemsButtonSlideMenu: MenuItem[] = [];

  tmpChartConfig: chartConfig = { type: null, etch: [], labels: [], rangeMaxName: null, dataMax: [], dataCon: [], dataSC: [], dataST: [], dataSC2: [], dataST2: [], scoreEtch1: null, scoreEtch2: null, scoreEtch3: null, scoreEtch4: null, showEtchDescr: null };
  chartConfig: chartConfig = { type: null, etch: [], labels: [], rangeMaxName: null, dataMax: [], dataCon: [], dataSC: [], dataST: [], dataSC2: [], dataST2: [], scoreEtch1: null, scoreEtch2: null, scoreEtch3: null, scoreEtch4: null, showEtchDescr: null };
  chartConfigPdoScore: chartConfig;
  analysisRefDate;


  //Show Detail Score
  showKPIscore: boolean = false;
  loadingKPIscore: boolean = true;
  chartConfigKPIscore: chartConfig;
  selectedChartItemKPIscore: ChartDropdown;

  //Show Detail Pdo Score
  showDetailPdoScore: boolean = false;
  loadingDetailPdoScore: boolean = true;
  chartConfigDetailPdoScore: chartConfig;
  selectedChartItemDetailPdoScore: ChartDropdown;

  //Show Pdo Account
  showPdoAccount: boolean[] = [];
  loadingPdoAccount: boolean[] = [];
  chartConfigPdoAccount: chartConfig[];
  selectedItemPdoAccount: ChartDropdown[];

  //show Pdo Score KPI
  showPdoScorekpi: boolean[] = [];
  loadingPdoScorekpi: boolean[] = [];
  chartConfigPdoScorekpi: chartConfig[];
  selectedChartItemPdoScorekpi: ChartDropdown[];

  weId: string;

  constructor(
    private readonly workEffortAnalysisService: WorkEffortAnalysisService,
    private readonly uomRangeValuesService: UomRangeValuesService,
    private readonly glAccountService: GlAccountService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly dataStorageService: DataStorageService
  ) {
    this._reload = new Subject<void>();
    this.chartItemsDialog = [
      { name: 'RADAR', code: 'radar' },
      { name: this.i18nService.translate('BAR'), code: 'bar' },
      { name: this.i18nService.translate('LINE'), code: 'line' },


    ];
    this.chartItems = [
      { name: 'RADAR', code: 'radar' },
      { name: this.i18nService.translate('BAR'), code: 'bar' },
      { name: this.i18nService.translate('LINE'), code: 'line' },


    ];
    this.selectedChart = { name: 'RADAR', code: 'radar' };

    this.radioButtonChart.items = [
      { name: this.i18nService.translate('Label'), key: 'E' },
      { name: this.i18nService.translate('Description'), key: 'D' },
      { name: this.i18nService.translate('All'), key: 'A' },


    ];
    this.radioButtonChart.selectedItem = { name: this.i18nService.translate('Label'), key: 'E' };

    this.noKPI = this.i18nService.translate("There are no indicators to display");

  }

  ngOnInit() {
    this.form = {};
    this.formKPI = {};

    this.header = false;

    this.context = this.route.parent.snapshot.data.context;

    this.route.paramMap.subscribe(paramMap => {
      //this.context = paramMap.get('context');
      this.analysisId = paramMap.get('analysisId');

      this.analyses = [];
    });

    this.weId = this.dataStorageService.getData(this.router.url, 'weId');

    this.home = { icon: 'pi pi-list', routerLink: '/c/' + this.context + '/consultation/analysis/' };

    //let url: string = (!!this.weId) ? location.href.substring(location.href.indexOf('/c/'), location.href.indexOf(';weId')) : location.href;

    let desc = this.dataStorageService.getData(this.router.url, 'desc');
    this.analysisRefDate = new Date(this.dataStorageService.getData(this.router.url, 'refDate'));

    if (!!!desc || !!!this.analysisRefDate) {
      this.getWorkEffortAnalysis$ = this.workEffortAnalysisService.getWorkEffortAnalysis(this.analysisId).subscribe(x => {
        desc = x.description;
        this.tmpItems = [{ label: `${desc}`, routerLink: '/c/' + this.context + '/consultation/analysis/' + this.analysisId }];

        this.items = [{ label: `${desc}`, routerLink: '/c/' + this.context + '/consultation/analysis/' + this.analysisId }];

        this.analysisRefDate = x.referenceDate;

      })
    }
    else {
      this.tmpItems = [{ label: `${desc}`, routerLink: '/c/' + this.context + '/consultation/analysis/' + this.analysisId }];

      this.items = [{ label: `${desc}`, routerLink: '/c/' + this.context + '/consultation/analysis/' + this.analysisId }];
    }

    if (!!this.weId) {
      let dataId = { id: this.weId }
      this.routingDetail(dataId);
      return;

    }
    const reloadedAnalyses = this._reload.pipe(mergeMap(() => this.workEffortAnalysisService.getWorkEffortAnalysisTargetSummary(this.context, this.analysisId)));

    this.analyses$ = this.route.data.pipe(
      map((data: { analyses: any[] }) => data.analyses),
      mergeWith(reloadedAnalyses)
    )
      .subscribe((data) => {
        this.analyses = data;

        //if the request return 0 element
        if (data.length == 0) {
          this.header = true;
          this.analysisDesc = this.i18nService.translate("There is no data to display");
        }

        this.glAccountService.getPrecisionDecimal("SCORE").then(async pcs => {
          this.precision = pcs;



          //if the request return 1 element
          if (data.length == 1) {
            this.header = true;
            this.analysis = (<any>this.analyses[0]).workEffort;

            if (!!this.analyses[0].comments) {
              this.secondaryLang = await this.languageService.secondaryLang();

              this.comments = this.getComments(this.analyses[0].comments);

              this.header$ = this.workEffortAnalysisService.getWorkEffortAnalysisTargetHeaderOne(this.analysisId, this.analysis.workEffortId, this.comments.get('rangeDefault'))
                .subscribe((element) => {
                  this.targets = <WorkEffortAnalysisTarget>element[0];

                  this.analysisDesc = this.targets.workEffortName;

                  let dataId = { id: this.targets.workEffortId }

                  this.setValueHeader(this.targets);

                  if (this.comments.get('mainScore') == 'EMOTICON' || this.comments.get('mainScore') == 'EMOTONLY') {

                    this.mainScore = "EMOTICON";
                    this.setSrcImgHeader(this.targets, this.comments.get('mainScore'));

                    /*PER ANDARE IN UN WE SPECIFICATO */
                    if (!!this.weId) {
                      let dataId = { id: this.weId }
                      this.routingDetail(dataId);
                      return;

                    }
                    this.routingDetail(dataId);
                  }

                  if (this.comments.get('mainScore') == 'GAUGE') {

                    this.mainScore = "GAUGE";
                    this.uomRangeValues$ = this.uomRangeValuesService.uomRangeValues(this.comments.get('rangeDefault')).subscribe(data => {
                      data.forEach(e => {

                        if (e.colorEnumId == "GREEN") this.tmpGaugeConfig.forEach(conf => conf.fromValueGreen = e.fromValue);
                        if (e.colorEnumId == "YELLOW") this.tmpGaugeConfig.forEach(conf => conf.fromValueYellow = e.fromValue);
                        if (e.colorEnumId == "RED") this.tmpGaugeConfig.forEach(conf => conf.fromValueRed = e.fromValue);

                      });

                      this.maxValuePromise = this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault'));

                      this.maxValuePromise.then(
                        response => {
                          this.maxValue = response
                          this.tmpGaugeConfig.forEach(conf => conf.gaugeMax = response);

                          this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
                            .subscribe(min => {

                              this.tmpGaugeConfig.forEach(conf => conf.gaugeMin = min);

                              this.tmpGaugeConfig[0].gaugeValue = this.value1;
                              this.gaugeConfig1 = this.tmpGaugeConfig[0];

                              this.tmpGaugeConfig[1].gaugeValue = this.value2;
                              this.gaugeConfig2 = this.tmpGaugeConfig[1];

                              this.tmpGaugeConfig[2].gaugeValue = this.value3;
                              this.gaugeConfig3 = this.tmpGaugeConfig[2];

                              this.tmpGaugeConfig[3].gaugeValue = this.value4;
                              this.gaugeConfig4 = this.tmpGaugeConfig[3];

                              /*PER ANDARE IN UN WE SPECIFICATO */
                              if (!!this.weId) {
                                let dataId = { id: this.weId }
                                this.routingDetail(dataId);
                                return;

                              }
                              this.routingDetail(dataId);

                            })

                        })
                    })
                  }

                  if (this.comments.get('mainScore') == 'GAUGEFLEX') {
                    this.mainScore = "GAUGEFLEX";
                    let tmpGauge: gaugeConfig = { fromValueRed: 0, fromValueYellow: this.value2, fromValueGreen: this.value3 };

                    this.maxValuePromise = this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault'));

                    this.maxValuePromise.then(
                      response => {
                        this.maxValue = response
                        tmpGauge.gaugeMax = response;

                        this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
                          .subscribe(min => {
                            tmpGauge.gaugeMin = min;
                            tmpGauge.gaugeValue = this.value1;
                            this.gaugeConfig1 = tmpGauge;

                            /*PER ANDARE IN UN WE SPECIFICATO */
                            if (!!this.weId) {
                              let dataId = { id: this.weId }
                              this.routingDetail(dataId);
                              return;

                            }
                            this.routingDetail(dataId);

                          })

                      })
                  }

                  if (!!this.comments.get('detailKPI')) this.setDetailKPI(dataId.id);
                })

            }
            else this.analysisDesc = this.i18nService.translate("Parameterization missing");


          }
          if (data.length > 1) {

            this.header = true;
            this.analysis = (<any>this.analyses[0]).workEffort;

            if (!!this.analyses[0].comments) {
              this.secondaryLang = await this.languageService.secondaryLang();
              this.comments = this.getComments(this.analyses[0].comments);
              this.nameDetail = (!this.secondaryLang) ? this.comments.get('nameDetail') : this.comments.get('nameDetailLang'); //first folder name
              //this.nameKPI = (!this.secondaryLang) ? this.comments.get('nameKPI') : this.comments.get('nameKPILang');       //second folder name
              await this.setScoreEtch(); //set the scoreEtch1..4 and scoreEtchLang1..4

              this.setButtonSlideMenu(this.analysisId);

              this.header$ = this.workEffortAnalysisService.getWorkEffortAnalysisTargetHeaderMore(this.context, this.analysisId)
                .subscribe(element => {

                  this.targets = <WorkEffortAnalysisTarget>element[0];

                  this.analysisDesc = this.analysisId;

                  this.setValueHeader(this.targets);


                  if (this.comments.get('mainScore') == 'EMOTICON' || this.comments.get('mainScore') == 'EMOTONLY') {
                    this.mainScore = "EMOTICON";
                    let index;

                    if (!!this.value1) {
                      this.uomRangeValuesPathEmoticon$ = this.uomRangeValuesService.uomRangeValuesPathEmoticon(this.comments.get('rangeDefault'), this.value1).subscribe(v => {
                        if (!!v[0].dataResource.objectInfo) {
                          index = v[0].dataResource.objectInfo.indexOf("resources")
                          this.srcImg1 = v[0].dataResource.objectInfo.substring(index - 1);

                          this.emoticonCardConftmp1.iconContentId = v[0].iconContentId;
                          this.emoticonCardConftmp1.text = this.scoreEtch1;
                          if (this.comments.get('mainScore') == "EMOTONLY") this.emoticonCardConftmp1.value = null;
                          this.emoticonCardConf1 = this.emoticonCardConftmp1;

                        }

                      })
                    }
                    if (!!this.value2) {
                      this.uomRangeValuesPathEmoticon$ = this.uomRangeValuesService.uomRangeValuesPathEmoticon(this.comments.get('rangeDefault'), this.value2).subscribe(v => {

                        if (!!v[0].dataResource.objectInfo) {
                          index = v[0].dataResource.objectInfo.indexOf("resources")
                          this.srcImg2 = v[0].dataResource.objectInfo.substring(index - 1);

                          this.emoticonCardConftmp2.iconContentId = v[0].iconContentId;
                          this.emoticonCardConftmp2.text = this.scoreEtch2;
                          if (this.comments.get('mainScore') == "EMOTONLY") this.emoticonCardConftmp2.value = null;
                          this.emoticonCardConf2 = this.emoticonCardConftmp2;
                        }

                      })
                    }
                    if (!!this.value3) {
                      this.uomRangeValuesPathEmoticon$ = this.uomRangeValuesService.uomRangeValuesPathEmoticon(this.comments.get('rangeDefault'), this.value3).subscribe(v => {

                        if (!!v[0].dataResource.objectInfo) {
                          index = v[0].dataResource.objectInfo.indexOf("resources")
                          this.srcImg3 = v[0].dataResource.objectInfo.substring(index - 1);

                          this.emoticonCardConftmp3.iconContentId = v[0].iconContentId;
                          this.emoticonCardConftmp3.text = this.scoreEtch3;
                          if (this.comments.get('mainScore') == "EMOTONLY") this.emoticonCardConftmp3.value = null;
                          this.emoticonCardConf3 = this.emoticonCardConftmp3;
                        }

                      })
                    }
                    if (!!this.value4) {
                      this.uomRangeValuesPathEmoticon$ = this.uomRangeValuesService.uomRangeValuesPathEmoticon(this.comments.get('rangeDefault'), this.value4).subscribe(v => {

                        if (!!v[0].dataResource.objectInfo) {
                          index = v[0].dataResource.objectInfo.indexOf("resources")
                          this.srcImg4 = v[0].dataResource.objectInfo.substring(index - 1);

                          this.emoticonCardConftmp4.iconContentId = v[0].iconContentId;
                          this.emoticonCardConftmp4.text = this.scoreEtch4;
                          if (this.comments.get('mainScore') == "EMOTONLY") this.emoticonCardConftmp4.value = null;
                          this.emoticonCardConf4 = this.emoticonCardConftmp4;
                        }

                      })
                    }
                    /*PER ANDARE IN UN WE SPECIFICATO */
                    if (!!this.weId) {
                      let dataId = { id: this.weId }
                      this.routingDetail(dataId);
                      return;

                    }
                    this.configDetailScore();

                  }


                  if (this.comments.get('mainScore') == 'GAUGE') {

                    this.mainScore = "GAUGE";
                    this.uomRangeValues$ = this.uomRangeValuesService.uomRangeValues(this.comments.get('rangeDefault')).subscribe(data => {
                      data.forEach(e => {
                        if (e.colorEnumId == "GREEN") this.tmpGaugeConfig.forEach(conf => conf.fromValueGreen = e.fromValue);
                        if (e.colorEnumId == "YELLOW") this.tmpGaugeConfig.forEach(conf => conf.fromValueYellow = e.fromValue);
                        if (e.colorEnumId == "RED") this.tmpGaugeConfig.forEach(conf => conf.fromValueRed = e.fromValue);

                      });

                      this.maxValuePromise = this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault'));

                      this.maxValuePromise.then(
                        response => {
                          this.maxValue = response
                          this.tmpGaugeConfig.forEach(conf => conf.gaugeMax = response);

                          this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
                            .subscribe(min => {

                              this.tmpGaugeConfig.forEach(conf => conf.gaugeMin = min);

                              this.tmpGaugeConfig[0].gaugeValue = this.value1;
                              this.gaugeConfig1 = this.tmpGaugeConfig[0];

                              this.tmpGaugeConfig[1].gaugeValue = this.value2;
                              this.gaugeConfig2 = this.tmpGaugeConfig[1];

                              this.tmpGaugeConfig[2].gaugeValue = this.value3;
                              this.gaugeConfig3 = this.tmpGaugeConfig[2];

                              this.tmpGaugeConfig[3].gaugeValue = this.value4;
                              this.gaugeConfig4 = this.tmpGaugeConfig[3];

                              /*PER ANDARE IN UN WE SPECIFICATO */
                              if (!!this.weId) {
                                let dataId = { id: this.weId }
                                this.routingDetail(dataId);
                                return;

                              }
                              this.configDetailScore();

                            })

                        })
                    })
                  }

                  if (this.comments.get('mainScore') == 'GAUGEFLEX') {
                    this.mainScore = "GAUGEFLEX";
                    let tmpGauge: gaugeConfig = { fromValueRed: 0, fromValueYellow: this.value2, fromValueGreen: this.value3 };

                    this.maxValuePromise = this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault'));

                    this.maxValuePromise.then(
                      response => {
                        this.maxValue = response
                        tmpGauge.gaugeMax = response;

                        this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
                          .subscribe(min => {
                            tmpGauge.gaugeMin = min;
                            tmpGauge.gaugeValue = this.value1;
                            this.gaugeConfig1 = tmpGauge;

                            /*PER ANDARE IN UN WE SPECIFICATO */
                            if (!!this.weId) {
                              let dataId = { id: this.weId }
                              this.routingDetail(dataId);
                              return;

                            }
                            this.configDetailScore();

                          })

                      })
                  }

                })

              //if(!!this.comments.get('detailKPI')) this.setDetailKPI();
            } else this.analysisDesc = this.i18nService.translate("Parameterization missing");
          }

        });
      })

  }

  /**
   * This function sets the detail score and calls the configData() function.
   */
  configDetailScore() {

    if (this.comments.get('detailScore') == "EMOTICON_LIST" || this.comments.get('detailScore') == "EMOTONLY_LIST") {
      this.detailScore = this.comments.get('detailScore');
      this.headArrayEmoticonList();
      this.configData();
    }

    if (this.comments.get('detailScore') == "GAUGE_LIST") {
      this.detailScore = "GAUGE_LIST";


      if (this.comments.get('mainScore') != "GAUGE") {

        this.uomRangeValues$ = this.uomRangeValuesService.uomRangeValues(this.comments.get('rangeDefault')).subscribe(data => {
          data.forEach(e => {

            if (e.colorEnumId == "GREEN") this.tmpGaugeConfig.forEach(conf => conf.fromValueGreen = e.fromValue);
            if (e.colorEnumId == "YELLOW") this.tmpGaugeConfig.forEach(conf => conf.fromValueYellow = e.fromValue);
            if (e.colorEnumId == "RED") this.tmpGaugeConfig.forEach(conf => conf.fromValueRed = e.fromValue);

          });

          this.maxValuePromise = this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault'));

          this.maxValuePromise.then(
            response => {
              this.maxValue = response;
              this.tmpGaugeConfig.forEach(conf => conf.gaugeMax = response);

              this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
                .subscribe(min => {

                  this.tmpGaugeConfig.forEach(conf => conf.gaugeMin = min);
                  this.headArrayGaugeList();
                  this.configData();
                })


            })


        })
      } else {
        this.headArrayGaugeList();
        this.configData();
      }

    }

    if (this.comments.get('detailScore') == "GAUGEFLEX_LIST") {
      this.detailScore = "GAUGEFLEX_LIST";

      this.maxValuePromise = this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault'));

      this.maxValuePromise.then(
        response => {
          this.maxValue = response;
          this.tmpGaugeConfig.forEach(conf => conf.gaugeMax = response);

          this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
            .subscribe(min => {

              this.tmpGaugeConfig.forEach(conf => conf.gaugeMin = min);
              this.headArrayGaugeFlexList();
              this.configData();
            })


        })

    }

    if (this.comments.get('detailScore') == "RADAR") {
      this.detailScore = "CHART";
      this.selectedChart = { name: 'RADAR', code: 'radar' };
      this.tmpChartConfig.type = 'radar';
      this.headArrayConfChart();
      this.configData();
    }

    if (this.comments.get('detailScore') == "LINE") {
      this.detailScore = "CHART";
      this.selectedChart = { name: this.i18nService.translate('LINE'), code: 'line' };
      this.tmpChartConfig.type = 'line';
      this.headArrayConfChart();
      this.configData();
    }

    if (this.comments.get('detailScore') == "BAR") {
      this.detailScore = "CHART";
      this.selectedChart = { name: this.i18nService.translate('BAR'), code: 'bar' };
      this.tmpChartConfig.type = 'bar';
      this.headArrayConfChart();
      this.configData();
    }

    if (this.comments.get('detailScore') == "PIE") {
      this.detailScore = "CHART";
      this.selectedChart = { name: this.i18nService.translate('PIE'), code: 'pie' };
      this.tmpChartConfig.type = 'pie';
      this.headArrayConfChart();
      this.configData();
    }

    if (this.comments.get('detailScore') == "POLAR") {
      this.detailScore = "CHART";
      this.selectedChart = { name: this.i18nService.translate('POLAR'), code: 'polarArea' };
      this.tmpChartConfig.type = 'polarArea';
      this.headArrayConfChart();
      this.configData();
    }

    if (this.comments.get('detailScore') == "DOUGHNUT") {
      this.detailScore = "CHART";
      this.selectedChart = { name: this.i18nService.translate('DOUGHNUT'), code: 'doughnut' };
      this.tmpChartConfig.type = 'doughnut';
      this.headArrayConfChart();
      this.configData();
    }


  }



  /**
   * This function sets the gridArray for the table 
   * and defines the information for the detail score. 
   */
  async configData() {



    if (this.detailScore == "CHART") {
      this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
        .subscribe(min => {

          this.tmpChartConfig.dataMin = min;
        });

      await this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault')).then(response => {
        this.maxValue = response;

      });


      if (!!this.comments.get("showEtchDescr")) {
        this.tmpChartConfig.showEtchDescr = this.comments.get("showEtchDescr");
        this.radioButtonChart.selectedItem = this.radioButtonChart.items.filter(x => x.key === this.comments.get("showEtchDescr"))[0];

      } else {
        this.tmpChartConfig.showEtchDescr = 'E';
        this.radioButtonChart.selectedItem = this.radioButtonChart.items.filter(x => x.key === 'E')[0];

      }
    }

    this.workEffort$ = this.workEffortAnalysisService.getWorkEffortAnalysisTargetList(this.context, this.analysisId, (!!this.comments.get('dateControl')) ? this.comments.get('dateControl') : "NONE", this.comments.get('rangeDefault'), (this.comments.get('showOrgUnit')) ? this.comments.get('showOrgUnit') : "N")
      .subscribe((data) => {

        if (this.detailScore == "CHART" && data.length < 3) {
          this.selectedChart = { name: this.i18nService.translate('BAR'), code: 'bar' };
          this.tmpChartConfig.type = 'bar';
        }

        data.forEach(async (element, index) => {

          if (this.comments.get('detailScore') == "EMOTICON_LIST" || this.comments.get('detailScore') == "EMOTONLY_LIST") this.setSrcImgGridArray(element);

          let valSC = null, valST = null, valSC2 = null, valST2 = null;
          if (this.comments.get('detailScore') != "EMOTONLY_LIST") {
            if (!!element.scAmount || element.scAmount == 0) {
              valSC = + element.scAmount;
              valSC = + valSC.toFixed(this.precision);
            }

            if (!!element.stAmount || element.stAmount == 0) {
              valST = + element.stAmount;
              valST = + valST.toFixed(this.precision);
            }

            if (!!element.sc2Amount || element.sc2Amount == 0) {
              valSC2 = + element.sc2Amount;
              valSC2 = + valSC2.toFixed(this.precision);
            }

            if (!!element.st2Amount || element.st2Amount == 0) {
              valST2 = + element.st2Amount;
              valST2 = + valST2.toFixed(this.precision);
            }

          }

          let workEffortName, partyName, etch, label;
          if (this.comments.get('showOrgUnit') == 'Y') {
            partyName = (!this.secondaryLang) ? element.partyName : element.partyNameLang;
            etch = element.parentRoleCode;
            label = (!!etch) ? etch + " - " + partyName : partyName;
          }
          else if (this.comments.get('showOrgUnit') == 'A') {
            workEffortName = (!this.secondaryLang) ? element.workEffortName : element.workEffortNameLang;
            etch = element.workEffortEtch;
            let labelTmp = (!!etch) ? etch + " - " + workEffortName : workEffortName;
            partyName = (!this.secondaryLang) ? element.partyName : element.partyNameLang;
            etch = element.parentRoleCode;

            label = labelTmp + " (" + ((!!etch) ? etch + " - " + partyName : partyName) + ")"
          }
          else {
            workEffortName = (!this.secondaryLang) ? element.workEffortName : element.workEffortNameLang;
            etch = element.workEffortEtch;
            label = (!!etch) ? etch + " - " + workEffortName : workEffortName;
          }

          this.tmpGridArray.push({
            id: element.workEffortId,
            workEffortName: label,
            scoreEtch1: { amount: valSC, linkImg: this.srcImgSC, gaugeConfig: { fromValueRed: this.tmpGaugeConfig[4].gaugeMin, fromValueYellow: valST, fromValueGreen: valSC2, gaugeMax: Math.max(this.tmpGaugeConfig[4].gaugeMax, valSC2), gaugeMin: this.tmpGaugeConfig[4].gaugeMin } },
            scoreEtch2: { amount: valST, linkImg: this.srcImgST },
            scoreEtch3: { amount: valSC2, linkImg: this.srcImgSC2 },
            scoreEtch4: { amount: valST2, linkImg: this.srcImgST2 },
            buttonDetails: true,
          })

          if (this.detailScore == "CHART") { await this.configChart(element, this.maxValue); }


        });

        this.gridArray = this.tmpGridArray;
        this.loadingWorkEffort = false;

      })

  }

  /**
   * This function get comments from a string.
   * 
   * @param str - String
   * @returns Map<string, string>
   */
  getComments(str): Map<string, string> {

    const map = new Map();

    str.replaceAll(/\n\s/g, "").replaceAll("\"", "").split(";").map(item => { map.set(item.trim().split("=")[0], item.trim().split("=")[1]) });

    return map;
  }

  /**
   * This function set the scoreEtch1..4 with scoreEtch1..4 or scoreEtchLang1..4 in comments.
   * Finally set the available graphs.
   */
  async setScoreEtch() {

    if (!this.secondaryLang) {

      if (!!this.comments.get('scoreEtch1')) this.scoreEtch1 = this.comments.get('scoreEtch1');
      if (!!this.comments.get('scoreEtch2')) this.scoreEtch2 = this.comments.get('scoreEtch2');
      if (!!this.comments.get('scoreEtch3')) this.scoreEtch3 = this.comments.get('scoreEtch3');
      if (!!this.comments.get('scoreEtch4')) this.scoreEtch4 = this.comments.get('scoreEtch4');

    }
    else {

      if (!!this.comments.get('scoreEtchLang1')) this.scoreEtch1 = this.comments.get('scoreEtchLang1');
      if (!!this.comments.get('scoreEtchLang2')) this.scoreEtch2 = this.comments.get('scoreEtchLang2');
      if (!!this.comments.get('scoreEtchLang3')) this.scoreEtch3 = this.comments.get('scoreEtchLang3');
      if (!!this.comments.get('scoreEtchLang4')) this.scoreEtch4 = this.comments.get('scoreEtchLang4');

    }


    if (!this.myXOR(!!this.scoreEtch1, !!this.scoreEtch2, !!this.scoreEtch3, !!this.scoreEtch4)) {

      this.chartItems.push(
        { name: this.i18nService.translate('PIE'), code: 'pie' },
        { name: this.i18nService.translate('POLAR'), code: 'polarArea' },
        { name: this.i18nService.translate('DOUGHNUT'), code: 'doughnut' },
      );

    }

  }

  async setEtchScore() {
    if (!this.secondaryLang) {
      if (!!this.comments.get('etchScore1')) this.etchScore1 = this.comments.get('etchScore1');
      if (!!this.comments.get('etchScore2')) this.etchScore2 = this.comments.get('etchScore2');
      if (!!this.comments.get('etchScore3')) this.etchScore3 = this.comments.get('etchScore3');
      if (!!this.comments.get('etchScore4')) this.etchScore4 = this.comments.get('etchScore4');

    }
    else {
      if (!!this.comments.get('etchScoreLang1')) this.etchScore1 = this.comments.get('etchScoreLang1');
      if (!!this.comments.get('etchScoreLang2')) this.etchScore2 = this.comments.get('etchScoreLang2');
      if (!!this.comments.get('etchScoreLang3')) this.etchScore3 = this.comments.get('etchScoreLang3');
      if (!!this.comments.get('etchScoreLang4')) this.etchScore4 = this.comments.get('etchScoreLang4');

    }

  }

  /**
   * This function perform the logical xor operation.
   * 
   * @param values - values
   * @returns - The result of the operation.
   */
  myXOR(...values) {

    let result = values[0];

    for (let i = 1; i < values.length; i++) {
      result = result ^ values[i];
    }

    return result;
  }

  /**
   * Sets the button slide menu in the header.
   * 
   * @param id
   */
  setButtonSlideMenu(id) {
    this.itemsButtonSlideMenu = [];
    /*Collegamento all'interrogazione di una scheda*/
    /*this.itemsButtonSlideMenu.push({
      label: 'Scheda',
      icon: 'pi pi-angle-right',
      command: () => this.openRecordCard(id)
    });*/

    if (!!this.comments.get('otherAnalysisEtch1') && !!this.comments.get('otherWorkEffortTypeId1') && this.workEffort) {

      this.itemsButtonSlideMenu.push({
        label: this.i18nService.translate(this.comments.get('otherAnalysisEtch1')),
        icon: 'pi pi-angle-right',
        command: () => this.toWorkEffort(this.comments.get('otherWorkEffortTypeId1'), id)

      })
      this.otherAnalysisEtch1 = this.i18nService.translate(this.comments.get('otherAnalysisEtch1'));
    }

    if (!!this.comments.get('otherAnalysisEtch2') && !!this.comments.get('otherWorkEffortTypeId2') && this.workEffort) {

      this.itemsButtonSlideMenu.push({
        label: this.i18nService.translate(this.comments.get('otherAnalysisEtch2')),
        icon: 'pi pi-angle-right',
        command: () => this.toWorkEffort(this.comments.get('otherWorkEffortTypeId2'), id)

      })
      this.otherAnalysisEtch2 = this.i18nService.translate(this.comments.get('otherAnalysisEtch2'));
    }
    if (!!this.comments.get('otherAnalysisEtch3') && !!this.comments.get('otherWorkEffortTypeId3') && this.workEffort) {
      this.itemsButtonSlideMenu.push({
        label: this.i18nService.translate(this.comments.get('otherAnalysisEtch3')),
        icon: 'pi pi-angle-right',
        command: () => this.toWorkEffort(this.comments.get('otherWorkEffortTypeId3'), id)

      })
      this.otherAnalysisEtch3 = this.i18nService.translate(this.comments.get('otherAnalysisEtch3'));
    }
    if (!!this.comments.get('otherAnalysisEtch4') && !!this.comments.get('otherWorkEffortTypeId4') && this.workEffort) {
      this.itemsButtonSlideMenu.push({
        label: this.i18nService.translate(this.comments.get('otherAnalysisEtch4')),
        icon: 'pi pi-angle-right',
        command: () => this.toWorkEffort(this.comments.get('otherWorkEffortTypeId4'), id)

      })
      this.otherAnalysisEtch4 = this.i18nService.translate(this.comments.get('otherAnalysisEtch4'));
    }

    if (!!this.comments.get('pdoScore') && this.workEffort) {
      this.itemsButtonSlideMenu.push({
        label: this.i18nService.translate('Historical score trend'),
        icon: 'pi pi-angle-right',
        command: () => {
          this.showDialogTitle = this.i18nService.translate('Historical score trend');
          this.pdoScoreDialog(this.comments.get('pdoScore'), id)
        }

      })
    }

    if (!!this.comments.get('KPIscore') && this.workEffort) {
      this.itemsButtonSlideMenu.push({
        label: this.i18nService.translate('Trend score detail'),
        icon: 'pi pi-angle-right',
        command: () => {
          this.showDialogTitle = this.i18nService.translate('Trend score detail');
          this.KPIscoreDialog(this.comments.get('KPIscore'), id)
        }

      })
    }

    if (!!this.comments.get('detailPdoScore') && !!this.comments.get('detailPdoScoreFiscal') && this.workEffort) {
      this.itemsButtonSlideMenu.push({
        label: this.i18nService.translate('Historical detail score'),
        icon: 'pi pi-angle-right',
        command: () => {
          this.showDialogTitle = this.i18nService.translate('Historical detail score');
          this.detailPdoScoreDialog(this.comments.get('detailPdoScore'), id, this.comments.get('detailPdoScoreFiscal'))
        }

      })
    }

    if (this.itemsButtonSlideMenu.length != 0) this.disButtSliMenu = false;

  }

  /**
   * 
   * @param id 
   */
  openRecordCard(id) {
    console.log(id);

  }


  async toWorkEffort(workEffortTypeId, currentWorkEffortId) {
    let wea = new WorkEffortAnalysis();
    wea.referenceDate = this.analysisRefDate? new Date(this.analysisRefDate) : null;
    wea.workEffortTypeId = workEffortTypeId;

    if (wea.workEffortTypeId != null && wea.referenceDate != null) {

      const obs = this.workEffortAnalysisService.getWEAWithRefDateAndWorkEffortTypeId(wea);

      obs.then(x => {

        let url = `/c/${this.context}/consultation/analysis/${x.workEffortAnalysisId}`;
        this.dataStorageService.setData(url, 'desc', x.description);
        this.dataStorageService.setData(url, 'refDate', x.referenceDate? new Date(x.referenceDate).toDateString() : null);
        this.dataStorageService.setData(url, 'weId', currentWorkEffortId);
        this.resetVariable();

        //this.router.navigate([url]);

        this.router.navigateByUrl(`/`, { skipLocationChange: true }).then(() =>
          this.router.navigateByUrl(url, { replaceUrl: true }));
        const dom: any = document.querySelector('body');
        dom.classList.remove('push-right');

      })
    }
  }


  /**
   * This function set the values to be displayed in the header.
   * 
   * @param target - Target.
   */
  setValueHeader(target: WorkEffortAnalysisTarget) {
    if (!!target.scAmount || target.scAmount == 0) {
      this.value1 = + target.scAmount;
      this.value1 = + this.value1.toFixed(this.precision);
      this.emoticonCardConftmp1.value = this.value1;
    }
    else this.value1 = null;

    if (!!target.stAmount || target.stAmount == 0) {
      this.value2 = + target.stAmount;
      this.value2 = + this.value2.toFixed(this.precision);
      this.emoticonCardConftmp2.value = this.value2;
    }
    else this.value2 = null;

    if (!!target.sc2Amount || target.sc2Amount == 0) {
      this.value3 = + target.sc2Amount;
      this.value3 = + this.value3.toFixed(this.precision);
      this.emoticonCardConftmp3.value = this.value3;
    }
    else this.value3 = null;

    if (!!target.st2Amount || target.st2Amount == 0) {
      this.value4 = + target.st2Amount;
      this.value4 = + this.value4.toFixed(this.precision);
      this.emoticonCardConftmp4.value = this.value4;
    }
    else this.value4 = null
  }


  /**
   * This function configures the variable chartConfig to set the chart.
   * 
   * @param element - The target to add.
   */
  async configChart(element: WorkEffortAnalysisTarget, max) {

    let workEffortName, partyName, etch, label;
    if (this.comments.get('showOrgUnit') == 'Y') {
      workEffortName = (!this.secondaryLang) ? element.partyName : element.partyNameLang;
      etch = element.parentRoleCode;
    }
    else if (this.comments.get('showOrgUnit') == 'A') {
      workEffortName = ((!this.secondaryLang) ? element.workEffortName : element.workEffortNameLang) ?? "";
      etch = element.workEffortEtch;

      partyName = (!this.secondaryLang) ? element.partyName : element.partyNameLang;

      workEffortName = (workEffortName ?? "") + " (" + (partyName ?? "") + ")";
      etch = (etch ?? "") + " (" + (element.parentRoleCode ?? "") + ")";
    }
    else {
      workEffortName = (!this.secondaryLang) ? element.workEffortName : element.workEffortNameLang;
      etch = element.workEffortEtch;
    }

    (!!etch) ? this.tmpChartConfig.etch.push(etch) : this.tmpChartConfig.etch.push('');

    (!!workEffortName) ? this.tmpChartConfig.labels.push(workEffortName) : this.tmpChartConfig.labels.push('');

    if (!!this.comments.get('rangeMaxName')) {
      this.tmpChartConfig.rangeMaxName = this.comments.get('rangeMaxName');

      this.tmpChartConfig.dataMax.push(max);
      this.setChartConfig(element)

    }
    else this.setChartConfig(element);

  }


  /**
   * This function sets the quantities in the dataset for the chart.
   * 
   * @param element - Target.
   */
  setChartConfig(element: WorkEffortAnalysisTarget) {
    if (!!this.scoreEtch1) {
      this.tmpChartConfig.scoreEtch1 = this.scoreEtch1;
      (!!element.scAmount) ? this.tmpChartConfig.dataSC.push(+element.scAmount) : this.tmpChartConfig.dataSC.push(null);
    }
    if (!!this.scoreEtch2) {
      this.tmpChartConfig.scoreEtch2 = this.scoreEtch2;
      (!!element.stAmount) ? this.tmpChartConfig.dataST.push(+element.stAmount) : this.tmpChartConfig.dataST.push(null);
    }

    if (!!this.scoreEtch3) {
      this.tmpChartConfig.scoreEtch3 = this.scoreEtch3;
      (!!element.sc2Amount) ? this.tmpChartConfig.dataSC2.push(element.sc2Amount) : this.tmpChartConfig.dataSC2.push(null);
    }

    if (!!this.scoreEtch4) {
      this.tmpChartConfig.scoreEtch4 = this.scoreEtch4;
      (!!element.st2Amount) ? this.tmpChartConfig.dataST2.push(element.st2Amount) : this.tmpChartConfig.dataST2.push(null);
    }


    this.chartConfig = { ...this.tmpChartConfig };


  }

  /**
   * Set the type of chart selected from the dropdown.
   */
  setChartType() {

    this.tmpChartConfig.type = this.selectedChart.code;
    this.tmpChartConfig = { ...this.tmpChartConfig };
    this.chartConfig = this.tmpChartConfig;
  }

  /**
   * Set the headArray for the emoticon list.
   */
  headArrayEmoticonList() {
    this.headArray.push({ head: `${this.nameDetail}`, fieldName: 'workEffortName', actionInput: 'null', actionOutput: 'outputClickLabelData', display: "table-cell", filter: "textFilter", sortIcon: true });
    if (!!this.scoreEtch1) this.headArray.push({ head: `${this.scoreEtch1}`, fieldName: 'scoreEtch1', actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
    if (!!this.scoreEtch2) this.headArray.push({ head: `${this.scoreEtch2}`, fieldName: 'scoreEtch2', actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
    if (!!this.scoreEtch3) this.headArray.push({ head: `${this.scoreEtch3}`, fieldName: 'scoreEtch3', actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
    if (!!this.scoreEtch4) this.headArray.push({ head: `${this.scoreEtch4}`, fieldName: 'scoreEtch4', actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });

  }

  /**
   * Set the headArray for the gauge list.
   */
  headArrayGaugeList() {
    this.headArray.push({ head: `${this.nameDetail}`, fieldName: 'workEffortName', actionInput: 'null', actionOutput: 'outputClickLabelData', display: "table-cell", filter: "textFilter", sortIcon: true });
    if (!!this.scoreEtch1) this.headArray.push({ head: `${this.scoreEtch1}`, fieldName: 'scoreEtch1', actionInput: 'null', actionOutput: 'actionGauge', display: "table-cell", filter: "null", gaugeConfig: this.tmpGaugeConfig[4], sortIcon: false });
    if (!!this.scoreEtch2) this.headArray.push({ head: `${this.scoreEtch2}`, fieldName: 'scoreEtch2', actionInput: 'null', actionOutput: 'actionGauge', display: "table-cell", filter: "null", gaugeConfig: this.tmpGaugeConfig[5], sortIcon: false });
    if (!!this.scoreEtch3) this.headArray.push({ head: `${this.scoreEtch3}`, fieldName: 'scoreEtch3', actionInput: 'null', actionOutput: 'actionGauge', display: "table-cell", filter: "null", gaugeConfig: this.tmpGaugeConfig[6], sortIcon: false });
    if (!!this.scoreEtch4) this.headArray.push({ head: `${this.scoreEtch4}`, fieldName: 'scoreEtch4', actionInput: 'null', actionOutput: 'actionGauge', display: "table-cell", filter: "null", gaugeConfig: this.tmpGaugeConfig[7], sortIcon: false });

  }

  /**
  * Set the headArray for the gauge flex list.
  */
  headArrayGaugeFlexList() {
    this.headArray.push({ head: `${this.nameDetail}`, fieldName: 'workEffortName', actionInput: 'null', actionOutput: 'outputClickLabelData', display: "table-cell", filter: "textFilter", sortIcon: true });
    if (this.scoreEtch1) this.headArray.push({ head: `${this.scoreEtch1}`, fieldName: 'scoreEtch1', actionInput: 'null', actionOutput: ActionOutput.actionGaugeFlex, display: "table-cell", filter: "null", sortIcon: false });
  }

  /**
   * Set the headArray for the chart configuration.
   */
  headArrayConfChart() {
    this.headArray.push({ head: `${this.nameDetail}`, fieldName: 'workEffortName', actionInput: 'null', actionOutput: 'outputClickLabelData', display: "table-cell", filter: "textFilter", sortIcon: true });
    if (!!this.scoreEtch1) this.headArray.push({ head: `${this.scoreEtch1}`, fieldName: 'scoreEtch1', actionInput: 'null', actionOutput: 'actionAmount', display: "table-cell", filter: "null", sortIcon: false });
    if (!!this.scoreEtch2) this.headArray.push({ head: `${this.scoreEtch2}`, fieldName: 'scoreEtch2', actionInput: 'null', actionOutput: 'actionAmount', display: "table-cell", filter: "null", sortIcon: false });
    if (!!this.scoreEtch3) this.headArray.push({ head: `${this.scoreEtch3}`, fieldName: 'scoreEtch3', actionInput: 'null', actionOutput: 'actionAmount', display: "table-cell", filter: "null", sortIcon: false });
    if (!!this.scoreEtch4) this.headArray.push({ head: `${this.scoreEtch4}`, fieldName: 'scoreEtch4', actionInput: 'null', actionOutput: 'actionAmount', display: "table-cell", filter: "null", sortIcon: false });

  }

  /**
   * Set the src of the images to be displayed in the header.
   * 
   * @param element - Target element.
   */
  setSrcImgHeader(element: WorkEffortAnalysisTarget, type: string) {

    if (!!this.comments.get("scoreEtch1")) {
      this.emoticonCardConf1.text = this.comments.get("scoreEtch1");
      this.emoticonCardConf1.iconContentId = element.rvcIconContentId;
      if (type != 'EMOTONLY') this.emoticonCardConf1.value = element.scAmount;
    }

    if (!!this.comments.get("scoreEtch2")) {
      this.emoticonCardConf2.text = this.comments.get("scoreEtch2");
      this.emoticonCardConf2.iconContentId = element.rvtIconContentId;
      if (type != 'EMOTONLY') this.emoticonCardConf2.value = element.stAmount;
    }

    if (!!this.comments.get("scoreEtch3")) {
      this.emoticonCardConf3.text = this.comments.get("scoreEtch3");
      this.emoticonCardConf3.iconContentId = element.rvc2IconContentId;
      if (type != 'EMOTONLY') this.emoticonCardConf3.value = element.sc2Amount;
    }

    if (!!this.comments.get("scoreEtch4")) {
      this.emoticonCardConf4.text = this.comments.get("scoreEtch4");
      this.emoticonCardConf4.iconContentId = element.rvt2IconContentId;
      if (type != 'EMOTONLY') this.emoticonCardConf4.value = element.st2Amount;
    }

  }

  /**
   * Set the src of the images to be displayed in each row of the table.
   * 
   * @param element - Target element.
   */
  setSrcImgGridArray(element: WorkEffortAnalysisTarget) {

    let i;

    if (!!element.drcObjectInfo) {
      i = element.drcObjectInfo.indexOf("resources");
      this.srcImgSC = element.drcObjectInfo.substring(i - 1);
      this.srcImgSC = element.rvcIconContentId;
    }
    else this.srcImgSC = null;

    if (!!element.drtObjectInfo) {
      i = element.drtObjectInfo.indexOf("resources");
      this.srcImgST = element.drtObjectInfo.substring(i - 1);
      this.srcImgST = element.rvtIconContentId;
    }
    else this.srcImgST = null;

    if (!!element.drc2ObjectInfo) {
      i = element.drc2ObjectInfo.indexOf("resources");
      this.srcImgSC2 = element.drc2ObjectInfo.substring(i - 1);
      this.srcImgSC2 = element.rvc2IconContentId;
    }
    else this.srcImgSC2 = null;

    if (!!element.drt2ObjectInfo) {
      i = element.drt2ObjectInfo.indexOf("resources");
      this.srcImgST2 = element.drt2ObjectInfo.substring(i - 1);
      this.srcImgST2 = element.rvt2IconContentId;
    }
    else this.srcImgST2 = null;
  }

  /**
   * This function receives the clicked element in the breadcrumb 
   * and calls the routingDetail function to recalculate the data to display.
   * 
   * @param data 
   */
  onItemClick(data) {

    this.tmpItems = this.tmpItems.filter(item => this.tmpItems.indexOf(item) < this.tmpItems.indexOf(data.item));

    if (data.item.routerLink && !data.item.icon) this.router.navigateByUrl(`/c/` + this.context + `/consultation/analysis`, { skipLocationChange: true }).then(() =>
      this.router.navigateByUrl(data.item.routerLink, { replaceUrl: true }));

    if (!!data.item.id) this.routingDetail(data.item.id);

  }

  resetVariable() {
    this.workEffort = true;
    this.disButtSliMenu = true;
    this.itemsButtonSlideMenu = [];
    this.mainScore = null;
    this.detailScore = null;
    this.nameDetail = null;
    this.nameKPI = null;
    this.loadingKPI = true;


    this.headArray = [
      { head: '', fieldName: '', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", sortIcon: true },
      { head: '', fieldName: '', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", sortIcon: false },
      { head: '', fieldName: 'idNumber', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", gaugeConfig: null, sortIcon: false },
    ];
    this.headArrayKPI = [
      { head: '', fieldName: '', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", linkImg: null, value: null, sortIcon: false },
      { head: '', fieldName: 'idNumber', actionInput: 'null', actionOutput: 'outputLabelData', display: "none", filter: "null", gaugeConfig: null },
      { head: this.i18nService.translate('Indicator'), fieldName: 'indicator', actionInput: 'null', actionOutput: 'outputLabelData', display: "table-cell", filter: "textFilter", sortIcon: true },

    ];
    this.gridArray = [];
    this.tmpGridArray = [];
    this.loadingWorkEffort = true;
    this.gridArrayKPI = [];
    this.tmpGridArrayKPI = [];

    this.buttonSlideMenuKPI = [];

    this.chartConfig = null;

    //Show Pdo Score
    this.showPdoScore = false;
    this.loadingPdoScore = true;
    this.chartConfigPdoScore = null;
    this.selectedChartItemPdoScore = null;

    //Show Detail Score
    this.showKPIscore = false;
    this.loadingKPIscore = true;
    this.chartConfigKPIscore = null;
    this.selectedChartItemKPIscore = null;

    //Show Detail Pdo Score
    this.showDetailPdoScore = false;
    this.loadingDetailPdoScore = true;
    this.chartConfigDetailPdoScore = null;
    this.selectedChartItemDetailPdoScore = null;

    //Show Pdo Account
    this.showPdoAccount = [];
    this.loadingPdoAccount = [];
    this.chartConfigPdoAccount = [];
    this.selectedItemPdoAccount = [];

    //show Pdo Score KPI
    this.showPdoScorekpi = [];
    this.loadingPdoScorekpi = [];
    this.chartConfigPdoScorekpi = [];
    this.selectedChartItemPdoScorekpi = [];


    this.scoreEtch1 = null;
    this.scoreEtch2 = null;
    this.scoreEtch3 = null;
    this.scoreEtch4 = null;
    this.etchScore1 = null;
    this.etchScore2 = null;
    this.etchScore3 = null;
    this.etchScore4 = null;

    this.srcImg1 = null;
    this.srcImg2 = null;
    this.srcImg3 = null;
    this.srcImg4 = null;
    this.srcImgSC = null;
    this.srcImgST = null;
    this.srcImgSC2 = null;
    this.srcImgST2 = null;

    this.emoticonCardConf1 = { text: null, value: null, iconContentId: null };
    this.emoticonCardConf2 = { text: null, value: null, iconContentId: null };
    this.emoticonCardConf3 = { text: null, value: null, iconContentId: null };
    this.emoticonCardConf4 = { text: null, value: null, iconContentId: null };

    /* gaugeConfig1..4 header gauge configuration variables */
    this.gaugeConfig1 = { gaugeLabel: "", gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null };
    this.gaugeConfig2 = { gaugeLabel: "", gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null };
    this.gaugeConfig3 = { gaugeLabel: "", gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null };
    this.gaugeConfig4 = { gaugeLabel: "", gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null };


    this.tmpGaugeConfig = [
      { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch1 header
      { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch2 header
      { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch3 header
      { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch4 header
      { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch1 table
      { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch2 table
      { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch3 table
      { gaugeLabel: null, gaugeValue: null, gaugeMin: null, gaugeMax: null, fromValueGreen: null, fromValueRed: null, fromValueYellow: null }, //gaugeConfig scoreEtch4 table
    ];


    this.tmpChartConfig = { type: null, labels: [], etch: [], rangeMaxName: null, dataMax: [], dataCon: [], dataSC: [], dataST: [], dataSC2: [], dataST2: [], scoreEtch1: null, scoreEtch2: null, scoreEtch3: null, scoreEtch4: null };
    this.items = [];
    this.comments.clear();



    this.chartItems = [
      { name: 'RADAR', code: 'radar' },
      { name: this.i18nService.translate('BAR'), code: 'bar' },
      { name: this.i18nService.translate('LINE'), code: 'line' },

    ];
    this.selectedChart = { name: 'RADAR', code: 'radar' };
    this.itemsButtonSlideMenu = [];
  }

  /**
   * This function processes the data to be displayed given a workEffortId.
   * 
   * @param data - Element of gridArray.
   */
  async routingDetail(data) {
    this.secondaryLang = await this.languageService.secondaryLang();

    let workEffortId = data.id;

    this.resetVariable();

    this.workEffort$ = this.workEffortAnalysisService.getWorkEffortAnalysisHeader(this.analysisId, workEffortId)
      .subscribe(async element => {
        this.header = true;
        if (element.length == 0) this.analysisDesc = this.i18nService.translate("There is no data to display");
        else {

          if (!!(<any>element[0]).comments) {
            this.secondaryLang = await this.languageService.secondaryLang()

            this.comments = this.getComments((<any>element[0]).comments);
            this.nameDetail = (!this.secondaryLang) ? this.comments.get('nameDetail') : this.comments.get('nameDetailLang');
            this.nameKPI = (!this.secondaryLang) ? this.comments.get('nameKPI') : this.comments.get('nameKPILang');       //second folder name
            await this.setScoreEtch();

            this.setButtonSlideMenu(workEffortId);

            let workEffortName, partyName, etch, label;
            if (this.comments.get('showOrgUnitTst') == 'Y') {
              partyName = (!this.secondaryLang) ? (<any>element[0]).party.partyName : (<any>element[0]).party.partyNameLang;
              etch = (<any>element[0]).partyParentRole.parentRoleCode;
              label = (!!etch) ? etch + " - " + partyName : partyName;
            }
            else if (this.comments.get('showOrgUnitTst') == 'A') {
              workEffortName = (!this.secondaryLang) ? (<any>element[0]).workEffort.workEffortName : (<any>element[0]).workEffort.workEffortNameLang;
              etch = (<any>element[0]).workEffort.etch;
              let labelTmp = (!!etch) ? etch + " - " + workEffortName : workEffortName;
              partyName = (!this.secondaryLang) ? (<any>element[0]).party.partyName : (<any>element[0]).party.partyNameLang;
              etch = (<any>element[0]).partyParentRole.parentRoleCode;
              let labelTmp2 = (!!etch) ? etch + " - " + partyName : partyName;

              label = (labelTmp ?? "") + " (" + (labelTmp2 ?? "") + ")"

            }
            else {
              workEffortName = (!this.secondaryLang) ? (<any>element[0]).workEffort.workEffortName : (<any>element[0]).workEffort.workEffortNameLang;
              etch = (<any>element[0]).workEffort.etch;
              label = (!!etch) ? etch + " - " + workEffortName : workEffortName;
            }

            this.analysisDesc = label;
            this.tmpItems.push({ label: this.analysisDesc, url: null, id: data });
            this.items = this.tmpItems;

            this.header$ = this.workEffortAnalysisService.getWorkEffortAnalysisTargetHeaderOne(this.analysisId, workEffortId, this.comments.get('rangeDefault'))
              .subscribe((element) => {

                this.targets = <WorkEffortAnalysisTarget>element[0];

                this.setValueHeader(this.targets);

                if (this.comments.get('mainScore') == 'EMOTICON' || this.comments.get('mainScore') == 'EMOTONLY') {

                  this.mainScore = "EMOTICON";
                  this.setSrcImgHeader(this.targets, this.comments.get('mainScore'));
                }

                if (this.comments.get('mainScore') == 'GAUGE') {

                  this.mainScore = "GAUGE";
                  this.uomRangeValues$ = this.uomRangeValuesService.uomRangeValues(this.comments.get('rangeDefault')).subscribe(data => {
                    data.forEach(e => {

                      if (e.colorEnumId == "GREEN") this.tmpGaugeConfig.forEach(conf => conf.fromValueGreen = e.fromValue);
                      if (e.colorEnumId == "YELLOW") this.tmpGaugeConfig.forEach(conf => conf.fromValueYellow = e.fromValue);
                      if (e.colorEnumId == "RED") this.tmpGaugeConfig.forEach(conf => conf.fromValueRed = e.fromValue);

                    });

                    this.maxValuePromise = this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault'));


                    this.maxValuePromise.then(
                      response => {
                        this.maxValue = response
                        this.tmpGaugeConfig.forEach(conf => conf.gaugeMax = response);

                        this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
                          .subscribe(min => {

                            this.tmpGaugeConfig.forEach(conf => conf.gaugeMin = min);

                            this.tmpGaugeConfig[0].gaugeValue = this.value1;
                            this.gaugeConfig1 = this.tmpGaugeConfig[0];

                            this.tmpGaugeConfig[1].gaugeValue = this.value2;
                            this.gaugeConfig2 = this.tmpGaugeConfig[1];

                            this.tmpGaugeConfig[2].gaugeValue = this.value3;
                            this.gaugeConfig3 = this.tmpGaugeConfig[2];

                            this.tmpGaugeConfig[3].gaugeValue = this.value4;
                            this.gaugeConfig4 = this.tmpGaugeConfig[3];

                          })

                      })
                  })

                }
                if (this.comments.get('mainScore') == 'GAUGEFLEX') {

                  this.mainScore = "GAUGEFLEX";
                  let tmpGauge: gaugeConfig = { fromValueRed: 0, fromValueYellow: this.value2, fromValueGreen: this.value3 };

                  this.maxValuePromise = this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault'));

                  this.maxValuePromise.then(
                    response => {
                      this.maxValue = response
                      tmpGauge.gaugeMax = response;

                      this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
                        .subscribe(min => {
                          tmpGauge.gaugeMin = min;
                          tmpGauge.gaugeValue = this.value1;
                          this.gaugeConfig1 = tmpGauge;
                        })

                    })
                }


              })

            if (this.comments.get('detailScore') == "EMOTICON_LIST" || this.comments.get('detailScore') == "EMOTONLY_LIST") {
              this.detailScore = this.comments.get('detailScore');
              this.configDataRouting(workEffortId);
            }

            if (this.comments.get('detailScore') == "GAUGE_LIST") {
              this.detailScore = "GAUGE_LIST";

              this.uomRangeValues$ = this.uomRangeValuesService.uomRangeValues(this.comments.get('rangeDefault')).subscribe(data => {
                data.forEach(e => {

                  if (e.colorEnumId == "GREEN") this.tmpGaugeConfig.forEach(conf => conf.fromValueGreen = e.fromValue);
                  if (e.colorEnumId == "YELLOW") this.tmpGaugeConfig.forEach(conf => conf.fromValueYellow = e.fromValue);
                  if (e.colorEnumId == "RED") this.tmpGaugeConfig.forEach(conf => conf.fromValueRed = e.fromValue);

                });

                this.maxValuePromise = this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault'));

                this.maxValuePromise.then(
                  response => {
                    this.maxValue = response;
                    this.tmpGaugeConfig.forEach(conf => conf.gaugeMax = response);

                    this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
                      .subscribe(min => {
                        this.tmpGaugeConfig.forEach(conf => conf.gaugeMin = min);

                        this.configDataRouting(workEffortId);
                      })

                  })


              })
            }

            if (this.comments.get('detailScore') == "GAUGEFLEX_LIST") {
              this.detailScore = "GAUGEFLEX_LIST";

              this.maxValuePromise = this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault'));

              this.maxValuePromise.then(
                response => {
                  this.maxValue = response;
                  this.tmpGaugeConfig.forEach(conf => conf.gaugeMax = response);

                  this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
                    .subscribe(min => {
                      this.tmpGaugeConfig.forEach(conf => conf.gaugeMin = min);

                      this.configDataRouting(workEffortId);
                    })

                })
            }

            if (this.comments.get('detailScore') == "RADAR") {

              this.detailScore = "CHART";
              this.selectedChart = { name: 'RADAR', code: 'radar' };
              this.tmpChartConfig.type = 'radar';
              this.configDataRouting(workEffortId);
            }

            if (this.comments.get('detailScore') == "LINE") {
              this.detailScore = "CHART";
              this.selectedChart = { name: this.i18nService.translate('LINE'), code: 'line' };
              this.tmpChartConfig.type = 'line';
              this.configDataRouting(workEffortId);
            }

            if (this.comments.get('detailScore') == "BAR") {

              this.detailScore = "CHART";
              this.selectedChart = { name: this.i18nService.translate('BAR'), code: 'bar' };
              this.tmpChartConfig.type = 'bar';
              this.configDataRouting(workEffortId);
            }

            if (this.comments.get('detailScore') == "PIE") {

              this.detailScore = "CHART";
              this.selectedChart = { name: this.i18nService.translate('PIE'), code: 'pie' };
              this.tmpChartConfig.type = 'pie';
              this.configDataRouting(workEffortId);
            }

            if (this.comments.get('detailScore') == "POLAR") {

              this.detailScore = "CHART";
              this.selectedChart = { name: this.i18nService.translate('POLAR'), code: 'polarArea' };
              this.tmpChartConfig.type = 'polarArea';
              this.configDataRouting(workEffortId);
            }

            if (this.comments.get('detailScore') == "DOUGHNUT") {

              this.detailScore = "CHART";
              this.selectedChart = { name: this.i18nService.translate('DOUGHNUT'), code: 'doughnut' };
              this.tmpChartConfig.type = 'doughnut';
              this.configDataRouting(workEffortId);
            }


            if (!!this.comments.get('detailKPI') && !!this.nameKPI) {
              this.setEtchScore();
              this.setDetailKPI(workEffortId);
            }

          }
          else this.analysisDesc = this.i18nService.translate("Parameterization missing");
        }

      })


  }


  /**
   * This function sets the gridArray for the table 
   * and defines the information for the detail score.
   * 
   * @param workEffortId - Work effort id.
   */
  configDataRouting(workEffortId) {



    if (this.detailScore == "CHART") {
      this.minValue$ = this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))
        .subscribe(min => {

          this.tmpChartConfig.dataMin = min;
        });


      if (!!this.comments.get("showEtchDescr")) {
        this.tmpChartConfig.showEtchDescr = this.comments.get("showEtchDescr");
        this.radioButtonChart.selectedItem = this.radioButtonChart.items.filter(x => x.key === this.comments.get("showEtchDescr"))[0];

      } else {
        this.tmpChartConfig.showEtchDescr = 'E';
        this.radioButtonChart.selectedItem = this.radioButtonChart.items.filter(x => x.key === 'E')[0];

      }

    }
    this.workEffortList$ = this.workEffortAnalysisService.getWorkEffortAnalysisTargetListWithWE(this.analysisId, workEffortId, (!!this.comments.get('dateControl')) ? this.comments.get('dateControl') : "NONE", this.comments.get('rangeDefault'), (this.comments.get('showOrgUnit')) ? this.comments.get('showOrgUnit') : "N")
      .subscribe(async we => {
        //(we.length == 0) ? this.nameDetail = null : this.nameDetail = (!this.secondaryLang) ? this.comments.get('nameDetail') : this.comments.get('nameDetailLang'); //first folder name
        if (this.detailScore == "CHART") {
          this.headArrayConfChart();
          await this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault')).then(response => {
            this.maxValue = response;

          });
        }
        if (this.detailScore == "EMOTICON_LIST" || this.detailScore == "EMOTONLY_LIST") { this.headArrayEmoticonList(); }
        if (this.detailScore == "GAUGE_LIST") { this.headArrayGaugeList(); }
        if (this.detailScore == "GAUGEFLEX_LIST") {
          this.headArrayGaugeFlexList();
        }
        if (this.detailScore == "CHART" && we.length < 3) {
          this.selectedChart = { name: this.i18nService.translate('BAR'), code: 'bar' };
          this.tmpChartConfig.type = 'bar';
        }

        we.forEach((e, index) => {

          if (this.comments.get('detailScore') == "EMOTICON_LIST" || this.comments.get('detailScore') == "EMOTONLY_LIST") this.setSrcImgGridArray(e);

          let valSC = null, valST = null, valSC2 = null, valST2 = null;

          if (this.comments.get('detailScore') != "EMOTONLY_LIST") {

            if (!!e.scAmount || e.scAmount == 0) {
              valSC = + e.scAmount;
              valSC = + valSC.toFixed(this.precision);
            }

            if (!!e.stAmount || e.stAmount == 0) {
              valST = + e.stAmount;
              valST = + valST.toFixed(this.precision);
            }

            if (!!e.sc2Amount || e.sc2Amount == 0) {
              valSC2 = + e.sc2Amount;
              valSC2 = + valSC2.toFixed(this.precision);
            }

            if (!!e.st2Amount || e.st2Amount == 0) {
              valST2 = + e.st2Amount;
              valST2 = + valST2.toFixed(this.precision);
            }

          }

          let workEffortName, partyName, etch, label;
          if (this.comments.get('showOrgUnit') == 'Y') {
            partyName = (!this.secondaryLang) ? e.partyName : e.partyNameLang;
            etch = e.parentRoleCode;
            label = (!!etch) ? etch + " - " + partyName : partyName;
          }
          else if (this.comments.get('showOrgUnit') == 'A') {
            workEffortName = (!this.secondaryLang) ? e.workEffortName : e.workEffortNameLang;
            etch = e.workEffortEtch;
            let labelTmp = (!!etch) ? etch + " - " + workEffortName : workEffortName;
            partyName = (!this.secondaryLang) ? e.partyName : e.partyNameLang;
            etch = e.parentRoleCode;

            label = labelTmp + " (" + ((!!etch) ? etch + " - " + partyName : partyName) + ")"
          }
          else {
            workEffortName = (!this.secondaryLang) ? e.workEffortName : e.workEffortNameLang;
            etch = e.workEffortEtch;
            label = (!!etch) ? etch + " - " + workEffortName : workEffortName;
          }

          this.tmpGridArray.push({
            id: e.workEffortId,
            workEffortName: label,
            scoreEtch1: { amount: valSC, linkImg: this.srcImgSC, gaugeConfig: { fromValueRed: this.tmpGaugeConfig[4].gaugeMin, fromValueYellow: valST, fromValueGreen: valSC2, gaugeMax: Math.max(this.tmpGaugeConfig[4].gaugeMax, valSC2), gaugeMin: this.tmpGaugeConfig[4].gaugeMin } },
            scoreEtch2: { amount: valST, linkImg: this.srcImgST },
            scoreEtch3: { amount: valSC2, linkImg: this.srcImgSC2 },
            scoreEtch4: { amount: valST2, linkImg: this.srcImgST2 },
            buttonDetails: true,
          })

          if (this.detailScore == "CHART") this.configChart(e, this.maxValue);

        })

        this.gridArray = this.tmpGridArray;
        this.loadingWorkEffort = false;

      })

  }

  setItemButtonSlideMenuKPI(item) {
    this.itemKPI = item;
  }

  setButtonSlideMenuKPI() {
    this.buttonSlideMenuKPI = [];

    if (!!this.comments.get('pdoAccount')) {
      this.buttonSlideMenuKPI.push({
        label: this.i18nService.translate('Historical indicator trend'),
        icon: 'pi pi-angle-right',
        command: () => { this.pdoAccountDialog(this.comments.get('pdoAccount')); }
      })
    }

    if (!!this.comments.get('pdoScoreKpi')) {
      this.buttonSlideMenuKPI.push({
        label: this.i18nService.translate('Historical score trend'),
        icon: 'pi pi-angle-right',
        command: () => { this.PdoScorekpiDialog(this.comments.get('pdoScoreKpi')); }
      })
    }
  }

  /**
   * Sets the headArrayKPI and the gridArrayKPI.
   * 
   * @param workEffortId - Work effort id.
   */
  setDetailKPI(workEffortId) {

    this.setButtonSlideMenuKPI();

    const OTHER_MEASURE = "OTHER_MEASURE";
    const DATE_MEASURE = "DATE_MEASURE";

    if (this.comments.get('detailKPI') == 'SCORE') {

      if (this.comments.get('showKpiType') == 'Y') this.headArrayKPI.push({ head: this.i18nService.translate('Type'), fieldName: `typology`, actionInput: 'null', actionOutput: 'outputLabelData', display: "table-cell", filter: "null", sortIcon: false });
      if (this.comments.get('showKpiUdm') == 'Y') this.headArrayKPI.push({ head: this.i18nService.translate('Measures'), fieldName: `measures`, actionInput: 'null', actionOutput: 'outputLabelData', display: "table-cell", filter: "null", sortIcon: false });

      if (!!this.comments.get('etchKPI1')) this.headArrayKPI.push({ head: this.i18nService.translate(`${this.comments.get('etchKPI1')}`), fieldName: `etchKPI1`, actionInput: 'null', actionOutput: 'outputLabelNumber', display: "table-cell", filter: "null", sortIcon: false });
      if (!!this.comments.get('etchKPI2')) this.headArrayKPI.push({ head: this.i18nService.translate(`${this.comments.get('etchKPI2')}`), fieldName: `etchKPI2`, actionInput: 'null', actionOutput: 'outputLabelNumber', display: "table-cell", filter: "null", sortIcon: false });
      if (!!this.comments.get('etchKPI3')) this.headArrayKPI.push({ head: this.i18nService.translate(`${this.comments.get('etchKPI3')}`), fieldName: `etchKPI3`, actionInput: 'null', actionOutput: 'outputLabelNumber', display: "table-cell", filter: "null", sortIcon: false });
      if (!!this.comments.get('etchKPI4')) this.headArrayKPI.push({ head: this.i18nService.translate(`${this.comments.get('etchKPI4')}`), fieldName: `etchKPI4`, actionInput: 'null', actionOutput: 'outputLabelNumber', display: "table-cell", filter: "null", sortIcon: false });
      if (!!this.etchScore1) this.headArrayKPI.push({ head: this.etchScore1, fieldName: `etchScore1`, actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
      if (!!this.etchScore2) this.headArrayKPI.push({ head: this.etchScore2, fieldName: `etchScore2`, actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
      if (!!this.etchScore3) this.headArrayKPI.push({ head: this.etchScore3, fieldName: `etchScore3`, actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
      if (!!this.etchScore4) this.headArrayKPI.push({ head: this.etchScore4, fieldName: `etchScore4`, actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
      if (this.buttonSlideMenuKPI.length > 0) this.headArrayKPI.push({ head: '', fieldName: '', actionInput: ActionInput.actionDetails, actionOutput: 'null', display: "table-cell", filter: "null", sortIcon: false })

      this.detailKPI$ = this.workEffortAnalysisService.getDetailKPIScore(
        this.analysisId,
        workEffortId,
        (!!this.comments.get('dateControl')) ? this.comments.get('dateControl') : "NONE",
        this.comments.get('rangeDefault'),
        this.comments.get("purposeKPIList")?.split(",").map(x => x.trim()) ?? [])
        .subscribe(async detail => {
          let precisionKPI;
          await this.glAccountService.getPrecisionDecimal("SCOREKPI").then(async x => precisionKPI = x);

          detail.forEach((e, index) => {
            //let img = (!!e.drcObjectInfo) ? e.drcObjectInfo.substring(e.drcObjectInfo.indexOf("resources") - 1) : "";

            let labelIndicator;
            let accountName = (!this.secondaryLang) ? e.accountName : e.accountNameLang;
            (this.comments.get('showKpiCode') == 'Y') ? labelIndicator = e.accountCode + ' - ' + accountName : labelIndicator = accountName;

            let description = (!this.secondaryLang) ? e.description : e.descriptionLang;
            let abbreviation = (!this.secondaryLang) ? e.abbreviation : e.abbreviationLang;

            let uomTypeId: string = e.uomTypeId;

            let valKPI1, valKPI2, valKPI3, valKPI4;

            if (uomTypeId == OTHER_MEASURE) {
              valKPI1 = this.parseFloatToFixed(e.scAmount, e.decimalScale);
              valKPI2 = this.parseFloatToFixed(e.stAmount, e.decimalScale);
              valKPI3 = this.parseFloatToFixed(e.sc2Amount, e.decimalScale);
              valKPI4 = this.parseFloatToFixed(e.st2Amount, e.decimalScale);
            }
            else if (uomTypeId == DATE_MEASURE) {
              valKPI1 = getDate(addDaysFromGiulianDate(parseInt(e.scAmount)));
              valKPI2 = getDate(addDaysFromGiulianDate(parseInt(e.stAmount)));
              valKPI3 = getDate(addDaysFromGiulianDate(parseInt(e.sc2Amount)));
              valKPI4 = getDate(addDaysFromGiulianDate(parseInt(e.st2Amount)));
            }
            else {
              valKPI1 = e.scAmount;
              valKPI2 = e.stAmount;
              valKPI3 = e.sc2Amount;
              valKPI4 = e.st2Amount;
            }

            let scIconAmount = null, stIconAmount = null, sc2IconAmount = null, st2IconAmount = null;

            if (this.comments.get("showScore") != 'N') {
              scIconAmount = this.parseFloatToFixed(e.scIconAmount, precisionKPI);
              stIconAmount = this.parseFloatToFixed(e.stIconAmount, precisionKPI);
              sc2IconAmount = this.parseFloatToFixed(e.sc2IconAmount, precisionKPI);
              st2IconAmount = this.parseFloatToFixed(e.st2IconAmount, precisionKPI);
            }

            this.tmpGridArrayKPI.push({
              id: index,
              indicator: labelIndicator,
              typology: description,
              measures: abbreviation,
              etchKPI1: valKPI1,
              etchKPI2: valKPI2,
              etchKPI3: valKPI3,
              etchKPI4: valKPI4,
              etchScore1: { amount: scIconAmount, linkImg: e.rvcIconContentId },
              etchScore2: { amount: stIconAmount, linkImg: e.rvtIconContentId },
              etchScore3: { amount: sc2IconAmount, linkImg: e.rvc2IconContentId },
              etchScore4: { amount: st2IconAmount, linkImg: e.rvt2IconContentId },
              inputEnumId: e.inputEnumId,
              detectOrgUnitIdFlag: e.detectOrgUnitIdFlag,
              glAccountId: e.glAccountId,
              orgUnitId: e.orgUnitId,
              orgUnitRoleTypeId: e.orgUnitRoleTypeId,
              workEffortMeasureId: e.workEffortMeasureId,
              buttonDetails: (this.buttonSlideMenuKPI.length > 0),
            })


          })
          this.loadingKPI = false;

          this.gridArrayKPI = this.tmpGridArrayKPI;
        })

    }

    else if (this.comments.get('detailKPI') == 'PERIOD') {

      this.getWorkEffortAnalysis$ = this.workEffortAnalysisService.getWorkEffortAnalysis(this.analysisId)
        .subscribe(wea => {

          if (this.comments.get('showKpiType') == 'Y') this.headArrayKPI.push({ head: this.i18nService.translate('Type'), fieldName: `typology`, actionInput: 'null', actionOutput: 'outputLabelData', display: "table-cell", filter: "null", sortIcon: false });
          if (this.comments.get('showKpiUdm') == 'Y') this.headArrayKPI.push({ head: this.i18nService.translate('Measures'), fieldName: `measures`, actionInput: 'null', actionOutput: 'outputLabelData', display: "table-cell", filter: "null", sortIcon: false });
          this.headArrayKPI.push({ head: `Stato\nAvanzamento`, fieldName: `etchIndex`, actionInput: 'null', actionOutput: 'actionMultipleRowString', display: "table-cell", filter: "null", sortIcon: false });

          if (!!wea.labelM4Prev) this.headArrayKPI.push({ head: this.i18nService.translate(`${wea.labelM4Prev}`), fieldName: `labelM4Prev`, actionInput: 'null', actionOutput: 'actionMultipleRowNumber', display: "table-cell", filter: "null", sortIcon: false, align: 'right' });
          if (!!wea.labelM3Prev) this.headArrayKPI.push({ head: this.i18nService.translate(`${wea.labelM3Prev}`), fieldName: `labelM3Prev`, actionInput: 'null', actionOutput: 'actionMultipleRowNumber', display: "table-cell", filter: "null", sortIcon: false, align: 'right' });
          if (!!wea.labelM2Prev) this.headArrayKPI.push({ head: this.i18nService.translate(`${wea.labelM2Prev}`), fieldName: `labelM2Prev`, actionInput: 'null', actionOutput: 'actionMultipleRowNumber', display: "table-cell", filter: "null", sortIcon: false, align: 'right' });
          if (!!wea.labelM1Prev) this.headArrayKPI.push({ head: this.i18nService.translate(`${wea.labelM1Prev}`), fieldName: `labelM1Prev`, actionInput: 'null', actionOutput: 'actionMultipleRowNumber', display: "table-cell", filter: "null", sortIcon: false, align: 'right' });
          if (!!wea.labelPrev) this.headArrayKPI.push({ head: this.i18nService.translate(`${wea.labelPrev}`), fieldName: `labelPrev`, actionInput: 'null', actionOutput: 'actionMultipleRowNumber', display: "table-cell", filter: "null", sortIcon: false, align: 'right' });
          if (!!wea.labelP1Prev) this.headArrayKPI.push({ head: this.i18nService.translate(`${wea.labelP1Prev}`), fieldName: `labelP1Prev`, actionInput: 'null', actionOutput: 'actionMultipleRowNumber', display: "table-cell", filter: "null", sortIcon: false, align: 'right' });
          if (!!wea.labelP2Prev) this.headArrayKPI.push({ head: this.i18nService.translate(`${wea.labelP2Prev}`), fieldName: `labelP2Prev`, actionInput: 'null', actionOutput: 'actionMultipleRowNumber', display: "table-cell", filter: "null", sortIcon: false, align: 'right' });
          if (!!wea.labelP3Prev) this.headArrayKPI.push({ head: this.i18nService.translate(`${wea.labelP3Prev}`), fieldName: `labelP3Prev`, actionInput: 'null', actionOutput: 'actionMultipleRowNumber', display: "table-cell", filter: "null", sortIcon: false, align: 'right' });
          if (!!wea.labelP4Prev) this.headArrayKPI.push({ head: this.i18nService.translate(`${wea.labelP4Prev}`), fieldName: `labelP4Prev`, actionInput: 'null', actionOutput: 'actionMultipleRowNumber', display: "table-cell", filter: "null", sortIcon: false, align: 'right' });

          if (!!this.etchScore1) this.headArrayKPI.push({ head: this.etchScore1, fieldName: `etchScore1`, actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
          if (!!this.etchScore2) this.headArrayKPI.push({ head: this.etchScore2, fieldName: `etchScore2`, actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
          if (!!this.etchScore3) this.headArrayKPI.push({ head: this.etchScore3, fieldName: `etchScore3`, actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
          if (!!this.etchScore4) this.headArrayKPI.push({ head: this.etchScore4, fieldName: `etchScore4`, actionInput: 'null', actionOutput: 'actionEmoticonCard', display: "table-cell", filter: "null", sortIcon: false });
          if (this.buttonSlideMenuKPI.length > 0) this.headArrayKPI.push({ head: '', fieldName: '', actionInput: ActionInput.actionDetails, actionOutput: 'null', display: "table-cell", filter: "null", sortIcon: false })


          this.detailKPI$ = this.workEffortAnalysisService.getDetailKPIPeriod(
            this.analysisId,
            workEffortId,
            (!!this.comments.get('dateControl')) ? this.comments.get('dateControl') : "NONE",
            this.comments.get('rangeDefault'),
            this.comments.get("purposeKPIList")?.split(",").map(x => x.trim()) ?? [])
            .subscribe(async detail => {
              let indicator = [];
              let precisionKPI;
              await this.glAccountService.getPrecisionDecimal("SCOREKPI").then(async x => precisionKPI = x);

              let acCode = [];

              for (let index = indicator.length; index < detail.length; index += indicator.length) {
                let accountCode = detail[index].accountCode + detail[index].accountName;
                if (acCode.indexOf(accountCode) < 0) {
                  acCode.push(accountCode);

                  indicator = detail.filter(x => x.accountCode == detail[index].accountCode && x.accountName == detail[index].accountName);

                  //let img = (!!detail[index].drcObjectInfo) ? detail[index].drcObjectInfo.substring(detail[index].drcObjectInfo.indexOf("resources") - 1) : "";

                  let labelIndicator;
                  let accountName = (!this.secondaryLang) ? detail[index].accountName : detail[index].accountNameLang;
                  (this.comments.get('showKpiCode') == 'Y') ? labelIndicator = detail[index].accountCode + ' - ' + accountName : labelIndicator = accountName;

                  let description = (!this.secondaryLang) ? detail[index].description : detail[index].descriptionLang;
                  let abbreviation = (!this.secondaryLang) ? detail[index].abbreviation : detail[index].abbreviationLang;

                  let uomTypeId: string = detail[index].uomTypeId;

                  let valM4, valM3, valM2, valM1, val, valP1, valP2, valP3, valP4;
                  let type = "";
                  if (uomTypeId == OTHER_MEASURE) {
                    type = OTHER_MEASURE;
                    valM4 = indicator.map(x => this.parseFloatToFixed(x.m4Amount, detail[index].decimalScale));
                    valM3 = indicator.map(x => this.parseFloatToFixed(x.m3Amount, detail[index].decimalScale));
                    valM2 = indicator.map(x => this.parseFloatToFixed(x.m2Amount, detail[index].decimalScale));
                    valM1 = indicator.map(x => this.parseFloatToFixed(x.m1Amount, detail[index].decimalScale));
                    val = indicator.map(x => this.parseFloatToFixed(x.isAmount, detail[index].decimalScale));
                    valP1 = indicator.map(x => this.parseFloatToFixed(x.p1Amount, detail[index].decimalScale));
                    valP2 = indicator.map(x => this.parseFloatToFixed(x.p2Amount, detail[index].decimalScale));
                    valP3 = indicator.map(x => this.parseFloatToFixed(x.p3Amount, detail[index].decimalScale));
                    valP4 = indicator.map(x => this.parseFloatToFixed(x.p4Amount, detail[index].decimalScale));
                  }
                  else if (uomTypeId == DATE_MEASURE) {
                    type = DATE_MEASURE
                    valM4 = indicator.map(x => getDate(addDaysFromGiulianDate(parseInt(x.m4Amount))));
                    valM3 = indicator.map(x => getDate(addDaysFromGiulianDate(parseInt(x.m3Amount))));
                    valM2 = indicator.map(x => getDate(addDaysFromGiulianDate(parseInt(x.m2Amount))));
                    valM1 = indicator.map(x => getDate(addDaysFromGiulianDate(parseInt(x.m1Amount))));
                    val = indicator.map(x => getDate(addDaysFromGiulianDate(parseInt(x.isAmount))));
                    valP1 = indicator.map(x => getDate(addDaysFromGiulianDate(parseInt(x.p1Amount))));
                    valP2 = indicator.map(x => getDate(addDaysFromGiulianDate(parseInt(x.p2Amount))));
                    valP3 = indicator.map(x => getDate(addDaysFromGiulianDate(parseInt(x.p3Amount))));
                    valP4 = indicator.map(x => getDate(addDaysFromGiulianDate(parseInt(x.p4Amount))));
                  }
                  else {
                    valM4 = indicator.map(x => x.m4Amount);
                    valM3 = indicator.map(x => x.m3Amount);
                    valM2 = indicator.map(x => x.m2Amount);
                    valM1 = indicator.map(x => x.m1Amount);
                    val = indicator.map(x => x.isAmount);
                    valP1 = indicator.map(x => x.p1Amount);
                    valP2 = indicator.map(x => x.p2Amount);
                    valP3 = indicator.map(x => x.p3Amount);
                    valP4 = indicator.map(x => x.p4Amount);
                  }

                  let scIconAmount = null, stIconAmount = null, sc2IconAmount = null, st2IconAmount = null;

                  if (this.comments.get("showScore") != 'N') {
                    scIconAmount = this.parseFloatToFixed(detail[index].scIconAmount, precisionKPI);
                    stIconAmount = this.parseFloatToFixed(detail[index].stIconAmount, precisionKPI);
                    sc2IconAmount = this.parseFloatToFixed(detail[index].sc2IconAmount, precisionKPI);
                    st2IconAmount = this.parseFloatToFixed(detail[index].st2IconAmount, precisionKPI);
                  }


                  this.tmpGridArrayKPI.push({
                    id: accountCode,
                    indicator: labelIndicator,
                    typology: description,
                    measures: abbreviation,
                    etchIndex: indicator.map(x => x.descFTId),
                    labelM4Prev: valM4,
                    labelM3Prev: valM3,
                    labelM2Prev: valM2,
                    labelM1Prev: valM1,
                    labelPrev: val,
                    labelP1Prev: valP1,
                    labelP2Prev: valP2,
                    labelP3Prev: valP3,
                    labelP4Prev: valP4,
                    etchScore1: { amount: scIconAmount, linkImg: detail[index].rvcIconContentId },
                    etchScore2: { amount: stIconAmount, linkImg: detail[index].rvtIconContentId },
                    etchScore3: { amount: sc2IconAmount, linkImg: detail[index].rvc2IconContentId },
                    etchScore4: { amount: st2IconAmount, linkImg: detail[index].rvt2IconContentId },
                    inputEnumId: detail[index].inputEnumId,
                    detectOrgUnitIdFlag: detail[index].detectOrgUnitIdFlag,
                    glAccountId: detail[index].glAccountId,
                    orgUnitId: detail[index].orgUnitId,
                    orgUnitRoleTypeId: detail[index].orgUnitRoleTypeId,
                    workEffortMeasureId: detail[index].workEffortMeasureId,
                    buttonDetails: (this.buttonSlideMenuKPI.length > 0),
                    type: type,
                  })

                }
              }
              this.loadingKPI = false;
              this.gridArrayKPI = this.tmpGridArrayKPI;

            })

        })

    }
    else { console.log("Error value detailKPI") }

  }


  /**
   * This function converts a string to a float 
   * with the precision specified.
   * 
   * @param value 
   * @returns - float value.
   */
  parseFloatToFixed(value, precision) {
    return (!!value) ? parseFloat(value).toFixed(precision) : null;

  }

  /**
   * This function sets the display of the labels for the chart.
   * 
   * @param item radioButtom item
   */
  radButSelectItem(item) {

    this.tmpChartConfig.showEtchDescr = item.key;

    this.chartConfig = { ...this.tmpChartConfig };

  }

  pdoScoreDialog(chartType, workEffortId) {

    let selectedChart = this.chartItemsDialog.filter(x => x.code == ChartType[chartType])[0];
    this.selectedChartItemPdoScore = (!!selectedChart) ? selectedChart : this.chartItemsDialog.filter(x => x.code == 'bar')[0];
    this.showPdoScore = false;
    setTimeout(() => { this.showPdoScore = true; }, 0);

    this.pdoScore$ = this.workEffortAnalysisService.getPdoScore(workEffortId)
      .subscribe(async scoreList => {

        let tmpScoreChart: chartConfig = { type: null, labels: [], datasets: [] };

        let labelsState: boolean = false;

        if (scoreList.length > 0) {
          tmpScoreChart.type = (ChartType[chartType]) ? ChartType[chartType] : 'bar';
          let exit = false;
          while (!exit) {
            let labelValore = (!this.secondaryLang) ? scoreList[0].glFiscalType.description : scoreList[0].glFiscalType.descriptionLang;
            let tmpScore = scoreList.filter(x => (!this.secondaryLang) ? x.glFiscalType.description == labelValore : x.glFiscalType.descriptionLang == labelValore);

            if (!labelsState) {
              let labels: string[] = [];
              scoreList.forEach(x => {
                let code = (!this.secondaryLang) ? x.customTimePeriod.customTimePeriodCode : x.customTimePeriod.customTimePeriodCodeLang;
                if (!labels.find(element => element == code)) {
                  labels.push(code);

                }
              })
              tmpScoreChart.labels.push(...labels);
              labelsState = true;
            }
            let dataset: number[] = [];

            tmpScore.forEach((record, index) => {
              let indexLabel = index;
              let added: boolean = false;
              while (!added) {
                if (((!this.secondaryLang) ? record.customTimePeriod.customTimePeriodCode : record.customTimePeriod.customTimePeriodCodeLang) == tmpScoreChart.labels[indexLabel]) {
                  dataset[indexLabel] = record.acctgTransEntry.amount;
                  added = true;

                }
                indexLabel = indexLabel + 1;

              }
            })

            tmpScoreChart.datasets.push({
              label: labelValore,
              data: dataset
            })


            scoreList = scoreList.filter(x => (!this.secondaryLang) ? x.glFiscalType.description != labelValore : x.glFiscalType.descriptionLang != labelValore);
            if (scoreList.length == 0) exit = true;
          }
          await lastValueFrom(this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))).then(min => tmpScoreChart.min = min);
          await this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault')).then(max => tmpScoreChart.max = max);

          this.chartConfigPdoScore = tmpScoreChart;
          this.loadingPdoScore = false;
        }
        else {
          this.loadingPdoScore = false;

        }
      });
  }


  KPIscoreDialog(chartType, workEffortId) {
    //console.log("KPIscore")

    let selectedChart = this.chartItemsDialog.filter(x => x.code == ChartType[chartType])[0];
    this.selectedChartItemKPIscore = (!!selectedChart) ? selectedChart : this.chartItemsDialog.filter(x => x.code == 'bar')[0];
    this.showKPIscore = false;
    setTimeout(() => { this.showKPIscore = true; }, 0);

    this.KPIscore$ = this.workEffortAnalysisService.getKPIscore(workEffortId, this.analysisRefDate)
      .subscribe(async scoreList => {
        //console.log(scoreList)

        let tmpScoreChart: chartConfig = { type: null, labels: [], datasets: [] };

        let labelsState: boolean = false;
        if (scoreList.length > 0) {
          tmpScoreChart.type = (ChartType[chartType]) ? ChartType[chartType] : 'bar';
          let exit = false;
          while (!exit) {
            let labelValore = (!this.secondaryLang) ? scoreList[0].glFiscalType.description : scoreList[0].glFiscalType.descriptionLang;
            let tmpScore = scoreList.filter(x => (!this.secondaryLang) ? x.glFiscalType.description == labelValore : x.glFiscalType.descriptionLang == labelValore);

            if (!labelsState) {
              let labels: string[] = [];
              scoreList.forEach(x => {
                let name = (!this.secondaryLang) ? x.glAccount.accountName : x.glAccount.accountNameLang;
                if (!labels.find(element => element == name)) {
                  labels.push(name);

                }
              })
              tmpScoreChart.labels.push(...labels);
              labelsState = true;
            }
            let dataset: number[] = [];

            tmpScore.forEach((record, index) => {
              let indexLabel = index;
              let added: boolean = false;
              while (!added) {
                if (((!this.secondaryLang) ? record.glAccount.accountName : record.glAccount.accountNameLang) == tmpScoreChart.labels[indexLabel]) {
                  dataset[indexLabel] = record.acctgTransEntry.amount;
                  added = true;

                }
                indexLabel = indexLabel + 1;
              }
            })
            tmpScoreChart.datasets.push({
              label: labelValore,
              data: dataset
            })


            scoreList = scoreList.filter(x => (!this.secondaryLang) ? x.glFiscalType.description != labelValore : x.glFiscalType.descriptionLang != labelValore);
            if (scoreList.length == 0) exit = true;
          }
          await lastValueFrom(this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))).then(min => tmpScoreChart.min = min);
          await this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault')).then(max => tmpScoreChart.max = max);

          this.chartConfigKPIscore = tmpScoreChart;
          this.loadingKPIscore = false;
        }
        else {
          this.loadingKPIscore = false;
        }

      })
  }

  detailPdoScoreDialog(chartType, workEffortId, glFiscalTypeId) {
    // console.log("workEffortId: " + workEffortId + "; glFiscalTypeId: " + glFiscalTypeId)
    let selectedChart = this.chartItemsDialog.filter(x => x.code == ChartType[chartType])[0];
    this.selectedChartItemDetailPdoScore = (!!selectedChart) ? selectedChart : this.chartItemsDialog.filter(x => x.code == 'bar')[0];
    this.showDetailPdoScore = false;
    setTimeout(() => { this.showDetailPdoScore = true; }, 0);
    this.detailPdoScore$ = this.workEffortAnalysisService.getDetailPdoScore(workEffortId, glFiscalTypeId)
      .subscribe(async scoreList => {

        let tmpScoreChart: chartConfig = { type: null, labels: [], datasets: [] };
        let labelsState: boolean = false;
        if (scoreList.length > 0) {
          tmpScoreChart.type = (ChartType[chartType]) ? ChartType[chartType] : 'bar';
          let exit = false;
          while (!exit) {
            let labelValore = (!this.secondaryLang) ? scoreList[0].glAccount.accountName : scoreList[0].glAccount.accountNameLang;
            let tmpScore = scoreList.filter(x => (!this.secondaryLang) ? x.glAccount.accountName == labelValore : x.glAccount.accountNameLang == labelValore);

            if (!labelsState) {
              let labels: string[] = [];
              scoreList.forEach(x => {
                let code = (!this.secondaryLang) ? x.customTimePeriod.customTimePeriodCode : x.customTimePeriod.customTimePeriodCodeLang;
                if (!labels.find(element => element == code)) {
                  labels.push(code);

                }
              })
              tmpScoreChart.labels.push(...labels);
              labelsState = true;
            }
            let dataset: number[] = new Array(tmpScoreChart.labels.length);

            tmpScore.forEach((record, index) => {
              let indexLabel = index;
              let added: boolean = false;
              while (!added) {
                if (((!this.secondaryLang) ? record.customTimePeriod.customTimePeriodCode : record.customTimePeriod.customTimePeriodCodeLang) == tmpScoreChart.labels[indexLabel]) {
                  dataset[indexLabel] = record.acctgTransEntry.amount;
                  added = true;
                }
                indexLabel = indexLabel + 1;
              }
            })
            tmpScoreChart.datasets.push({
              label: labelValore,
              data: dataset
            })


            scoreList = scoreList.filter(x => (!this.secondaryLang) ? x.glAccount.accountName != labelValore : x.glAccount.accountNameLang != labelValore);
            if (scoreList.length == 0) exit = true;
          }
          await lastValueFrom(this.uomRangeValuesService.uomRangeValuesMin(this.comments.get('rangeDefault'))).then(min => tmpScoreChart.min = min);
          await this.uomRangeValuesService.uomRangeValuesMax(this.comments.get('rangeDefault')).then(max => tmpScoreChart.max = max);

          this.chartConfigDetailPdoScore = tmpScoreChart;
          this.loadingDetailPdoScore = false;
        }
        else {
          this.loadingDetailPdoScore = false;
        }

      })

  }


  pdoAccountDialog(chartType) {

    /*console.log("pdoAccount");
    console.log("inputEnumId: " + this.itemKPI.inputEnumId + "; detectOrgUnitIdFlag: " + this.itemKPI.detectOrgUnitIdFlag + "; detectOrgUnitIdFlag: " + this.itemKPI.detectOrgUnitIdFlag)
    console.log(this.itemKPI)*/
    let ref: number;

    this.showPdoAccount.push(false);
    ref = this.showPdoAccount.length - 1;

    this.loadingPdoAccount[ref] = true;

    if (!!this.itemKPI.inputEnumId) {
      if (this.itemKPI.inputEnumId == "ACCINP_OBJ") {
        this.pdoAccountWEMI$ = this.workEffortAnalysisService.getPdoAccountWEMI(this.itemKPI.glAccountId, this.itemKPI.workEffortMeasureId)
          .subscribe(async scoreList => {
            this.setPdoAccountDialog(chartType, scoreList, this.itemKPI, ref);
          })

      }
      else if (this.itemKPI.inputEnumId == "ACCINP_UO" && !!this.itemKPI.detectOrgUnitIdFlag && this.itemKPI.detectOrgUnitIdFlag == "Y") {
        this.pdoAccountOrgUnit$ = this.workEffortAnalysisService.getPdoAccountOrgUnit(this.itemKPI.glAccountId, this.itemKPI.orgUnitRoleTypeId, this.itemKPI.orgUnitId)
          .subscribe(async scoreList => {
            this.setPdoAccountDialog(chartType, scoreList, this.itemKPI, ref);
          })
      }
      else if (this.itemKPI.inputEnumId == "ACCINP_UO") {
        this.pdoAccount$ = this.workEffortAnalysisService.getPdoAccount(this.itemKPI.glAccountId)
          .subscribe(async scoreList => {
            this.setPdoAccountDialog(chartType, scoreList, this.itemKPI, ref);
          })
      }
    }

  }


  async setPdoAccountDialog(chartType: string, scoreList: Score[], itemKPI, ref: number) {
    let selectedChart = this.chartItemsDialog.filter(x => x.code == ChartType[chartType])[0];
    this.selectedItemPdoAccount[ref] = { ...((!!selectedChart) ? selectedChart : this.chartItemsDialog.filter(x => x.code == 'bar')[0]) };
    setTimeout(() => { this.showPdoAccount[ref] = true; }, 0);
    let tmpScoreChart: chartConfig = { type: null, title: itemKPI.indicator, labels: [], datasets: [] };
    let labelsState: boolean = false;

    if (scoreList.length > 0) {
      tmpScoreChart.type = (ChartType[chartType]) ? ChartType[chartType] : 'bar';
      let exit = false;
      while (!exit) {
        let labelValore = (!this.secondaryLang) ? scoreList[0].glFiscalType.description : scoreList[0].glFiscalType.descriptionLang;
        let tmpScore = scoreList.filter(x => (!this.secondaryLang) ? x.glFiscalType.description == labelValore : x.glFiscalType.descriptionLang == labelValore);

        if (!labelsState) {
          let labels: string[] = [];
          scoreList.forEach(x => {
            let code = (!this.secondaryLang) ? x.customTimePeriod.customTimePeriodCode : x.customTimePeriod.customTimePeriodCodeLang;
            if (!labels.find(element => element == code)) {
              labels.push(code);

            }
          })
          tmpScoreChart.labels.push(...labels);
          labelsState = true;
        }
        let dataset: number[] = new Array(tmpScoreChart.labels.length);

        tmpScore.forEach((record, index) => {
          let indexLabel = index;
          let added: boolean = false;
          while (!added) {
            if (((!this.secondaryLang) ? record.customTimePeriod.customTimePeriodCode : record.customTimePeriod.customTimePeriodCodeLang) == tmpScoreChart.labels[indexLabel]) {
              dataset[indexLabel] = record.acctgTransEntry.amount;
              added = true;

            }
            indexLabel = indexLabel + 1;

          }
        });

        tmpScoreChart.datasets.push({
          label: labelValore,
          data: dataset
        });

        scoreList = scoreList.filter(x => ((!this.secondaryLang) ? x.glFiscalType.description != labelValore : x.glFiscalType.descriptionLang != labelValore));
        if (scoreList.length == 0) exit = true;
      }

      tmpScoreChart.responsiveScale = true;

      this.chartConfigPdoAccount[ref] = tmpScoreChart;

    }

    this.loadingPdoAccount[ref] = false;


  }



  PdoScorekpiDialog(chartType) {

    let ref: number;
    this.showPdoScorekpi.push(false);
    ref = this.showPdoScorekpi.length - 1;
    this.loadingPdoScorekpi[ref] = true;

    let selectedChart = this.chartItemsDialog.filter(x => x.code == ChartType[chartType])[0];
    this.selectedChartItemPdoScorekpi[ref] = (!!selectedChart) ? selectedChart : this.chartItemsDialog.filter(x => x.code == 'bar')[0];

    setTimeout(() => { this.showPdoScorekpi[ref] = true; }, 0);
    this.pdoScorekpi$ = this.workEffortAnalysisService.getPdoScorekpi(this.itemKPI.workEffortMeasureId)
      .subscribe(async scoreList => {
        let tmpScoreChart: chartConfig = { type: null, title: this.itemKPI.indicator, labels: [], datasets: [] };
        let labelsState: boolean = false;

        if (scoreList.length > 0) {
          tmpScoreChart.type = (ChartType[chartType]) ? ChartType[chartType] : 'bar';

          let exit = false;
          while (!exit) {
            let labelValore = (!this.secondaryLang) ? scoreList[0].glFiscalType.description : scoreList[0].glFiscalType.descriptionLang;
            let tmpScore = scoreList.filter(x => (!this.secondaryLang) ? x.glFiscalType.description == labelValore : x.glFiscalType.descriptionLang == labelValore);

            if (!labelsState) {
              let labels: string[] = [];
              scoreList.forEach(x => {
                let code = (!this.secondaryLang) ? x.customTimePeriod.customTimePeriodCode : x.customTimePeriod.customTimePeriodCodeLang;
                if (!labels.find(element => element == code)) {
                  labels.push(code);

                }
              });

              tmpScoreChart.labels.push(...labels);
              labelsState = true;
            }
            let dataset: number[] = [];

            tmpScore.forEach((record, index) => {
              let indexLabel = index;
              let added: boolean = false;
              while (!added) {
                if (((!this.secondaryLang) ? record.customTimePeriod.customTimePeriodCode : record.customTimePeriod.customTimePeriodCodeLang) == tmpScoreChart.labels[indexLabel]) {
                  dataset[indexLabel] = record.acctgTransEntry.amount;
                  added = true;

                }
                indexLabel = indexLabel + 1;
              }
            });

            tmpScoreChart.datasets.push({
              label: labelValore,
              data: dataset
            });


            scoreList = scoreList.filter(x => (!this.secondaryLang) ? x.glFiscalType.description != labelValore : x.glFiscalType.descriptionLang != labelValore);
            if (scoreList.length == 0) exit = true;
          }

          tmpScoreChart.responsiveScale = true;

          this.chartConfigPdoScorekpi[ref] = tmpScoreChart;

        }

        this.loadingPdoScorekpi[ref] = false;


      })

  }

  ngOnDestroy() {
    this.detailKPI$?.unsubscribe();
    this.workEffortList$?.unsubscribe();
    this.workEffort$?.unsubscribe();
    this.getWorkEffortAnalysis$?.unsubscribe();
    this.analyses$?.unsubscribe();
    this.header$?.unsubscribe();
    this.uomRangeValues$?.unsubscribe();
    this.uomRangeValuesPathEmoticon$?.unsubscribe();
    this.minValue$?.unsubscribe();
    this.pdoScore$?.unsubscribe();
    this.KPIscore$?.unsubscribe();
    this.detailPdoScore$?.unsubscribe();
    this.pdoAccountWEMI$?.unsubscribe();
    this.pdoAccountOrgUnit$?.unsubscribe();
    this.pdoAccount$?.unsubscribe();
    this.pdoScorekpi$?.unsubscribe();
  }

}


