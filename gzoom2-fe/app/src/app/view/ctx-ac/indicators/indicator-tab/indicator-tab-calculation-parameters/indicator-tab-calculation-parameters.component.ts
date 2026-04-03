import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, Observable, Subject, map, switchMap, takeUntil, tap } from 'rxjs';
import { ButtonService } from '../../../../../commons/service/button.service';
import { I18NService } from 'app/i18n/i18n.service';
import { DialogService } from 'primeng/dynamicdialog';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomMethodService } from 'app/api/service/custom-method.service';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { ConfirmationService, SelectItem } from 'primeng/api';
import { dropdownCustomMethod } from 'app/commons/utils/dropdownSelectItem';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'gzoom-indicator-tab-calculation-parameters',
  templateUrl: './indicator-tab-calculation-parameters.component.html',
  styleUrls: ['./indicator-tab-calculation-parameters.component.scss']
})
export class IndicatorTabCalculationParametersComponent implements OnInit, OnDestroy {
  activeIndex: number[] = [1];
  private destroy$ = new Subject<void>();
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  scrollable: Observable<boolean>;
  disableSave: boolean = true;
  loading: boolean = true;
  glAccountId: string;
  disableSaveComp: boolean = true;
  form: FormGroup = this.formBuilder.group({
    calcCustomMethodId: new FormControl<string>(null),
    prioCalc: new FormControl<number>(null, [Validators.maxLength(20)]),
  });
  customMethod$: Observable<SelectItem[]>;


  get calcCustomMethodId() { return this.form.get("calcCustomMethodId") }
  get prioCalc() { return this.form.get("prioCalc") }

  constructor(private router: Router,
    private route: ActivatedRoute,
    private i18nService: I18NService,
    private buttonService: ButtonService,
    public dialogService: DialogService,
    private scrollableTabService: ScrollableTabService,
    private confirmDialogService: ConfirmDialogService,
    private formBuilder: FormBuilder,
    private customMethodService: CustomMethodService,
    private glAccountService: GlAccountService,
    private msgService: MsgService,
    private confirmationService: ConfirmationService
  ) {
    this.buttonService.showSaveButton(false);
    this.glAccountId = this.route.parent.snapshot.paramMap.get('glAccountId');
  }

  ngOnInit(): void {
    this.reload$.pipe(
      switchMap(() => this.glAccountService.getGlAccount(this.glAccountId))
    )
      .pipe(
        takeUntil(this.destroy$),
        tap(glAccount => {
          if (glAccount.calcCustomMethodId) this.activeIndex = [0, 1];
          else this.activeIndex = [1];

          this.form.patchValue({
            calcCustomMethodId: glAccount.calcCustomMethodId,
            prioCalc: glAccount.prioCalc,
          })
          this.form.markAsPristine();
        })
      ).subscribe();

    this.customMethod$ = this.reload$.pipe(
      switchMap(() => this.customMethodService.getCustomMethodList())
    ).pipe(
      map(x => dropdownCustomMethod(x)),
    );
  }

  reset() {
    this._reload.next();
  }
  activeIndexChange(index) {
    this.activeIndex = index
  }
  updateCalculation() {
    if (this.activeIndex.indexOf(0) != -1 && this.calcCustomMethodId.value == null) {
      this.confirmDialogService.confirmClearCalcCustomMethod().then(x => {
        if (x) {
          this.saveUpdate();
        }
      }
      )
    }
    else this.saveUpdate();
  }

  saveUpdate() {
    if (this.form.valid) {
      this.glAccountService.updateCalcCustomMethodAndPrioCalc(this.glAccountId, this.calcCustomMethodId.value, this.prioCalc.value)
        .subscribe(
          {
            next: (x) => {
              this.msgService.successUpdate();
              this._reload.next();
            },
            error: (error) => {
              this.msgService.error(error.error.message)
            }
          }
        )

    }
    else {
      this.msgService.errorFieldsRequired();
    }
  }

  canDeactivate(): boolean {

    return !this.form?.pristine.valueOf() || !this.disableSaveComp
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
  }
}
