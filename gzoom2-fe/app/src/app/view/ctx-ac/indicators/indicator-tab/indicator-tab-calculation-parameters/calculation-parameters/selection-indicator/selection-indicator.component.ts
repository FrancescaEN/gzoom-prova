import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { GlAccount } from 'app/api/model/glAccount';
import { GlAccountType } from 'app/api/model/glAccountType';
import { GlAccountTypeService } from 'app/api/service/gl-account-type.service';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { WorkEffortPurposeTypeService } from 'app/api/service/work-effort-purpose-type.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { dropdownGlAccountType, dropdownWorkEffortPurposeType } from 'app/commons/utils/dropdownSelectItem';
import { I18NService } from 'app/i18n/i18n.service';
import { isEmpty, isNull, omitBy } from 'lodash';
import { FilterMatchMode, SelectItem } from 'primeng/api';
import { BehaviorSubject, Observable, Subject, Subscription, map, switchMap, takeUntil, tap } from 'rxjs';

@Component({
  selector: 'gzoom-selection-indicator',
  templateUrl: './selection-indicator.component.html',
  styleUrls: ['./selection-indicator.component.scss']
})
export class SelectionIndicatorComponent implements OnInit, OnDestroy {
  @Input() selectedIndicator!: GlAccount;
  @Output() selectedIndicatorChange: EventEmitter<GlAccount> = new EventEmitter<GlAccount>();

  private destroy$ = new Subject<void>();
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  glAccountType: SelectItem[];
  workEffortPurposeType: SelectItem[];
  indicators: GlAccount[] = [];
  loading: boolean = true;
  scrollable: Observable<boolean> = this.scrollableTabService.isScrollableWidth(960);
  disableResetFilter: boolean = true;
  filterForm: FormGroup = this.formBuilder.group({
    matchModeSearch: new FormControl<string>(FilterMatchMode.CONTAINS, { nonNullable: true }),
    search: new FormControl<string>(null),
    glAccountTypeId: new FormControl<string>(null),
    workEffortPurposeTypeId: new FormControl<string[]>(null),
  });

  get search() { return this.filterForm.get('search') }
  get matchModeSearch() { return this.filterForm.get('matchModeSearch') }
  get workEffortPurposeTypeId() { return this.filterForm.get('workEffortPurposeTypeId') }
  get glAccountTypeId() { return this.filterForm.get('glAccountTypeId') }

  constructor(
    private route: ActivatedRoute,
    private readonly router: Router,
    private readonly i18nService: I18NService,
    private glAccountService: GlAccountService,
    private glAccountTypeService: GlAccountTypeService,
    private formBuilder: FormBuilder,
    private workEffortPurposeTypeService: WorkEffortPurposeTypeService,
    private scrollableTabService: ScrollableTabService
  ) { }

  ngOnInit(): void {
    this.reload$.pipe(
      tap(() => {
        this.loading = true;
        const { matchModeSearch, isSecondaryLang, ...values } = omitBy(this.filterForm.value, isNull);
        this.disableResetFilter = isEmpty(values)
      }),
      switchMap(() => this.glAccountService.getGlAccountByFilterParams({ ...omitBy(this.filterForm.value, isNull), isSecondaryLang: this.secondaryLang }))
    ).subscribe(values => {
      this.filterForm.markAsPristine();
      this.indicators = values;
      this.loading = false;
    })

    this.glAccountTypeService.getGlAccountTypeList().pipe(
      takeUntil(this.destroy$),
      map(x => dropdownGlAccountType(x, this.secondaryLang)),
    ).subscribe(x => this.glAccountType = x);

    this.workEffortPurposeTypeService.getWorkEffortPurposeTypeByPurposeTypeEnumId('PT_INDICATOR')
      .pipe(
        takeUntil(this.destroy$),
        map(x => dropdownWorkEffortPurposeType(x, this.secondaryLang))
      ).subscribe(x => this.workEffortPurposeType = x);
  }

  getGlAccountTypeDescription(glAccountTypeId: string): string {
    let glAccountType = this.glAccountType.find(x => x.value === glAccountTypeId);
    return glAccountType ? glAccountType.label : glAccountTypeId;
  }

  filter() {
    this._reload.next();
  }

  resetFilter() {
    this.filterForm.reset();
    this.filter();

  }


  emitSelect() {
    this.selectedIndicatorChange.emit(this.selectedIndicator);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
  }
}