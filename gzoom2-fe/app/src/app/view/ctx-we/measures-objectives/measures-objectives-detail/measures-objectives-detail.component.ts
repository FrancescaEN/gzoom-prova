import { AfterContentChecked, AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Enumeration } from 'app/api/model/enumeration';
import { PeriodType } from 'app/api/model/period-type';
import { WorkEffortMeasure } from 'app/api/model/workEffortMeasure';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { PeriodTypeService } from 'app/api/service/period-type.service';
import { WorkEffortMeasureService } from 'app/api/service/work-effort-measure.service';
import { WorkEffortService } from 'app/api/service/work-effort.service';
import { I18NService } from 'app/i18n/i18n.service';
import { CardDetail } from 'app/layout/gzoom-card-detail/gzoom-card-detail.component';
import { SelectItem } from 'primeng/api';
import { Observable, Subject, Subscription, combineLatest, map, mergeMap, mergeWith, switchMap, tap } from 'rxjs';
import { Location } from "@angular/common";
import { MsgService } from 'app/commons/service/message.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { DateMoreThanValidator } from 'app/commons/validators/custom-validator';
import { ToolbarService } from 'app/commons/service/toolbar.service';

@Component({
  selector: 'app-measures-objectives-detail',
  templateUrl: './measures-objectives-detail.component.html',
  styleUrls: ['./measures-objectives-detail.component.css']
})
export class MeasuresObjectivesDetailComponent implements OnInit, OnDestroy, AfterViewInit, AfterContentChecked {
  _reload: Subject<void>;

  title: string;
  titleWE: string;
  titleGL: string;

  form: FormGroup;
  workEffortMeasure: WorkEffortMeasure;
  workEffortMeasureId: string;

  dropdownWeMeasureType: SelectItem[] = [];
  dropdownPT: SelectItem[] = [];
  dropdownWeScoreConvEnum: SelectItem[] = [];
  dropdownWeWithoutPerf: SelectItem[] = [];
  dropdownWeScoreRangeEnum: SelectItem[] = [];

  details$: Subscription;
  form$: Subscription;
  workEffortMeasure$: Subscription;
  dropdownWeMeasureType$: Subscription;
  dropdownPT$: Subscription;
  dropdownWeScoreConvEnum$: Subscription;
  dropdownWeWithoutPerf$: Subscription;
  dropdownWeScoreRangeEnum$: Subscription;

  secondaryLang: boolean;
  backLink = '../../'

  activeIndex: number[] = [0, 1];
  details: CardDetail[] = [
    { title: "ID", description: "" },
    { title: "Work Effort", description: "" },
    { title: "Unit Cont./Extr.", description: "" },

  ];

  constructor(
    private route: ActivatedRoute,
    private readonly workEffortMeasureService: WorkEffortMeasureService,
    private readonly workEffortService: WorkEffortService,
    private readonly glAccountService: GlAccountService,
    private readonly i18nService: I18NService,
    private formBuilder: FormBuilder,
    private readonly enumService: EnumerationService,
    private readonly periodTypeService: PeriodTypeService,
    private readonly confirmDialogService: ConfirmDialogService,
    private _location: Location,
    private msgService: MsgService,
    private toolbarService: ToolbarService,
    private cd: ChangeDetectorRef

  ) {
    this.toolbarService.setDefaultFormComponentButton();
    this._reload = new Subject<void>();
    this.secondaryLang = this.i18nService.getIsSecondaryLang();
    this.workEffortMeasureId = this.route.parent.snapshot.params.workEffortMeasureId;
    
  }
  ngAfterContentChecked(): void {
    this.cd.detectChanges();
  }

  ngAfterViewInit(): void {

    this.form$ = this.form.valueChanges
      .subscribe(x => {
        if (!this.form.pristine) {
          this.toolbarService.setDisabledSave(false);
        }

      });

  }

  ngOnDestroy(): void {
    this.details$?.unsubscribe();
    this.workEffortMeasure$?.unsubscribe();
    this.dropdownWeMeasureType$?.unsubscribe();
    this.dropdownPT$?.unsubscribe();
    this.dropdownWeScoreConvEnum$?.unsubscribe();
    this.dropdownWeWithoutPerf$?.unsubscribe();
    this.dropdownWeScoreRangeEnum$?.unsubscribe();
    this.form$?.unsubscribe();
  }

  ngOnInit(): void {

    this.form = this.formBuilder.group({
      workEffortMeasureId: new FormControl<string>(null, Validators.required),
      fromDate: new FormControl<Date>(null, Validators.required),
      thruDate: new FormControl<Date>(null, [Validators.required, DateMoreThanValidator('fromDate')]),
      kpiScoreWeight: new FormControl<number>(null, Validators.required),
      kpiOtherWeight: new FormControl<number>(null, Validators.required),
      sequenceId: new FormControl<number>(null),
      uomDescr: new FormControl<string>(null, Validators.maxLength(2000)),
      uomDescrLang: new FormControl<string>(null, Validators.maxLength(2000)),
      comments: new FormControl<string>(null, Validators.maxLength(2000)),
      commentsLang: new FormControl<string>(null, Validators.maxLength(2000)),
      weMeasureTypeEnumId: new FormControl<string>(null, Validators.required),
      periodTypeId: new FormControl<string>(null, Validators.required),
      weScoreConvEnumId: new FormControl<string>(null, Validators.required),
      weWithoutPerf: new FormControl<string>(null, Validators.required),
      weScoreRangeEnumId: new FormControl<string>(null, Validators.required),
    });

    const reload = this._reload.pipe(mergeMap(() => this.workEffortMeasureService.getWorkEffortMeasure(this.workEffortMeasureId)));
    const wemrs$: Observable<WorkEffortMeasure> = this.route.data.pipe(
      map((data: { obss: WorkEffortMeasure }) => data.obss),
      mergeWith(reload)
    )
    
    this.addDetail({ title: "ID", description: this.workEffortMeasureId })
    this.details$ = wemrs$.pipe(
      switchMap(x =>
        combineLatest([
          this.workEffortService.getWorkEffort(x.workEffortId),
          this.glAccountService.getGlAccount(x.glAccountId)
        ])),
    ).subscribe(([w, g]) => {
      this.addDetail({ title: "Work Effort", description: ((w.etch) ? w.etch + " - " : "") + ((!this.secondaryLang) ? w.workEffortName : w.workEffortNameLang) });
      this.addDetail({ title: "Unit Cont./Extr.", description: ((g.accountCode) ? g.accountCode + " - " : "") + ((!this.secondaryLang) ? g.accountName : g.accountNameLang) });
    });

    this.workEffortMeasure$ = wemrs$.pipe(
      tap((x) => {
        this.workEffortMeasure = x;
        this.form.patchValue({
          workEffortMeasureId: x.workEffortMeasureId,
          fromDate: x.fromDate ? new Date(x.fromDate) : null,
          thruDate: x.thruDate ? new Date(x.thruDate) : null,
          kpiScoreWeight: x.kpiScoreWeight,
          kpiOtherWeight: x.kpiOtherWeight,
          sequenceId: x.sequenceId,
          uomDescr: x.uomDescr,
          uomDescrLang: x.uomDescrLang,
          comments: x.comments,
          commentsLang: x.commentsLang,
          weMeasureTypeEnumId: x.weMeasureTypeEnumId,
          periodTypeId: x.periodTypeId,
          weScoreConvEnumId: x.weScoreConvEnumId,
          weWithoutPerf: x.weWithoutPerf,
          weScoreRangeEnumId: x.weScoreRangeEnumId,
        });
        this.form.markAsPristine();
      })
    ).subscribe(() => this.toolbarService.setDisabledSave(true));

    this.dropdownPT$ = this.periodTypeService.periodTypes().pipe(
      map(this.dropdownPeriodType)
    ).subscribe(dropdown => this.dropdownPT = dropdown);
    this.dropdownWeMeasureType$ = this.enumServiceEnumeratios('WE_MEASURE_TYPE').subscribe(dropdown => this.dropdownWeMeasureType = dropdown);
    this.dropdownWeScoreConvEnum$ = this.enumServiceEnumeratios('WE_SCORE_CONVERSION').subscribe(dropdown => this.dropdownWeScoreConvEnum = dropdown);
    this.dropdownWeWithoutPerf$ = this.enumServiceEnumeratios('WE_WITHOUT_PERF').subscribe(dropdown => this.dropdownWeWithoutPerf = dropdown);
    this.dropdownWeScoreRangeEnum$ = this.enumServiceEnumeratios('WE_SCORE_RULE').subscribe(dropdown => this.dropdownWeScoreRangeEnum = dropdown);

  }

  addDetail(detail: CardDetail) {
    let index = this.details.findIndex(x => x.title == detail.title);
    this.details[index] = detail;
  }

  enumServiceEnumeratios(enumTypeId: string): Observable<SelectItem[]> {
    return this.enumService.enumerations(enumTypeId).pipe(
      map(x => this.dropdownEnum(x))
    )
  }

  dropdownEnum(types: Enumeration[]): SelectItem[] {
    if (types) {
      return types.map((enumItem: Enumeration) => { return { label: ((!this.secondaryLang) ? enumItem.description : enumItem.descriptionLang), value: enumItem.enumId } });
    }
    return []
  }

  dropdownPeriodType(types: PeriodType[]): SelectItem[] {
    if (types) {
      return types.map((pType: PeriodType) => { return { label: pType.description, value: pType.periodTypeId } });
    }
    return []
  }

  save() {
    if (this.form.valid) {
      this.workEffortMeasure = Object.assign(this.workEffortMeasure, this.form.value);
      this.workEffortMeasureService.updateWorkEffortMeasure(this.workEffortMeasure)
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
      if (this.form.controls['thruDate'].errors != null && this.form.controls['thruDate'].errors['date_more_than']) {

        this.msgService.errorDate(this.form.get('fromDate').value, this.form.get('thruDate').value);
      }
      else {
        this.msgService.errorFieldsRequired();
      }
    }
  }

  delete() {
    this.confirmDialogService.delete().then(
      x => {
        if (x) {
          this.workEffortMeasureService.deleteWorkEffortMeasure(this.workEffortMeasureId)
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

  canDeactivate(): boolean {
    return !this.form?.pristine.valueOf()
  }
}
