import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { BehaviorSubject, Observable, Subject, map, switchMap, takeUntil, tap } from 'rxjs';
import { ButtonService } from '../../../../../../commons/service/button.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';

import { SelectItem } from 'primeng/api';
import { dropdownGlFiscalType } from 'app/commons/utils/dropdownSelectItem';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { GlAccountInputCalcService } from 'app/api/service/gl-account-input-calc.service';
import { GlAccountInputCalc } from 'app/api/model/glAccountInputCalc';
import { GlFiscalTypeService } from 'app/api/service/gl-fiscal-type.service';
import { NewParameterComponent } from './new-parameter/new-parameter.component';
import { UpdateOperandComponent } from './update-operand/update-operand.component';
import { MsgService } from 'app/commons/service/message.service';



@Component({
  selector: 'gzoom-calculation-parameters',
  templateUrl: './calculation-parameters.component.html',
  styleUrls: ['./calculation-parameters.component.scss']
})
export class CalculationParametersComponent implements OnInit, OnDestroy {

  @Input() reloadData$: Observable<void>;

  private destroy$ = new Subject<void>();
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  scrollable: Observable<boolean> = this.scrollableTabService.isScrollableWidth(960);
  @Input() disableSave: boolean = true;
  @Output() disableSaveChange = new EventEmitter();
  loading: boolean = false;
  glAccountInputCalc: GlAccountInputCalc[] = [];
  glAccountId: string;
  glFiscalType: SelectItem[] = [];
  updatedGlAccountInputCalc: string[] = [];

  selectedItems: GlAccountInputCalc[] = [];


  ref: DynamicDialogRef = new DynamicDialogRef();
  refDialogUpdateOperand: DynamicDialogRef = new DynamicDialogRef();

  constructor(private router: Router,
    private route: ActivatedRoute,
    private i18nService: I18NService,
    private buttonService: ButtonService,
    public dialogService: DialogService,
    private scrollableTabService: ScrollableTabService,
    private confirmDialogService: ConfirmDialogService,
    private glAccountInputCalcService: GlAccountInputCalcService,
    private glAccountService: GlAccountService,
    private glFiscalTypeService: GlFiscalTypeService,
    private msgService: MsgService,

  ) {
    this.buttonService.showSaveButton(false);

    this.glAccountId = this.route.parent.snapshot.paramMap.get('glAccountId');
  }

  ngOnInit(): void {

    this.reloadData$.subscribe(x => this._reload.next())

    this.reload$.pipe(
      tap(() => {
        this.loading = true;
        this.disableSave = true;
        this.disableSaveChange.emit(true)
      }),
      switchMap(() => this.glAccountInputCalcService.getGlAccountInputCalcByGlAccountId(this.glAccountId)),
      takeUntil(this.destroy$),
      map(x => {
        x.forEach(y => {
          if (y.glAccountRef) {
            const accountName = y.glAccountRef?.accountName,
              accountNameLang = y.glAccountRef?.accountNameLang,
              accountCode = y.glAccountRef?.accountCode;
            y.glAccountRef.accountName =
              `${accountCode} - ${(this.secondaryLang
                ? accountNameLang
                : accountName)}`;

          }

        })
        return x;
      })
    )
      .subscribe(values => { this.glAccountInputCalc = values; this.loading = false; });

    this.glFiscalTypeService.getGlFiscalType()
      .pipe(
        takeUntil(this.destroy$),
        map(x => dropdownGlFiscalType(x, this.secondaryLang))).subscribe(values => this.glFiscalType = values)


  }

  getGlFiscalTypeDescr(code) {
    return code ? this.glFiscalType.find(x => x.value == code)?.label : code;
  }

  changeValue(glAccountInputCalcId: string) {

    if (this.updatedGlAccountInputCalc.indexOf(glAccountInputCalcId) === -1) this.updatedGlAccountInputCalc.push(glAccountInputCalcId);
    this.disableSave = false;
    this.disableSaveChange.emit(false);
  }

  changeOperand(glAccountInputCalc: GlAccountInputCalc) {
    this.refDialogUpdateOperand = this.dialogService.open(UpdateOperandComponent, {
      header: this.i18nService.translate('Select indicator'),

      styleClass: 'min-width p-dialog-width',
      contentStyle: { overflow: 'auto' },
      data: {
        glAccountIdRef: glAccountInputCalc.glAccountIdRef,
        glAccountInputCalcId: glAccountInputCalc.glAccountInputCalcId,
      }
    });

    this.refDialogUpdateOperand.onClose.subscribe(x => {
      if (x === "reload")
        this._reload.next()
    });
  }

  newParameter() {
    this.ref = this.dialogService.open(NewParameterComponent, {
      header: this.i18nService.translate('Select indicator'),

      styleClass: 'min-width p-dialog-width',
      contentStyle: { overflow: 'auto' },
      data: {
        glAccountId: this.glAccountId,
        glFiscalType: this.glFiscalType
      }
    });

    this.ref.onClose.subscribe((newItem: GlAccountInputCalc) => {
      if (newItem) {
        this._reload.next();
      }
    });
  }


  save() {
    const updatedItems = this.glAccountInputCalc.filter(x => this.updatedGlAccountInputCalc.indexOf(x.glAccountInputCalcId) != -1);

    this.glAccountInputCalcService.update(updatedItems)
      .subscribe({
        next: (value) => {
          this.msgService.successUpdate();
          this._reload.next();
        },
        error: (error) => {
          this.msgService.error(error.error.message ?? error);
          console.log(error)
        }
      })

  }


  reset() {
    this._reload.next();
  }


  doNothing(e: any) {
    e.stopPropagation();
    return;
  }

  delete() {
    this.confirmDialogService.deleteMultiElement().then(x => {
      if (x) {
        this.glAccountInputCalcService.delete(this.selectedItems.map(x => x.glAccountInputCalcId))
          .subscribe({
            next: (value) => {
              this.selectedItems = [];
              this.msgService.successDelete();

              this._reload.next();
            },
            error: (error) => {
              this.msgService.error(error.error.message ?? error);
              console.log(error)
            }
          })
      }
    })


  }

  canDeactivate(): boolean {
    return !this.disableSave;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
    this.ref?.close();
    this.refDialogUpdateOperand?.close();
  }

}
