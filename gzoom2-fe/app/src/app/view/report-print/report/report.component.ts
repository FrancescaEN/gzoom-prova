import { AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, OnInit, SimpleChanges } from '@angular/core';

import { ActivatedRoute, Router } from '@angular/router';
import { FormArray, UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';


import { Observable, Subject, lastValueFrom } from 'rxjs';
import { filter, map, mergeWith, switchMap, take, tap } from 'rxjs/operators';

import { SelectItem } from 'app/commons/model/selectitem';
import { I18NService } from '../../../i18n/i18n.service';
import { Message } from 'app/commons/model/message';

import { Report } from '../report';
import { ReportParam } from '../report';
import { ReportType } from '../report';
import { WorkEffortType } from '../report';

import { Party } from '../../../api/model/party';
import { PartyService } from 'app/api/service/party.service';
import { UomService } from 'app/api/service/uom.service';
import { StatusItemService } from 'app/api/service/status-item.service';
import { RoleType } from '../../../api/model/role-type';
import { RoleTypeService } from 'app/api/service/role-type.service';
import { WorkEffort } from '../../../api/model/work-effort';
import { WorkEffortService } from 'app/api/service/work-effort.service';
import { UomRangeValues } from '../../ctx-ac/uom/range-values/uomRangeValues';


import { ReportDownloadComponent } from '../../../shared/report-download/report-download.component';

import { HttpClient } from '@angular/common/http';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { Enumeration } from 'app/api/model/enumeration';
import { WorkEffortTypeService } from 'app/api/service/work-effort-type.service';
import { WorkEffortRevisionService } from 'app/api/service/work-effort-revision.service';
import { DownloadActivityService } from 'app/shared/report-download/download-activity.service';
import { WorkEffortRevision } from 'app/api/model/workEffortRevision';
import { ApiClientService } from 'app/commons/service/client.service';
import { UserPreferenceService } from 'app/api/service/user-preference.service';
import { CustomTimePeriodService } from 'app/api/service/custom-time-period.service';
import { CustomTimePeriod } from 'app/api/model/customTimePeriod';
import { WorkEffortTypeContentService } from 'app/api/service/work-effort-type-content.service';
import { ReportService } from 'app/api/service/report.service';
import { QueryConfigService } from 'app/api/service/query-config.service';
import { QueryConfig } from 'app/api/model/queryConfig';
import { UomRatingScaleService } from 'app/api/service/uom-rating-scale.service';
import { StatusItem } from 'app/api/model/statusItem';
import { dropdownCustomTimePeriod, dropdownPartyRoleView, dropdownStatusItem } from 'app/commons/utils/dropdownSelectItem';
import { PartyRoleViewService } from 'app/api/service/party-role-view.service';
import { PartyRoleView } from 'app/api/model/partyRoleView';
    
import { of } from 'rxjs';
import { catchError } from 'rxjs/operators';


/** Convert from WorkEffort[] to SelectItem[] */
function workEfforts2SelectItems(types: WorkEffort[], isSecondaryLanguage = false): SelectItem[] {
  if (types == null) {
    return [];
  }

  //language check for use correct label (1st or 2nd language)
  let fieldName = 'workEffortName';
  if (isSecondaryLanguage)
    fieldName = 'workEffortNameLang';

  return types.map((wt: WorkEffort) => {
    return {
      label: (wt.sourceReferenceId != null ? wt.sourceReferenceId + ' - ' + wt[fieldName] : wt[fieldName]),
      value: wt.workEffortId
    };
  });
}

/** Convert from workEffortRevision[] to SelectItems[] */
function workEffortRevision2SelectItems(types: WorkEffortRevision[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffortRevision) => {
    return { label: (wt.description), value: wt.workEffortRevisionId };
  });
}

/** Convert from WorkEffortId20R20P20D[] to SelectItem[] */
function workEffortId20R20P20D2SelectItems(types: WorkEffort[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffort) => {
    return {
      label: (wt.sourceReferenceId != null ? wt.sourceReferenceId + ' - ' + wt.workEffortName : wt.workEffortName),
      value: wt.workEffortId
    };
  });
}

/** Convert from workEffortId20D6[] to SelectItem[] */
function workEffortId20D62SelectItems(types: WorkEffortType[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffort) => {
    return {
      label: (wt.sourceReferenceId != null ? wt.sourceReferenceId + ' - ' + wt.workEffortName : wt.workEffortName),
      value: wt.workEffortId
    };
  });
}

/** Convert from WorkEffortType[] to SelectItem[] */
function workEffortTypes20R20P20D2SelectItems(types: WorkEffortType[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffortType) => {
    return { label: (wt.description), value: wt.workEffortTypeId };
  });
}

/** convert workEffortTypeIdParametric to SelectItem[] */
function workEffortTypeIdParametricSelectItems(types: WorkEffortType[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffortType) => {
    return { label: (wt.description), value: wt.workEffortTypeId };
  });
}

/** convert workEffortTypeIdParametric2 to SelectItem[] */
function workEffortTypeIdParametric2SelectItems(types: WorkEffortType[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffortType) => {
    return { label: (wt.description), value: wt.workEffortTypeId };
  });
}

/** convert workEffortTypeIdParametric3 to SelectItem[] */
function workEffortTypeIdParametric3SelectItems(types: WorkEffortType[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffortType) => {
    return { label: (wt.description), value: wt.workEffortTypeId };
  });
}

/** Convert from RoleType[] to SelectItem[] */
function roleType2SelectItems(party: RoleType[]): SelectItem[] {
  if (party == null) {
    return [];
  }
  return party.map((p: RoleType) => {
    return { label: p.description, value: p.roleTypeId };
  });
}


/** Convert from workEffortIdParametric[] to SelectItem[] */
function workEffortIdParametricSelectItems(types: WorkEffort[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffort) => {
    return {
      label: (wt.sourceReferenceId != null ? wt.sourceReferenceId + ' - ' + wt.workEffortName : wt.workEffortName),
      value: wt.workEffortId
    };
  });
}

/** Convert from workEffortIdParametric2[] to SelectItem[] */
function workEffortIdParametric2SelectItems(types: WorkEffort[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffort) => {
    return {
      label: (wt.sourceReferenceId != null ? wt.sourceReferenceId + ' - ' + wt.workEffortName : wt.workEffortName),
      value: wt.workEffortId
    };
  });
}

/** Convert from workEffortIdParametric3[] to SelectItem[] */
function workEffortIdParametric3SelectItems(types: WorkEffort[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffort) => {
    return {
      label: (wt.sourceReferenceId != null ? wt.sourceReferenceId + ' - ' + wt.workEffortName : wt.workEffortName),
      value: wt.workEffortId
    };
  });
}

/** Convert from workEffortTypeId20D6[] to SelectItem[] */
function workEffortTypeId20D62SelectItems(types: WorkEffortType[]): SelectItem[] {
  if (types == null) {
    return [];
  }
  return types.map((wt: WorkEffortType) => {
    return { label: (wt.description), value: wt.workEffortTypeId };
  });
}


/** Convert from UomRangeValues[] to SelectItem[] */
function uomRangeValues2SelectItems(uomRangeValues: UomRangeValues[], isSecondaryLanguage: boolean = false): SelectItem[] {
  if (uomRangeValues == null) {
    return [];
  }

  let fieldName = 'comments'
  if (isSecondaryLanguage)
    fieldName = 'commentsLang'

  return uomRangeValues.map((p: UomRangeValues) => {
    return { label: p[fieldName], value: p.uomRangeValuesId };
  });
}

/** Convert from Enumeration[] to SelectItem[] */
function enumeration2SelectItems(enumeration: Enumeration[]): SelectItem[] {
  if (enumeration == null) {
    return [];
  }
  return enumeration.map((p: Enumeration) => {
    return { label: p.description, value: p.enumCode };
  });
}

/** Convert from StatusItem[] to SelectItem[] */
function statusItem2SelectItems(status: StatusItem[]): SelectItem[] {
  if (status == null) {
    return [];
  }
  return status.map((p: StatusItem) => {
    return { label: p.description, value: p.description };
  });
}

/** COnvert from languages[] to SelectItem[] */
function languages2SelectItems(lang: string[]): SelectItem[] {
  if (lang == null) {
    return [];
  }
  return lang.map(p => {
    return { label: this.i18nService.translate(p), value: p };
  });
}

/** Convert from WorkEffortType[] to SelectItem[] */
function outputFormat2SelectItems(outputFormat: ReportType[]): SelectItem[] {
  if (outputFormat == null) {
    return [];
  }
  return outputFormat.map((p: ReportType) => {
    return { label: p.description, value: p.mimeTypeId };
  });
}


@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})

export class ReportComponent implements OnInit {
  _reload: Subject<ReloadParams>;
  _reloadWorkffortParams: Subject<ReloadParams>;
  _reloadRoleType: Subject<ReloadParams>;

  /** Error message from be*/
  error = '';
  /** Selected report*/
  selectedReport: Report;
  lang = this.i18nService.getLang();
  isSecondaryLang: boolean = this.i18nService.getIsSecondaryLang();

  reportContentId: string;
  reportContentTypeId: string;
  condQueryConfNum: number[];
  queryConfig$: Observable<QueryConfig>;
  queryConfig: QueryConfig = new QueryConfig;

  outputFormats: ReportType[];
  outputFormatSelectItem: SelectItem[] = [];
  workEffortTypes: WorkEffortType[];
  workEffortRevision: WorkEffortRevision[];
  workEffortTypeSelectItem: SelectItem[] = [];
  params: ReportParam[];

  workEffort: WorkEffort;
  workEfforts: WorkEffort[] = [];
  workEffortId: string;

  workEffortIdSelectItem: SelectItem[] = [];
  orgUnitIdSelectItem: SelectItem[] = [];
  currentStatusNameSelectItem: SelectItem[] = [];
  statusItemByCodeSelectItem: SelectItem[] = [];
  roleTypeIdSelectItem: SelectItem[] = [];
  partyIdSelectItem: SelectItem[] = [];
  workEffortIdChildSelectItem: SelectItem[] = [];
  uomRangeValuesIdSelectItem: SelectItem[] = [];
  customTimePeriodSelectItem: SelectItem[] = [];
  form: UntypedFormGroup;

  personIdSelectItem: SelectItem[] = [];
  partyRoleViewByRoleTypeId0SelectItem: SelectItem[] = [];
  partyRoleViewByRoleTypeId1SelectItem: SelectItem[] = [];
  workEffortTypeIdParametricSelectItem: SelectItem[] = [];
  workEffortIdParametricSelectItem: SelectItem[] = [];
  workEffortTypeIdParametric2SelectItem: SelectItem[] = [];
  workEffortIdParametric2SelectItem: SelectItem[] = [];
  workEffortTypeIdParametric3SelectItem: SelectItem[] = [];
  workEffortIdParametric3SelectItem: SelectItem[] = [];
  workEffortTypeId20R20P20DSelectItem: SelectItem[] = [];
  workEffortTypeId20D6SelectItem: SelectItem[] = [];
  workEffortId20R20P20DSelectItem: SelectItem[] = [];
  workEffortId20D6SelectItem: SelectItem[] = [];
  politicianRoleTypeIdSelectItem: SelectItem[] = [];
  managerIdSelectItem: SelectItem[] = [];
  assessorsSelectItem: SelectItem[] = [];
  enumerationSelectItem: SelectItem[] = [];
  enumerationSelectItem1: SelectItem[] = [];
  enumerationSelectItem2: SelectItem[] = [];
  enumerationSelectItem3: SelectItem[] = [];
  orderBilSelectItem: SelectItem[] = [];
  scorePreviewSelectItem: SelectItem[] = [];
  scoreActualSelectItem: SelectItem[] = [];
  workEffortRevisionSelectItem: SelectItem[] = [];
  defaultOrganizationUnitId = 'Company';
  showUoCodeParamOrgUnit: string;

  paramsSelectItem: any = {};
  paramsOptions: any = {};
  paramsLabel: any = {};
  paramsNonMandatory: string[] = [];

  hiddenMail: boolean;

  msgs: Message[] = [];
  languages: string[] = [];
  languagesSelectItem: SelectItem[] = [];
  langType: string;

  get workEffortTypeId() { return this.form.get('workEffortTypeId') }

  get outputFormat() { return this.form.get('outputFormat') }

  get languageSelected() { return this.form?.get('birtControlName') }

  constructor(private readonly route: ActivatedRoute,
              private readonly router: Router,
              public readonly i18nService: I18NService,
              private readonly reportService: ReportService,
              private readonly partyService: PartyService,
              private readonly partyRoleViewService: PartyRoleViewService,
              private readonly uomService: UomService,
              private readonly uomRatingScaleService: UomRatingScaleService,
              private readonly statusItemService: StatusItemService,
              private readonly roleTypeService: RoleTypeService,
              private readonly workEffortService: WorkEffortService,
              private readonly workEffortTypeService: WorkEffortTypeService,
              private readonly workEffortRevisionService: WorkEffortRevisionService,
              private readonly reportDownloadComponent: ReportDownloadComponent,
              private readonly enumerationService: EnumerationService,
              private readonly downloadActivityService: DownloadActivityService,
              private readonly client: ApiClientService,
              private readonly userPreferenceService: UserPreferenceService,
              private readonly customTimePeriodService: CustomTimePeriodService,
              private readonly workEffortTypeContentService: WorkEffortTypeContentService,
              private readonly changeRef: ChangeDetectorRef,
              private readonly queryConfigService: QueryConfigService,
              private fb: UntypedFormBuilder,
              http: HttpClient) {
    this._reload = new Subject<ReloadParams>();
    this._reloadWorkffortParams = new Subject<ReloadParams>();
    this._reloadRoleType = new Subject<ReloadParams>();
  }

  filterDisplay(param: ReportParam) {
    return param.display;
  }


  ngOnInit() {
    const parentTypeId = this.route.parent.snapshot.data.context;
    const reportContentId = this.route.snapshot.params.reportContentId;

    this.form = this.fb.group({});
    this.form.addControl('outputFormat', new UntypedFormControl('', Validators.required));
    this.form.addControl('workEffortTypeId', new UntypedFormControl('', Validators.required));
    this.form.addControl('birtControlName', new UntypedFormControl(''));

    // Blocco rifattorizzato con reload
    const initialOrgUnits$ = this.userPreferenceService
      .getUserPreference('ORGANIZATION_PARTY')
      .pipe(
        take(1),
        filter(data => !!data?.userPrefValue && data.userPrefValue !== 'DEFAULT'),
        switchMap(data => {
          this.defaultOrganizationUnitId = data.userPrefValue;

          return this.partyService.orgUnits(
            parentTypeId,
            this.paramsOptions['orgUnitId'],
            this.workEffortTypeId.value,
            this.defaultOrganizationUnitId
          ).pipe(
            catchError(err => {
              console.error('Errore orgUnits (initial)', err);
              return of([] as Party[]);
            })
          );
        })
      );

    const reloadedOrgUnits$ = this._reload.pipe(
      switchMap(() =>
        this.partyService.orgUnits(
          parentTypeId,
          this.paramsOptions['orgUnitId'],
          this.workEffortTypeId.value,
          this.defaultOrganizationUnitId
        ).pipe(
          catchError(err => {
            console.error('Errore orgUnits (reload)', err);
            return of([] as Party[]);
          })
        )
      )
    );

    // Merge iniziale + reload, trasformazione in SelectItem e aggiornamento stato
    const orgUnitsSub = initialOrgUnits$
      .pipe(
        mergeWith(reloadedOrgUnits$),
        map((ps: Party[]) => this.orgUnit2SelectItems(ps) as SelectItem[])
      )
      .subscribe((orgUnitsData: SelectItem[]) => {
        this.orgUnitIdSelectItem = orgUnitsData;
        this.paramsSelectItem['orgUnitIdSelectItem'] = [...this.orgUnitIdSelectItem];
    });

    // ---- LANGUAGES (ex async/await) ----
    this.client
      .get('/profile/i18n/languages')
      .pipe(map(json => json.results as string[]))
      .subscribe((languages: string[]) => {
        this.languages = languages;
        console.log('Lista dei languages ', this.languages)

        this.languageSelected.setValue(this.languages[0]);

        this.languagesSelectItem = this.languages.map((p: string) => {
          return {
            label: this.i18nService.translate(p),
            value: p
          };
        });

        this.form.get('language')?.setValue(this.languagesSelectItem[0]);
      });
    // -----------------------------------
    //console.log("languages available report ", this.languages);

    this.langType = this.i18nService.getLanguageType();

    const reloadedStatus = this._reload.pipe(switchMap(() => this.statusItemService.statusItems(parentTypeId)));
    const reloadedStatusObs = this.route.data.pipe(
      map((data: { statusItems: StatusItem[] }) => data.statusItems),
      mergeWith(reloadedStatus),
      map(statusItem2SelectItems)
    ).subscribe((data) => {
      this.currentStatusNameSelectItem = data;
      this.paramsSelectItem['currentStatusNameSelectItem'] = [...this.currentStatusNameSelectItem];

    });

    const reloadedStatusItemByCode = this._reload.pipe(switchMap(() => this.statusItemService.getStatusItemByCode(this.paramsOptions['statusItemByCode'])));
    const reloadedStatusItemByCodeObs = this.route.data.pipe(
      map((data: { statusItems: StatusItem[] }) => data.statusItems),
      mergeWith(reloadedStatusItemByCode),
      map(x => dropdownStatusItem(x, this.secondaryLang()))
    ).subscribe((data: SelectItem[]) => {
      this.statusItemByCodeSelectItem = data;
      this.paramsSelectItem['statusItemByCodeSelectItem'] = [...this.statusItemByCodeSelectItem];

    });


    const reloadedRoleType = this._reload.pipe(switchMap(() => this.roleTypeService.roleTypes()));
    const reloadedRoleTypeObs = this.route.data.pipe(
      map((data: { roleTypes: RoleType[] }) => data.roleTypes),
      mergeWith(reloadedRoleType),
      map(roleType2SelectItems)
    ).subscribe((data) => {
      this.roleTypeIdSelectItem = data;
      this.paramsSelectItem['roleTypeIdSelectItem'] = [...this.roleTypeIdSelectItem];
    });

    const reloadedParty = this._reloadRoleType.pipe(
      switchMap(params => (params.roleTypeId ? this.partyService.roleTypePartys(params.roleTypeId, this.paramsOptions['partyId'], this.workEffortTypeId.value) : reloadedParty)));
    const reloadedPartyObs = this.route.data.pipe(
      map((data: { partys: Party[] }) => data.partys),
      mergeWith(reloadedParty),
      map(this.party2SelectItems)
    ).subscribe((data) => {
      this.partyIdSelectItem = data;
      this.paramsSelectItem['partyIdSelectItem'] = [...this.partyIdSelectItem];

    });

    const reloadedWorkEffort = this._reload.pipe(
      switchMap(params =>
        (params.workEffortTypeId ? this.workEffortService.workEfforts(parentTypeId, this.paramsOptions['workEffortId'] ? this.paramsOptions['workEffortId'] : params.workEffortTypeId, this.selectedReport.useFilter)
          : reloadedWorkEffort)));
    const workEffortObs = this.route.data.pipe(
      map((data: { workEfforts: WorkEffort[] }) => data.workEfforts),
      mergeWith(reloadedWorkEffort),
      map((data: WorkEffort[]) => workEfforts2SelectItems(data, this.secondaryLang()))
    ).subscribe((data) => {
      this.workEffortIdSelectItem = data;
      this.paramsSelectItem['workEffortIdSelectItem'] = [...this.workEffortIdSelectItem];
    });

    const reloadedPerson = this._reload.pipe(switchMap(() => this.partyService.roleTypePartys('EMPLOYEE', this.paramsOptions['personId'], this.workEffortTypeId.value)));
    const reloadedPersonObs = this.route.data.pipe(
      map((data: { partys: Party[] }) => data.partys),
      mergeWith(reloadedPerson),
      map(ps => this.party2SelectItems(ps))
    ).subscribe((data) => {
      let emptyPersonIdSelectItem = [];
      this.personIdSelectItem = emptyPersonIdSelectItem.concat(data);
      this.paramsSelectItem['personIdSelectItem'] = [...this.personIdSelectItem];
    });

    const reloadedPartyRoleViewByRoleTypeId0 = this._reload.pipe(switchMap(() => this.partyRoleViewService.getPartyRoleViewByRoleTypeId(this.paramsOptions['partyRoleViewByRoleTypeId0'])));
    const reloadedPartyRoleViewByRoleTypeId0Obs = this.route.data.pipe(
      map((data: { partyRoleViews: PartyRoleView[] }) => data.partyRoleViews),
      mergeWith(reloadedPartyRoleViewByRoleTypeId0),
      map(ps => dropdownPartyRoleView(ps))
    ).subscribe((data) => {
      let emptyPartyRoleViewByRoleTypeId0SelectItem = [];
      this.partyRoleViewByRoleTypeId0SelectItem = emptyPartyRoleViewByRoleTypeId0SelectItem.concat(data);
      this.paramsSelectItem['partyRoleViewByRoleTypeId0SelectItem'] = [...this.partyRoleViewByRoleTypeId0SelectItem];
    });

    const reloadedPartyRoleViewByRoleTypeId1 = this._reload.pipe(switchMap(() => this.partyRoleViewService.getPartyRoleViewByRoleTypeId(this.paramsOptions['partyRoleViewByRoleTypeId1'])));
    const reloadedPartyRoleViewByRoleTypeId1Obs = this.route.data.pipe(
      map((data: { partyRoleViews: PartyRoleView[] }) => data.partyRoleViews),
      mergeWith(reloadedPartyRoleViewByRoleTypeId1),
      map(ps => dropdownPartyRoleView(ps))
    ).subscribe((data) => {
      let emptyPartyRoleViewByRoleTypeId1SelectItem = [];
      this.partyRoleViewByRoleTypeId1SelectItem = emptyPartyRoleViewByRoleTypeId1SelectItem.concat(data);
      this.paramsSelectItem['partyRoleViewByRoleTypeId1SelectItem'] = [...this.partyRoleViewByRoleTypeId1SelectItem];
    });

    const reloadedPoliticianTypeId = this._reload.pipe(switchMap(() => this.partyService.roleTypePartysBetween('WEM_P1,WEM_P99')));
    const reloadPoliticianTypeIdObs = this.route.data.pipe(
      map((data: { partys: Party[] }) => data.partys),
      mergeWith(reloadedPoliticianTypeId),
      map(ps => this.party2SelectItems(ps))
    ).subscribe((data) => {
      this.politicianRoleTypeIdSelectItem = data;
      this.paramsSelectItem['politicianRoleTypeIdSelectItem'] = [...this.politicianRoleTypeIdSelectItem];
    });

    const reloadedManagerId = this._reload.pipe(switchMap(() => this.partyService.getManagers()));
    const reloadedManagerIdObs = this.route.data.pipe(
      map((data: { managers: Party[] }) => data.managers),
      mergeWith(reloadedManagerId),
      filter(ps => !!ps),
      map(ps => ps?.map(item => { return { label: item.partyName, value: item.partyId } }))
    ).subscribe((data) => {
      this.managerIdSelectItem = data;
      this.paramsSelectItem['managerIdSelectItem'] = [...this.managerIdSelectItem];
    });

    const reloadedAssessors = this._reload.pipe(switchMap(() => this.enumerationService.enumerations('ASSESSORS')));
    const reloadedAssessorsObs = this.route.data.pipe(
      map((data: { enumerations: Enumeration[] }) => data.enumerations),
      mergeWith(reloadedAssessors),
      map(enumeration2SelectItems)
    ).subscribe((data) => {
      this.assessorsSelectItem = data;
      this.paramsSelectItem['assessorsSelectItem'] = [...this.assessorsSelectItem];
    });

    /**
     * Generic filter for enumeration table, take enumeration_type_id as option param from json
     */
    const reloadedEnumeration = this._reload.pipe(switchMap(() => this.enumerationService.enumerations(this.paramsOptions['enumeration'])));
    const reloadedEnumerationObs = this.route.data.pipe(
      map((data: { enumerations: Enumeration[] }) => data.enumerations),
      mergeWith(reloadedEnumeration),
      map(enumeration2SelectItems)
    ).subscribe((data) => {
      this.enumerationSelectItem = data;
      this.paramsSelectItem['enumerationSelectItem'] = [...this.enumerationSelectItem];
    });

    const reloadedEnumeration2 = this._reload.pipe(switchMap(() => this.enumerationService.enumerations(this.paramsOptions['enumeration2'])));
    const reloadedEnumerationObs2 = this.route.data.pipe(
      map((data: { enumerations: Enumeration[] }) => data.enumerations),
      mergeWith(reloadedEnumeration2),
      map(enumeration2SelectItems)
    ).subscribe((data) => {
      this.enumerationSelectItem2 = data;
      this.paramsSelectItem['enumeration2SelectItem'] = [...this.enumerationSelectItem2];
    });

    const reloadedEnumeration3 = this._reload.pipe(switchMap(() => this.enumerationService.enumerations(this.paramsOptions['enumeration3'])));
    const reloadedEnumerationObs3 = this.route.data.pipe(
      map((data: { enumerations: Enumeration[] }) => data.enumerations),
      mergeWith(reloadedEnumeration3),
      map(enumeration2SelectItems)
    ).subscribe((data) => {
      this.enumerationSelectItem3 = data;
      this.paramsSelectItem['enumeration3SelectItem'] = [...this.enumerationSelectItem3];
    });

    const reloadedOrderbil = this._reload.pipe(switchMap(() => this.enumerationService.enumerations('ORDERBIL')));
    const reloadedOrderbilObs = this.route.data.pipe(
      map((data: { enumerations: Enumeration[] }) => data.enumerations),
      mergeWith(reloadedOrderbil),
      map(enumeration2SelectItems)
    ).subscribe((data) => {
      this.orderBilSelectItem = data;
      this.paramsSelectItem['orderBilSelectItem'] = [...this.orderBilSelectItem];
    });

    const reloadedScorePreview = this._reload.pipe(switchMap(() => this.enumerationService.enumerations('SCOREPREVIEW')));
    const reloadedScorePreviewObs = this.route.data.pipe(
      map((data: { enumerations: Enumeration[] }) => data.enumerations),
      mergeWith(reloadedScorePreview),
      map(enumeration2SelectItems)
    ).subscribe((data) => {
      this.scorePreviewSelectItem = data;
      this.paramsSelectItem['scorePreviewSelectItem'] = [...this.scorePreviewSelectItem];
    });

    const reloadedActualPreview = this._reload.pipe(switchMap(() => this.enumerationService.enumerations('SCOREACTUAL')));
    const reloadedActualPreviewObs = this.route.data.pipe(
      map((data: { enumerations: Enumeration[] }) => data.enumerations),
      mergeWith(reloadedActualPreview),
      map(enumeration2SelectItems)
    ).subscribe((data) => {
      this.scoreActualSelectItem = data;
      this.paramsSelectItem['scoreActualSelectItem'] = [...this.scoreActualSelectItem];
    });

    const reloadedWorkEffortTypeIdParametric = this._reload.pipe(
      switchMap(() => this.workEffortTypeService.workEffortTypesParametric(this.workEffortTypeId.value))); //'20P92STM'
    const workEffortTypeIdParametricObs = this.route.data.pipe(
      map((data: { workEffortTypes: WorkEffortType[] }) => data.workEffortTypes),
      mergeWith(reloadedWorkEffortTypeIdParametric),
      map(workEffortTypeIdParametricSelectItems)
    ).subscribe((data) => {
      this.workEffortTypeIdParametricSelectItem = data;
      this.paramsSelectItem['workEffortTypeIdParametricSelectItem'] = [...this.workEffortTypeIdParametricSelectItem];
    });

    const reloadedWorkEffortIdParametric = this._reloadWorkffortParams.pipe(
      switchMap(params => (params.workEffortTypeIdParametric ?
        this.workEffortService.workEfforts(parentTypeId, params.workEffortTypeIdParametric, this.selectedReport.useFilter)
        : reloadedWorkEffortIdParametric)));
    const reloadedWorkEffortIdParametricObs = this.route.data.pipe(
      map((data: { workEfforts: WorkEffort[] }) => data.workEfforts),
      mergeWith(reloadedWorkEffortIdParametric),
      map(workEffortIdParametricSelectItems)
    ).subscribe((data) => {
      this.workEffortIdParametricSelectItem = data;
      this.paramsSelectItem['workEffortIdParametricSelectItem'] = [...this.workEffortIdParametricSelectItem];
    });

    const reloadedWorkEffortTypeIdParametric2 = this._reload.pipe(
      switchMap(() => this.workEffortTypeService.workEffortTypesParametric(this.workEffortTypeId.value)));
    const workEffortTypeIdParametricObs2 = this.route.data.pipe(
      map((data: { workEffortTypes: WorkEffortType[] }) => data.workEffortTypes),
      mergeWith(reloadedWorkEffortTypeIdParametric2),
      map(workEffortTypeIdParametric2SelectItems)
    ).subscribe((data) => {
      this.workEffortTypeIdParametric2SelectItem = data;
      let label = 'Select WorkEffortType';
      if (this.form && this.form.get('label') && this.form.get('label').value)
        label = this.form.get('label').value;
      this.paramsSelectItem['workEffortTypeIdParametric2SelectItem'] = [...this.workEffortTypeIdParametric2SelectItem];
    });

    const reloadedWorkEffortIdParametric2 = this._reloadWorkffortParams.pipe(
      switchMap(params => (params.workEffortTypeIdParametric2 ?
        this.workEffortService.workEfforts(parentTypeId, params.workEffortTypeIdParametric2, this.selectedReport.useFilter)
        : reloadedWorkEffortIdParametric2)));
    const reloadedWorkEffortIdParametricObs2 = this.route.data.pipe(
      map((data: { workEfforts: WorkEffort[] }) => data.workEfforts),
      mergeWith(reloadedWorkEffortIdParametric2),
      map(workEffortIdParametric2SelectItems)
    ).subscribe((data) => {
      this.workEffortIdParametric2SelectItem = data;
      this.paramsSelectItem['workEffortIdParametric2SelectItem'] = [...this.workEffortIdParametric2SelectItem];
    });

    const reloadedWorkEffortTypeIdParametric3 = this._reload.pipe(
      switchMap(() => this.workEffortTypeService.workEffortTypesParametric(this.workEffortTypeId.value)));
    const workEffortTypeIdParametricObs3 = this.route.data.pipe(
      map((data: { workEffortTypes: WorkEffortType[] }) => data.workEffortTypes),
      mergeWith(reloadedWorkEffortTypeIdParametric3),
      map(workEffortTypeIdParametric3SelectItems)
    ).subscribe((data) => {
      this.workEffortTypeIdParametric3SelectItem = data;
      this.paramsSelectItem['workEffortTypeIdParametric3SelectItem'] = [...this.workEffortTypeIdParametric3SelectItem];
    });

    const reloadedWorkEffortIdParametric3 = this._reloadWorkffortParams.pipe(
      switchMap(params => (params.workEffortTypeIdParametric3 ?
        this.workEffortService.workEfforts(parentTypeId, params.workEffortTypeIdParametric3, this.selectedReport.useFilter)
        : reloadedWorkEffortIdParametric3)));
    const reloadedWorkEffortIdParametricObs3 = this.route.data.pipe(
      map((data: { workEfforts: WorkEffort[] }) => data.workEfforts),
      mergeWith(reloadedWorkEffortIdParametric3),
      map(workEffortIdParametric3SelectItems)
    ).subscribe((data) => {
      this.workEffortIdParametric3SelectItem = data;
      this.paramsSelectItem['workEffortIdParametric3SelectItem'] = [...this.workEffortIdParametric3SelectItem];
    });

    const reloadedWorkEffortTypeId20R20P20D = this._reload.pipe(switchMap(() => this.workEffortTypeService.workEffortTypesParametric(this.workEffortTypeId.value))); //'20P92STM'
    const workEffortTypeId20R20P20DObs = this.route.data.pipe(
      map((data: { workEffortTypes: WorkEffortType[] }) => data.workEffortTypes),
      mergeWith(reloadedWorkEffortTypeId20R20P20D),
      map(workEffortTypes20R20P20D2SelectItems)
    ).subscribe((data) => {
      this.workEffortTypeId20R20P20DSelectItem = data;
      this.paramsSelectItem['workEffortTypeId20R20P20DSelectItem'] = [...this.workEffortTypeId20R20P20DSelectItem];
    });

    const reloadedWorkEffortId20R20P20D = this._reload.pipe(
      switchMap(params => (params.workEffortTypeId20R20P20D ?
        this.workEffortService.workEfforts(parentTypeId, params.workEffortTypeId20R20P20D, this.selectedReport.useFilter)
        : reloadedWorkEffortId20R20P20D)));
    const reloadedWorkEffortId20R20P20DObs = this.route.data.pipe(
      map((data: { workEfforts: WorkEffort[] }) => data.workEfforts),
      mergeWith(reloadedWorkEffortId20R20P20D),
      map(workEffortId20R20P20D2SelectItems)
    ).subscribe((data) => {
      this.workEffortId20R20P20DSelectItem = data;
      this.paramsSelectItem['workEffortId20R20P20DSelectItem'] = [...this.workEffortId20R20P20DSelectItem];
    });

    const reloadedWorkEffortTypeId20D6 = this._reload.pipe(switchMap(() => this.workEffortTypeService.workEffortTypes('20D66%25,20D68%25,20D64%25,20D62%25,20D22%25,20D24%25')));
    const workEffortTypeId20D6Obs = this.route.data.pipe(
      map((data: { workEffortTypes: WorkEffortType[] }) => data.workEffortTypes),
      mergeWith(reloadedWorkEffortTypeId20D6),
      map(workEffortTypeId20D62SelectItems)
    ).subscribe((data) => {
      this.workEffortTypeId20D6SelectItem = data;
      this.paramsSelectItem['workEffortTypeId20D6SelectItem'] = [...this.workEffortTypeId20D6SelectItem];
    });

    const reloadedWorkEffortId20D6 = this._reload.pipe(
      switchMap(params => (params.workEffortTypeId20D6 ? this.workEffortService.workEfforts(parentTypeId, params.workEffortTypeId20D6, this.selectedReport.useFilter)
        : reloadedWorkEffortId20D6)));
    const reloadedWorkEffortId20D6Obs = this.route.data.pipe(
      map((data: { workEfforts: WorkEffort[] }) => data.workEfforts),
      mergeWith(reloadedWorkEffortId20D6),
      map(workEffortId20D62SelectItems)
    ).subscribe((data) => {
      this.workEffortId20D6SelectItem = data;
      this.paramsSelectItem['workEffortId20D6SelectItem'] = [...this.workEffortId20D6SelectItem];
    });

    const reloadedWorkEffortChild = this._reload.pipe(switchMap(params => (params.workEffortId ? this.workEffortService.workEffortParents(params.workEffortId) : reloadedWorkEffortChild)));
    const workEffortChildObs = this.route.data.pipe(
      map((data: { workEfforts: WorkEffort[] }) => data.workEfforts),
      mergeWith(reloadedWorkEffortChild),
      map((data: WorkEffort[]) => workEfforts2SelectItems(data, this.secondaryLang()))
    ).subscribe((data) => {
      this.workEffortIdChildSelectItem = data;
      this.paramsSelectItem['workEffortIdChildSelectItem'] = [...this.workEffortIdChildSelectItem];
    });

    const reloadWorkEffortRevision = this._reload.pipe(
      switchMap(() => this.workEffortRevisionService.workEffortRevisions(parentTypeId)));
    const workEffortRevisionObs = this.route.data.pipe(
      map((data: { workEffortRevision: WorkEffortRevision[] }) => data.workEffortRevision),
      mergeWith(reloadWorkEffortRevision),
      map(workEffortRevision2SelectItems)
    ).subscribe((data) => {
      this.workEffortRevisionSelectItem = data;
      this.paramsSelectItem['workEffortRevisionSelectItem'] = [...this.workEffortRevisionSelectItem];
    });

    const reloadedUomRangeValues = this._reload.pipe(switchMap(() => this.uomRatingScaleService.uomRangeValues('CORRIS')));
    const reloadedUomRangeValuesObs = this.route.data.pipe(
      map((data: { uomRangeValues: UomRangeValues[] }) => data.uomRangeValues),
      mergeWith(reloadedUomRangeValues),
      map(data => uomRangeValues2SelectItems(data, this.secondaryLang()))
    ).subscribe((data) => {
      this.uomRangeValuesIdSelectItem = data;
      this.paramsSelectItem['uomRangeValuesIdSelectItem'] = [...this.uomRangeValuesIdSelectItem];

    });

    const reloadedCustomTimePeriods = this._reload.pipe(switchMap(() => this.customTimePeriodService.customTimePeriods(this.paramsOptions['customTimePeriod'])));
    const reloadedCustomTimePeriodsObs = this.route.data.pipe(
      map((data: { customTimePeriod: CustomTimePeriod[] }) => data.customTimePeriod),
      mergeWith(reloadedCustomTimePeriods),
      map(ct => this.customTimePeriodsValues2SelectItems(ct))
    ).subscribe((data) => {
      this.customTimePeriodSelectItem = data;
      this.paramsSelectItem['customTimePeriodSelectItem'] = [...this.customTimePeriodSelectItem];
    });

    const reloadedWorkEffortTypeContent = this.workEffortTypeContentService.workEffortTypeContentParams(parentTypeId, 'WEFLD_MAIN').subscribe((data) => {
      if (data['showUoCode']) {
        this.showUoCodeParamOrgUnit = data['showUoCode'];
      } else {
        this.showUoCodeParamOrgUnit = 'MAIN';
      }
    });


    const reportObs = this.route.data.pipe(
      map((data: { report: Report }) => data.report)
    ).subscribe((data) => {
      this.onRowSelect(data);
    });

    //STATIC VALUE
    const orientation = [
      { value: 'LANDSCAPE', label: 'Orizontale' },
      { value: 'PORTRAIT', label: 'Verticale' }
    ]
    this.paramsSelectItem['orientationSelectItem'] = [...orientation];

  }




  //********* END ngOnInit */

  party2SelectItems(party: Party[]): SelectItem[] {
    if (party == null) {
      return [];
    }
    return party.map((p: Party) => {
      let label = p.partyName;
      if (this.languages.length > 1 && this.languages[1].indexOf(this.i18nService.getLang()) >= 0 && p.partyNameLang != null) {
        label = p.partyNameLang;
      }
      return { label: label, value: p.partyId };
    });
  }

  /** Convert from Party[] to SelectItem[] */
  orgUnit2SelectItems(party: Party[]): SelectItem[] {
    if (party == null) {
      return [];
    }
    let fieldName = 'partyName';
    if (this.secondaryLang())
      fieldName = 'partyNameLang'

    return party.map((p: Party) => {
      if (this.showUoCodeParamOrgUnit == 'EXT') {
        return { label: p.externalId + ' - ' + p[fieldName], value: p.partyId };
      } else if (this.showUoCodeParamOrgUnit == 'NONE') {
        return { label: p[fieldName], value: p.partyId };
      } else {
        return { label: p.partyParentRole.parentRoleCode + ' - ' + p[fieldName], value: p.partyId };
      }
    });
  }

  customTimePeriodsValues2SelectItems(customTimePeriod: CustomTimePeriod[]): SelectItem[] {
    if (customTimePeriod == null) {
      return [];
    }
    return customTimePeriod.map((p: CustomTimePeriod) => {
      let label = p.periodName;
      if (this.languages.length > 1 && this.languages[1].indexOf(this.i18nService.getLang()) >= 0) {
        label = p.periodNameLang;
      }
      return { label: label, value: this.reportService.getDate(p.thruDate) };
    });
  }

  /** Convert from WorkEffortType[] to SelectItem[] */
  workEffortType2SelectItems(workEffortType: WorkEffortType[]): SelectItem[] {
    if (workEffortType == null) {
      return [];
    }
    return workEffortType.map((p: WorkEffortType) => {
      let label = p.workEffortTypeName;
      if (this.languages.length > 1 && this.languages[1].indexOf(this.i18nService.getLang()) >= 0) {
        label = p.descriptionLang;
      }
      return { label: label, value: p.workEffortTypeId };
    });
  }

  onChangeAll(value, paramName) {
    if (paramName == 'roleTypeId') {
      this._reloadRoleType.next({ roleTypeId: value });
    } else if (paramName == 'workEffortId') {
      this._reload.next({ workEffortId: value });
    } else if (paramName == 'workEffortTypeId20D6') {
      this._reload.next({ workEffortTypeId20D6: value });
    } else if (paramName == 'workEffortTypeId20R20P20D') {
      this._reload.next({ workEffortTypeId20R20P20D: value });
    } else if (paramName == 'workEffortTypeIdParametric') {
      this._reloadWorkffortParams.next({ workEffortTypeIdParametric: value });
    } else if (paramName == 'workEffortTypeIdParametric2') {
      this._reloadWorkffortParams.next({ workEffortTypeIdParametric2: value });
    } else if (paramName == 'workEffortTypeIdParametric3') {
      this._reloadWorkffortParams.next({ workEffortTypeIdParametric3: value });
    }
  }

  onRowSelect(data: Report) {
    this.selectedReport = data;
    this.reportContentTypeId = data.reportContentTypeId;
    this.error = null;
    if (this.reportContentTypeId == 'QUERY_CONFIG') {
      this.onRowSelectQueryConfig(this.selectedReport);
    } else {
      this.onRowSelectReport(this.selectedReport);
    }
  }

  onRowSelectQueryConfig(report: Report) {
    this.condQueryConfNum = [0, 1, 2, 3, 4, 5, 6, 7];
    this.params = [];
    let reportContentId = report.reportContentId;
    this.queryConfig$ = this.queryConfigService.getQueryConfig(reportContentId.substring(reportContentId.indexOf('Q') + 1))
      .pipe(tap(queryConfig => {
        this.queryConfig = queryConfig;
        let paramForm = {};
        for (let index = 0; index < this.condQueryConfNum.length; index++) {
          const name = queryConfig['cond' + index + 'Name'];
          const info = queryConfig['cond' + index + 'Info'];
          if (name)
            paramForm['cond' + index + 'Info'] = new UntypedFormControl(info, Validators.required);

        }
        this.outputFormats = [...report.outputFormats];
        this.outputFormat.setValue(report.outputFormats[0].mimeTypeId);
        paramForm['outputFormat'] = new UntypedFormControl(this.outputFormat.value, Validators.required);
        this.form = this.fb.group(paramForm);
      }));


    this.reportContentTypeId = report.reportContentTypeId;
  }

  onRowSelectReport(data) {
    this.selectedReport = Object.assign({}, data);

    this.paramsNonMandatory = [];

    this.hiddenMail = (this.selectedReport.reportContentId.indexOf('REMINDER') < 0);

    Object.keys(this.form.controls).forEach(controlName => {
      if (!['outputFormat', 'workEffortTypeId', 'birtControlName'].includes(controlName)) {
        this.form.removeControl(controlName);
      }
    });


    data.params.forEach((element) => {

      //gestione required
      if (element.mandatory) {
        this.form.addControl(element.paramName, new UntypedFormControl(element.paramDefault, Validators.required));
      } else {
        this.form.addControl(element.paramName, new UntypedFormControl(element.paramDefault));
        this.paramsNonMandatory.push(element.paramName);
      }

      this.paramsOptions[element.paramName] = element.options;
      this.paramsLabel[element.paramName] = element.label;

    });


    this.outputFormats = [...data.outputFormats];
    this.outputFormatSelectItem = outputFormat2SelectItems(this.workEffortTypes);
    this.outputFormat.setValue(data.outputFormats[0].mimeTypeId)

    let parentTypeId = this.selectedReport.parentTypeId;

    this.workEffortTypes = data.workEffortTypes;
    this.workEffortTypeSelectItem = this.workEffortType2SelectItems(this.workEffortTypes);

    this.onRowSelectWorkEffortType(data.workEffortTypes[0]);

    this.params = [...data.params];
  }

  onRowSelectWorkEffortType(data) {

    this.workEffortTypeId.setValue(data.workEffortTypeId);
    this._reload.next({ workEffortTypeId: this.workEffortTypeId.value });
  }

  setDataReport() {
    this.selectedReport.outputFormat = this.outputFormat.value;
    this.selectedReport.workEffortTypeId = this.workEffortTypeId.value;
    this.selectedReport.birtLocale = this.languageSelected.value ? this.languageSelected.value : (this.languages?.length ? this.languages[0] : this.lang);
    this.selectedReport.defaultOrganizationUnitId = this.defaultOrganizationUnitId;

    //CONVERTO I DATI
    let temp = {};
    Object.keys(this.form.controls).forEach(key => temp[key] = this.form.get(key).value);
    this.selectedReport.paramsValue = Object.assign({}, temp);
    this.params.forEach((element) => {
      if (element.paramType == 'DATE') {
        this.selectedReport.paramsValue[element.paramName] = this.reportService.getDate(this.form.get(element.paramName).value);
      }
    });


  }

  print() {
    if (this.selectedReport.reportContentTypeId == 'QUERY_CONFIG') {

      for (let index = 0; index < this.condQueryConfNum.length; index++) {
        const name = this.queryConfig['cond' + index + 'Name'];

        if (name) {
          const value = this.form.value['cond' + index + 'Info'];
          this.queryConfig['cond' + index + 'Info'] = value;
        }
      }

      this.reportService.addQueryConfig(this.selectedReport, this.queryConfig).then((activityId: string) => {
        this.downloadActivityService.openDownload(activityId);

      })
        .catch((error) => {
          console.error('error.message', error);
          this.error = error;
        });

    } else {
      console.log('print report ');
      this.setDataReport();
      this.reportService
        .add(this.selectedReport)
        .then((activityId: string) => {
          this.downloadActivityService.openDownload(activityId);
        })
        .catch((error) => {
          console.error('error.message', error);
          this.error = this.i18nService.translate(error) || error;
        });
    }
  }


  secondaryLang(): boolean {
    if (this.languages.length > 1 && this.languages[1].indexOf(this.i18nService.getLang()) >= 0) {
      return true;
    }

    return false;
  }

  getPlaceholder(param: any): string {
    if (param.forceUseLabel) { return this.i18nService.translate('Select ' + (param.label ? param.label : param.paramName)) } else {
      return this.i18nService.translate('Select') +
        ' ' +
        this.i18nService.translate(
          param.label ? param.label : param.paramName
        )
    }
  }
}


export interface ReloadParams {
  workEffortTypeId?: string;
  useFilter?: string;
  roleTypeId?: string;
  workEffortId?: string;
  uomRangeId?: string;
  workEffortTypeId20R20P20D?: string;
  workEffortTypeId20D6?: string;
  workEffortTypeIdParametric?: string;
  workEffortTypeIdParametric2?: string;
  workEffortTypeIdParametric3?: string;
}
