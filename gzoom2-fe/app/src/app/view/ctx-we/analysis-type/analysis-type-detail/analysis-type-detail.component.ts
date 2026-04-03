import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EnumerationService } from 'app/api/service/enumeration.service';
import { I18NService } from 'app/i18n/i18n.service';
import { CardDetail } from 'app/layout/gzoom-card-detail/gzoom-card-detail.component';
import { SelectItem } from 'primeng/api';
import { Observable, Subject, Subscription, map, mergeMap, mergeWith, switchMap, tap } from 'rxjs';
import { MsgService } from 'app/commons/service/message.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { ToolbarService } from 'app/commons/service/toolbar.service';
import { WorkEffortAnalysis } from 'app/api/model/workEffortAnalysis';
import { WorkEffortAnalysisService } from 'app/api/service/work-effort-analysis.service';
import { WorkEffortAnalysisEx } from 'app/api/model/workEffortAnalysisEx';
import { WorkEffortTypeService } from 'app/api/service/work-effort-type.service';
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import { dropdownEnumeration, dropdownGlFiscalType, dropdownWorkEffortType } from 'app/commons/utils/dropdownSelectItem';


@Component({
  selector: 'app-analysis-type-detail',
  templateUrl: './analysis-type-detail.component.html',
  styleUrls: ['./analysis-type-detail.component.css']
})
export class AnalysisTypeDetailComponent implements OnInit, OnDestroy, AfterViewInit {
  _reload: Subject<void>;

  form: FormGroup;
  workEffortAnalysis: WorkEffortAnalysis;
  workEffortAnalysisId: string;

  dropdownContext: SelectItem[] = [];
  dropdownAnalysis: SelectItem[] = [];
  dropdownAvailability: SelectItem[] = [];
  dropdownValidity: SelectItem[] = [
    { label: this.i18nService.translate("At date"), value: "A" },
    { label: this.i18nService.translate("In year"), value: "N" },
    { label: this.i18nService.translate("No control"), value: "Y" }
  ];
  dropdownTypeBalance: SelectItem[] = [];

  formChanges$: Subscription;
  contextChanges$: Subscription;
  descrChanges$: Subscription;
  details$: Subscription;
  workEffortAnalysis$: Subscription;
  dropdownContext$: Subscription;
  dropdownAnalysis$: Subscription;
  dropdownAvailability$: Subscription;
  dropdownTypeBalance$: Subscription;

  secondaryLang: boolean;
  BILING: boolean;

  activeIndex: number[] = [0, 1];
  details: CardDetail[] = [
    { title: "Code", description: "" },
    { title: "Description", description: "" },
  ];

  isNew: boolean = false;
  noConfirm: boolean = false;
  backLinkRelToThisRoute: string;
  dataContext: string;

  constructor(
    private route: ActivatedRoute,
    private readonly workEffortAnalysisService: WorkEffortAnalysisService,
    private readonly workEffortTypeService: WorkEffortTypeService,
    private readonly i18nService: I18NService,
    private formBuilder: FormBuilder,
    private readonly enumService: EnumerationService,
    private readonly router: Router,
    private readonly confirmDialogService: ConfirmDialogService,
    private msgService: MsgService,
    private toolbarService: ToolbarService,
    private glFiscalTypeService: GlFiscalTypeService,
  ) {
    this.toolbarService.setDefaultFormComponentButton();

    this._reload = new Subject<void>();
    this.secondaryLang = this.i18nService.getIsSecondaryLang();
    this.BILING = this.i18nService.getLanguageType() == "BILING";

    const ctx = this.route.snapshot.parent.data.context
    this.dataContext = (ctx != 'CTX_WE') ? ctx : null;

    if (this.route.snapshot.routeConfig.path == "add/new") {
      this.isNew = true
      this.backLinkRelToThisRoute = '../../'
    }
    else {
      this.backLinkRelToThisRoute = '../'
      this.toolbarService.setDisabledNew(false);
      this.workEffortAnalysisId = this.route.snapshot.params.workEffortAnalysisId;
    }
  }

  get context(): AbstractControl { return this.form.get('context') }
  get workEffortTypeId(): AbstractControl { return this.form.get('workEffortTypeId') }

  ngAfterViewInit(): void {
    this.formChanges$ = this.form.valueChanges
      .subscribe(x => {
        if (!this.form.pristine) {
          this.toolbarService.setDisabledSave(false);
        }

      });

    this.contextChanges$ = this.context?.valueChanges.pipe(

      switchMap(x => this.workEffortTypeService.getWorkEffortTypeByParentId(x)),
      map(x => dropdownWorkEffortType(x, this.secondaryLang))
    ).subscribe(dropdown => {
      this.workEffortTypeId.enable();
      if (!dropdown.find(x => x.value == this.workEffortTypeId.value))
        this.workEffortTypeId.setValue(null);
      this.dropdownAnalysis = dropdown;
    });


    this.descrChanges$ = this.form.get(this.secondaryLang ? "descriptionLang" : "description")?.valueChanges
      .subscribe(desc =>
        this.addDetail({ title: "Description", description: desc })
      )


  }

  ngOnDestroy(): void {
    this.formChanges$?.unsubscribe();
    this.details$?.unsubscribe();
    this.workEffortAnalysis$?.unsubscribe();
    this.dropdownAvailability$?.unsubscribe();
    this.dropdownTypeBalance$?.unsubscribe();
    this.contextChanges$?.unsubscribe();
    this.descrChanges$?.unsubscribe();
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      workEffortAnalysisId: new FormControl<string>(null, [Validators.required, Validators.maxLength(20)]),
      context: new FormControl<string>(this.dataContext, Validators.required),
      description: new FormControl<string>(null, [Validators.required, Validators.maxLength(255)]),
      descriptionLang: new FormControl<string>(null, this.BILING ? [Validators.required, Validators.maxLength(255)] : Validators.nullValidator),
      workEffortTypeId: new FormControl<string>(null, Validators.required),
      referenceDate: new FormControl<Date>(null, Validators.required),
      availabilityId: new FormControl<string>(null, Validators.required),
      excludeValidity: new FormControl<string>(null, Validators.required),
      typeBalanceScoreConId: new FormControl<string>(null, Validators.required),
      typeBalanceScoreTarId: new FormControl<string>(null),
      typeBalanceConsIndId: new FormControl<string>(null),
      typeBalanceTarIndId: new FormControl<string>(null),
      yearM4Prev: new FormControl<Date>(null),
      yearM3Prev: new FormControl<Date>(null),
      yearM2Prev: new FormControl<Date>(null),
      yearM1Prev: new FormControl<Date>(null),
      yearPrev: new FormControl<Date>(null),
      yearP1Prev: new FormControl<Date>(null),
      yearP2Prev: new FormControl<Date>(null),
      yearP3Prev: new FormControl<Date>(null),
      yearP4Prev: new FormControl<Date>(null),
      labelM4Prev: new FormControl<string>(null, Validators.maxLength(20)),
      labelM3Prev: new FormControl<string>(null, Validators.maxLength(20)),
      labelM2Prev: new FormControl<string>(null, Validators.maxLength(20)),
      labelM1Prev: new FormControl<string>(null, Validators.maxLength(20)),
      labelPrev: new FormControl<string>(null, Validators.maxLength(20)),
      labelP1Prev: new FormControl<string>(null, Validators.maxLength(20)),
      labelP2Prev: new FormControl<string>(null, Validators.maxLength(20)),
      labelP3Prev: new FormControl<string>(null, Validators.maxLength(20)),
      labelP4Prev: new FormControl<string>(null, Validators.maxLength(20))
    });
    if (!this.dataContext) this.workEffortTypeId.disable();

    const reload = this._reload.pipe(mergeMap(() => this.workEffortAnalysisService.getWorkEffortAnalysisExById(this.workEffortAnalysisId)));
    const wea$: Observable<WorkEffortAnalysisEx> = this.route.data.pipe(
      map((data: { obss: WorkEffortAnalysisEx }) => data.obss),
      mergeWith(reload)
    )


    if (!this.isNew) {


      this.addDetail({ title: "Code", description: this.workEffortAnalysisId });

      this.details$ = wea$.subscribe((x) => {
        this.addDetail({ title: "Description", description: (this.secondaryLang ? x.descriptionLang : x.description) });
      });

      this.workEffortAnalysis$ = wea$.pipe(
        tap((x) => {
          this.workEffortAnalysis = x;
          this.form.patchValue({
            workEffortAnalysisId: x.workEffortAnalysisId,
            description: x.description,
            descriptionLang: x.descriptionLang,
            workEffortTypeId: x.workEffortTypeId,
            referenceDate: x.referenceDate ? new Date(x.referenceDate) : null,
            availabilityId: x.availabilityId,
            excludeValidity: x.excludeValidity,
            typeBalanceScoreConId: x.typeBalanceScoreConId,
            typeBalanceScoreTarId: x.typeBalanceScoreTarId,
            typeBalanceConsIndId: x.typeBalanceConsIndId,
            typeBalanceTarIndId: x.typeBalanceTarIndId,
            yearM4Prev: x.yearM4Prev ? new Date(x.yearM4Prev) : null,
            yearM3Prev: x.yearM3Prev ? new Date(x.yearM3Prev) : null,
            yearM2Prev: x.yearM2Prev ? new Date(x.yearM2Prev) : null,
            yearM1Prev: x.yearM1Prev ? new Date(x.yearM1Prev) : null,
            yearPrev: x.yearPrev ? new Date(x.yearPrev) : null,
            yearP1Prev: x.yearP1Prev ? new Date(x.yearP1Prev) : null,
            yearP2Prev: x.yearP2Prev ? new Date(x.yearP2Prev) : null,
            yearP3Prev: x.yearP3Prev ? new Date(x.yearP3Prev) : null,
            yearP4Prev: x.yearP4Prev ? new Date(x.yearP4Prev) : null,
            labelM4Prev: x.labelM4Prev,
            labelM3Prev: x.labelM3Prev,
            labelM2Prev: x.labelM2Prev,
            labelM1Prev: x.labelM1Prev,
            labelPrev: x.labelPrev,
            labelP1Prev: x.labelP1Prev,
            labelP2Prev: x.labelP2Prev,
            labelP3Prev: x.labelP3Prev,
            labelP4Prev: x.labelP4Prev,
            context: x.parentWorkEffortType.workEffortTypeId
          });
          this.workEffortTypeId.enable();
          this.form.markAsPristine();
        })
      ).subscribe(() => {
        this.toolbarService.setDisabledSave(true);
      });

    }

    this.dropdownAnalysis$ = wea$.pipe(
      switchMap(x => this.workEffortTypeService.getWorkEffortTypeByParentId(this.dataContext ?? x.parentWorkEffortType.workEffortTypeId)),
      map(x => dropdownWorkEffortType(x, this.secondaryLang))
    ).subscribe(dropdown => this.dropdownAnalysis = dropdown);

    this.dropdownContext$ = this.workEffortTypeService.getLikeWorkEffortTypeId(this.dataContext ?? 'CTX%').pipe(
      map(x => dropdownWorkEffortType(x, this.secondaryLang))
    ).subscribe(dropdown => this.dropdownContext = dropdown);


    this.dropdownAvailability$ = this.enumService.enumerations('WEANAVAIL').pipe(
      map(x => dropdownEnumeration(x, this.secondaryLang))
    ).subscribe(dropdown => this.dropdownAvailability = dropdown);

    this.dropdownTypeBalance$ = this.glFiscalTypeService.getGlFiscalTypeByIsIndicatorUsed('Y').pipe(
      map(x => dropdownGlFiscalType(x, this.secondaryLang))
    ).subscribe(dropdown => this.dropdownTypeBalance = dropdown);

  }

  addDetail(detail: CardDetail) {
    let index = this.details.findIndex(x => x.title == detail.title);
    this.details[index] = detail;
  }

  create() {
    this.router.navigate(['../add/new'], { relativeTo: this.route })
  }

  save() {
    if (this.form.valid) {
      (this.isNew) ? this.insert() : this.update();
    }
    else {
      this.msgService.errorFieldsRequired();
    }
  }

  insert() {
    this.workEffortAnalysis = this.form.value;

    this.workEffortAnalysisService.createWorkEffortAnalysis(this.workEffortAnalysis)
      .then(() => {
        this.toolbarService.setDisabledSave(true);
        this.noConfirm = true;
        this.msgService.successCreate();
        setTimeout(() => this.router.navigate([this.backLinkRelToThisRoute + this.workEffortAnalysis.workEffortAnalysisId], { relativeTo: this.route }), 2000);
      })
      .catch((err) => {
        this.msgService.error(err);
      })
  }

  update() {
    this.workEffortAnalysis = Object.assign(this.workEffortAnalysis, this.form.value)
    this.workEffortAnalysisService.updateWorkEffortAnalysis(this.workEffortAnalysis)
      .then(() => {
        this.toolbarService.setDisabledSave(true);
        this.msgService.successUpdate();
        this._reload.next();

      })
      .catch((err) => {
        this.msgService.error(err);
      })
  }

  delete() {

    this.confirmDialogService.delete().then(
      x => {
        if (x) {
          if (this.isNew) {
            this.msgService.successDelete();
            this.noConfirm = true;
            setTimeout(() => this.router.navigate([this.backLinkRelToThisRoute], { relativeTo: this.route }), 2000);
          }
          else {
            this.workEffortAnalysisService.deleteWorkEffortAnalysis(this.workEffortAnalysisId)
              .then(() => {
                this.msgService.successDelete();
                setTimeout(() => this.router.navigate([this.backLinkRelToThisRoute], { relativeTo: this.route }), 2000);
              })
              .catch((error) => {
                this.msgService.error(error.message)
                this._reload.next();
              });
          }

        }
      }

    )

  }

  canDeactivate(): boolean {
    return this.isNew ? !this.noConfirm ?? !this.form?.pristine.valueOf() : !this.form?.pristine.valueOf()
  }
}
