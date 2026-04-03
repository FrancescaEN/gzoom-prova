import { AfterContentChecked, AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Enumeration } from 'app/api/model/enumeration';
import { Party } from 'app/api/model/party';
import { WorkEffort } from 'app/api/model/work-effort';
import { WorkEffortAssocType } from 'app/api/model/workEffortAssocType';
import { WorkEffortPurposeType } from 'app/api/model/workEffortPurposeType';
import { WorkEffortTypePeriod } from 'app/api/model/workEffortTypePeriod';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { PartyService } from 'app/api/service/party.service';
import { WorkEffortAssocTypeService } from 'app/api/service/work-effort-assoc-type.service';
import { WorkEffortPurposeTypeService } from 'app/api/service/work-effort-purpose-type.service';
import { WorkEffortTypePeriodService } from 'app/api/service/work-effort-type-period.service';
import { WorkEffortService } from 'app/api/service/work-effort.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { MsgService } from 'app/commons/service/message.service';
import { ToolbarService } from 'app/commons/service/toolbar.service';
import { I18NService } from 'app/i18n/i18n.service';
import { CardDetail } from 'app/layout/gzoom-card-detail/gzoom-card-detail.component';
import { LoaderService } from 'app/shared/loader/loader.service';
import { Location } from "@angular/common";
import { MenuItem, Message as MessageError, MessageService, SelectItem } from 'primeng/api';
import { Observable, Subject, Subscription, map, mergeMap, mergeWith, tap } from 'rxjs';
import { WorkEffortTypeService } from 'app/api/service/work-effort-type.service';
import { StatusItemService } from 'app/api/service/status-item.service';
import { PartyRoleService } from 'app/api/service/party-role.service';
import { WorkEffortType } from 'app/view/report-print/report';
import { StatusItem } from 'app/api/model/statusItem';
import { StatusItemExType } from 'app/api/model/statusItemExType';
import { PartyRoleEx } from 'app/api/model/partyRoleEx';
import { DateMoreThanValidator } from 'app/commons/validators/custom-validator';

@Component({
  selector: 'app-details-goals',
  templateUrl: './details-goals.component.html',
  styleUrls: ['./details-goals.component.css'],
  styles: [`
  app-details-goals {
    width: 100%;
  }
`]
})
export class DetailsGoalsComponent implements OnInit, OnDestroy, AfterViewInit, AfterContentChecked {

  _reload: Subject<void>;
  messagesError: MessageError[] = [];
  title: string;
  titleWE: string;
  titleGL: string;

  form: FormGroup;
  workEffort: WorkEffort;
  workEffortId: string;
  workEffortTypeId: string;

  dropdownOrgId: SelectItem[] = [];
  dropdownWEP: SelectItem[] = [];
  dropdownWorkEffortTypePeriod: SelectItem[] = [];
  dropdownWorkEffortTypePurpose: SelectItem[] = [];
  dropdownIsPosted: SelectItem[] = [];
  dropdownEnum: SelectItem[] = [];
  dropdownWorkEffortAssocType: SelectItem[] = [];

  dropdownActualStatus: SelectItem[] = [];
  dropdownOrgUnit: SelectItem[] = [];
  dropdownWorkEffortType: SelectItem[] = [];

  backLink = '../../'
  secondaryLang;
  BILING: boolean;

  activeIndex: number[] = [0, 1];
  details: CardDetail[] = [
    { title: "ID", description: "" },
    { title: "Work Effort", description: "" }
  ];

  dropdownOrgId$: Subscription;
  dropdownWEP$: Subscription;
  dropdownWorkEffortTypePeriod$: Subscription;
  dropdownWorkEffortTypePurpose$: Subscription;
  dropdownEnum$: Subscription;
  workEffort$: Subscription;
  dropdownActualStatus$: Subscription;
  dropdownOrgUnit$: Subscription;
  dropdownWorkEffortType$: Subscription;
  dropdownWorkEffortAssocType$: Subscription;

  constructor(
    private route: ActivatedRoute,
    private readonly workEffortService: WorkEffortService,
    private readonly i18nService: I18NService,
    private readonly partyService: PartyService,
    private formBuilder: FormBuilder,
    private readonly workEffortTypePeriodService: WorkEffortTypePeriodService,
    private readonly workEffortTypePurposeService: WorkEffortPurposeTypeService,
    private readonly enumService: EnumerationService,
    private readonly loaderService: LoaderService,
    private readonly workEffortAssocTypeService: WorkEffortAssocTypeService,
    private msgService: MsgService,
    private toolbarService: ToolbarService,
    private readonly confirmDialogService: ConfirmDialogService,
    private readonly workEffortTypeService: WorkEffortTypeService,
    private readonly statusItemService: StatusItemService,
    private readonly partyRoleService: PartyRoleService,
    private readonly router: Router,
    private _location: Location,
    private cd: ChangeDetectorRef
  ) {
    this.toolbarService.setDefaultFormComponentButton();
    this._reload = new Subject<void>();
    this.BILING = this.i18nService.getLanguageType() == "BILING";

  }

  ngAfterContentChecked(): void {
    this.cd.detectChanges();
  }

  ngAfterViewInit(): void {
    this.form.valueChanges.subscribe(x => {
      if (x) this.toolbarService.setDisabledSave(false)
    });
  }

  ngOnDestroy(): void {
    this.dropdownOrgId$.unsubscribe();
    this.dropdownWEP$.unsubscribe();
    this.dropdownWorkEffortTypePeriod$.unsubscribe();
    this.dropdownWorkEffortTypePurpose$.unsubscribe();
    this.dropdownEnum$.unsubscribe();
    this.workEffort$.unsubscribe();
    this.dropdownActualStatus$.unsubscribe();
    this.dropdownOrgUnit$.unsubscribe();
    this.dropdownWorkEffortType$.unsubscribe();
    this.dropdownWorkEffortAssocType$.unsubscribe();
  }

  ngOnInit(): void {
    this.loaderService.hide();
    this.form = this.formBuilder.group({

      sourceReferenceId: new FormControl<string>(null, Validators.required),
      workEffortTypeId: new FormControl<string>(null, Validators.required),
      currentStatusId: new FormControl<string>(null, Validators.required),
      estimatedStartDate: new FormControl<Date>(null),
      estimatedCompletionDate: new FormControl<Date>(null, DateMoreThanValidator('estimatedStartDate')),
      orgUnitId: new FormControl<string>(null, Validators.required),
      organizationId: new FormControl<string>(null, Validators.required),
      workEffortParentId: new FormControl<string>(null, Validators.required),
      workEffortTypePeriodId: new FormControl<string>(null),
      workEffortPurposeTypeId: new FormControl<string>(null),
      etch: new FormControl<string>(null),
      workEffortName: new FormControl<string>(null, Validators.maxLength(2000)),
      workEffortNameLang: new FormControl<string>(null, Validators.maxLength(2000)),
      lastStatusUpdate: new FormControl<Date>(null, Validators.required),
      scheduledStartDate: new FormControl<Date>(null),
      scheduledCompletionDate: new FormControl<Date>(null),
      actualStartDate: new FormControl<Date>(null),
      actualCompletionDate: new FormControl<Date>(null),
      dataSoll: new FormControl<Date>(null),
      isPosted: new FormControl<string>(null),
      specialTerms: new FormControl<string>(null, Validators.maxLength(2000)),
      weightKpi: new FormControl<number>(null),
      totalEnumIdKpi: new FormControl<string>(null),
      weightSons: new FormControl<number>(null),
      totalEnumIdSons: new FormControl<string>(null),
      weightAssocWorkEffort: new FormControl<number>(null),
      totalEnumIdAssoc: new FormControl<string>(null),
      workEffortAssocTypeId: new FormControl<string>(null),
      lastCorrectScoreDate: new FormControl<Date>(null),

    });
    

    this.workEffortId = this.route.snapshot.parent.params.id;      
    
    this.addDetail({ title: "ID", description: this.workEffortId })
    this.secondaryLang = this.i18nService.getIsSecondaryLang();

    this.dropdownIsPosted.push({ label: "S", value: "Y" }, { label: "N", value: "N" })
    const reload = this._reload.pipe(mergeMap(() => this.workEffortService.getWorkEffort(this.workEffortId)));
    const we$: Observable<WorkEffort> = this.route.data.pipe(
      map((data: { obss: WorkEffort }) => data.obss),
      mergeWith(reload)
    );

    this.workEffort$ = we$.pipe(
      tap((x) => {
        this.workEffort = x;
        this.workEffortTypeId = x.workEffortTypeId,
          this.addDetail({ title: "Work Effort", description: ((x.etch) ? x.etch + " - " : "") + ((!this.secondaryLang) ? x.workEffortName : x.workEffortNameLang) });
        this.form.patchValue({
          organizationId: x.organizationId,
          sourceReferenceId: x.sourceReferenceId,
          workEffortTypeId: x.workEffortTypeId,
          currentStatusId: x.currentStatusId,
          estimatedStartDate: x.estimatedStartDate ? new Date(x.estimatedStartDate) : null,
          estimatedCompletionDate: x.estimatedCompletionDate ? new Date(x.estimatedCompletionDate) : null,
          orgUnitId: x.orgUnitId,
          workEffortParentId: x.workEffortParentId,
          workEffortTypePeriodId: x.workEffortTypePeriodId,
          workEffortPurposeTypeId: x.workEffortPurposeTypeId,
          etch: x.etch,
          workEffortName: x.workEffortName,
          workEffortNameLang: x.workEffortNameLang,
          lastStatusUpdate: x.lastStatusUpdate ? new Date(x.lastStatusUpdate) : null,
          scheduledStartDate: x.scheduledStartDate ? new Date(x.scheduledStartDate) : null,
          scheduledCompletionDate: x.scheduledCompletionDate ? new Date(x.scheduledCompletionDate) : null,
          actualStartDate: x.actualStartDate ? new Date(x.actualStartDate) : null,
          actualCompletionDate: x.actualCompletionDate ? new Date(x.actualCompletionDate) : null,
          dataSoll: x.dataSoll ? new Date(x.dataSoll) : null,
          isPosted: x.isPosted,
          specialTerms: x.specialTerms,
          weightKpi: x.weightKpi,
          totalEnumIdKpi: x.totalEnumIdKpi,
          weightSons: x.weightSons,
          totalEnumIdSons: x.totalEnumIdSons,
          weightAssocWorkEffort: x.weightAssocWorkEffort,
          totalEnumIdAssoc: x.totalEnumIdAssoc,
          workEffortAssocTypeId: x.workEffortAssocTypeId,
          lastCorrectScoreDate: x.lastCorrectScoreDate? new Date(x.lastCorrectScoreDate): null
        })
      })
    ).subscribe(() => {
      this.toolbarService.setDisabledSave(true)
    });

    this.dropdownOrgId$ = this.partyService.getPartyByOrgId().pipe(
      map(x => this.dropdownCreateOrgId(x))
    ).subscribe(dropdown => this.dropdownOrgId = dropdown);

    this.dropdownWEP$ = this.workEffortService.WorkEffortByOrgId().pipe(
      map(x => this.dropdownCreateWEP(x))
    ).subscribe(dropdown => this.dropdownWEP = dropdown);

    this.dropdownWorkEffortTypePeriod$ = this.workEffortTypePeriodService.getWorkEffortTypePeriodByWorkEffortTypeId(this.workEffortTypeId).pipe(
      map(x => this.dropdownCreateWETP(x))
    ).subscribe(dropdown => this.dropdownWorkEffortTypePeriod = dropdown);

    this.dropdownWorkEffortTypePurpose$ = this.workEffortTypePurposeService.getWorkEffortPurposeTypeByPurposeTypeEnumId("PT_INDICATOR").pipe(
      map(x => this.dropdownCreateWEPT(x))
    ).subscribe(dropdown => this.dropdownWorkEffortTypePurpose = dropdown);

    this.dropdownEnum$ = this.enumServiceEnumeratios('TOTAL').subscribe(dropdown => this.dropdownEnum = dropdown);

    this.dropdownWorkEffortAssocType$ = this.workEffortAssocTypeService.getWorkEffortAssocType().pipe(
      map(x => this.dropdownCreateWEAT(x))
    ).subscribe(dropdown => this.dropdownWorkEffortAssocType = dropdown);

    this.dropdownWorkEffortType$ = this.workEffortTypeService.getAllWorkEffortTypes().pipe(
      map(x => this.dropdownCreateWET(x))
    ).subscribe(dropdown => this.dropdownWorkEffortType = dropdown);

    this.dropdownOrgUnit$ = this.partyRoleService.getPartyRoleOrgId().pipe(
      map(x => this.dropdownCreatePR(x))
    ).subscribe(dropdown => this.dropdownOrgUnit = dropdown);

    this.dropdownActualStatus$ = this.statusItemService.getStatusItemStateTo().pipe(
      map(x => this.dropdownCreateSI(x))
    ).subscribe(dropdown => this.dropdownActualStatus = dropdown);
  }

  dropdownCreateOrgId(types: Party[]): SelectItem[] {
    if (types) {
      return types.map((partyItem: Party) => { return { label: this.secondaryLang ? partyItem.partyNameLang : partyItem.partyName, value: partyItem.partyId } });
    }
    return []
  }

  dropdownCreateWEP(types: WorkEffort[]): SelectItem[] {
    if (types) {
      return types.map((workEffortItem: WorkEffort) => { return { label: this.secondaryLang ? workEffortItem.workEffortNameLang : workEffortItem.workEffortName, value: workEffortItem.workEffortId } });
    }
    return []
  }

  dropdownCreateWETP(types: WorkEffortTypePeriod[]): SelectItem[] {
    if (types) {
      return types.map((workEffortTypePeriodItem: WorkEffortTypePeriod) => { return { label: workEffortTypePeriodItem.desProc, value: workEffortTypePeriodItem.workEffortTypePeriodId } });
    }
    return []
  }

  dropdownCreateWEPT(types: WorkEffortPurposeType[]): SelectItem[] {
    if (types) {
      return types.map((workEffortPurposeTypeItem: WorkEffortPurposeType) => { return { label: this.secondaryLang ? workEffortPurposeTypeItem.descriptionLang : workEffortPurposeTypeItem.description, value: workEffortPurposeTypeItem.workEffortPurposeTypeId } });
    }
    return []
  }

  enumServiceEnumeratios(enumTypeId: string): Observable<SelectItem[]> {
    return this.enumService.enumerations(enumTypeId).pipe(
      map(x => this.dropdownEnumeration(x))
    )
  }

  dropdownEnumeration(types: Enumeration[]): SelectItem[] {
    if (types) {
      return types.map((enumItem: Enumeration) => { return { label: ((!this.secondaryLang) ? enumItem.description : enumItem.descriptionLang), value: enumItem.enumId } });
    }
    return []
  }

  dropdownCreateWEAT(types: WorkEffortAssocType[]): SelectItem[] {
    if (types) {
      return types.map((workEffortAssocTypeItem: WorkEffortAssocType) => { return { label: workEffortAssocTypeItem.description, value: workEffortAssocTypeItem.workEffortAssocTypeId } });
    }
    return []
  }

  dropdownCreateWET(types: WorkEffortType[]): SelectItem[] {
    if (types) {
      return types.map((workEffortType: WorkEffortType) => { return { label: this.secondaryLang ? workEffortType.descriptionLang : workEffortType.description, value: workEffortType.workEffortTypeId } });
    }
    return []
  }

  dropdownCreateSI(types: StatusItemExType[]): SelectItem[] {
    if (types) {
      return types.map((statusItem: StatusItemExType) => { return { label: this.secondaryLang ? statusItem.statusType.description + "-" + statusItem.descriptionLang : statusItem.statusType.description + "-" + statusItem.description, value: statusItem.statusId } });
    }
    return []
  }

  dropdownCreatePR(types: PartyRoleEx[]): SelectItem[] {
    if (types) {
      return types.map((x: PartyRoleEx) => { return { label: this.secondaryLang ? x.partyParentRole.parentRoleCode + "-" + x.party.partyNameLang : x.partyParentRole.parentRoleCode + "-" + x.party.partyName, value: x.partyId } });
    }
    return []
  }

  addDetail(detail: CardDetail) {
    let index = this.details.findIndex(x => x.title == detail.title);    
    this.details[index] = detail;
  }

  save() {

    if (this.form.valid) {
      this.workEffort = Object.assign(this.workEffort, this.form.value);
      this.workEffortService.updateWorkEffort(this.workEffort)
        .then(() => {
          this.toolbarService.setDisabledSave(true);
          this._reload.next();
          this.msgService.successUpdate();

        })
        .catch((err) => {
          this.msgService.error(err);
        })
    }
    else {
      this.msgService.errorFieldsRequired();
    }
  }

  delete() {
    this.confirmDialogService.delete().then(
      x => {
        if (x) {
          this.workEffortService.deleteWorkEffortTree(this.workEffortId)
            .then(() => {
              this.msgService.successDelete();
              setTimeout(() => this._location.back(), 2000);
            })
            .catch((error) => {
              this.msgService.error(error.message)
              this._reload.next();
            });
        }
      }
    )
  }


}
