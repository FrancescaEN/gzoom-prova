import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GlAccountOrganization } from 'app/api/model/glAccountOrganization';
import { GlAccountOrganizationService } from 'app/api/service/gl-account-organization.service';
import { I18NService } from 'app/i18n/i18n.service';
import { BehaviorSubject, Observable, Subject, combineLatest, map, switchMap, takeUntil, tap } from 'rxjs';
import { ButtonService } from '../../../../../commons/service/button.service';
import { PartyService } from 'app/api/service/party.service';
import { Party } from 'app/api/model/party';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { MsgService } from 'app/commons/service/message.service';
import { UomRatingScaleService } from 'app/api/service/uom-rating-scale.service';
import { GlAccountMeasRatScService } from 'app/api/service/gl-account-meas-rat-sc.service';
import { GlAccountMeasRatSc } from 'app/api/model/glAccountMeasRatSc';
import { UomRatingScale } from 'app/api/model/uomRatingScale';
import { NewValueComponent } from './new-value/new-value.component';
import { doNothing } from 'app/commons/utils/doNothing';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { Table } from 'primeng/table';


interface GlAccountMeasRatScView {
  glAccountId: string,
  uomId: string,
  uomRatingValue: number,
  uomRatingValueDescription: string,
  uomCode: string,
  uomCodeLang: string,
  uomDescr: string,
  uomDescrLang: string
}

@Component({
  selector: 'gzoom-indicator-tab-value-list',
  templateUrl: './indicator-tab-value-list.component.html',
  styleUrls: ['./indicator-tab-value-list.component.scss'],
})
export default class IndicatorTabValueListComponent implements OnInit, OnDestroy {
  glAccountId: string;
  private destroy$ = new Subject<void>();
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  BILING: boolean = this.i18nService.getLanguageType() == "BILING";
  scrollable: Observable<boolean> = this.scrollableTabService.isScrollableWidth(960);
  disableSave: boolean = true;
  loading: boolean = true;

  glAccountMeasRatSc: GlAccountMeasRatSc[] = [];
  glAccountMeasRatScView: GlAccountMeasRatScView[] = [];
  updatedGlAccountMeasRatSc: { uomId: string, uomRatingValue: number }[] = [];
  uomRatingScale: UomRatingScale[] = [];

  selectedItems: GlAccountMeasRatScView[] = [];
  updatedItems: string[] = [];
  ref: DynamicDialogRef = new DynamicDialogRef();
  @ViewChild("dt") private dataTable: Table;

  constructor(private router: Router,
    private route: ActivatedRoute,
    private i18nService: I18NService,
    private glAccountMeasRatScService: GlAccountMeasRatScService,
    private uomRatingScaleService: UomRatingScaleService,
    private buttonService: ButtonService,
    private partyService: PartyService,
    public dialogService: DialogService,
    private scrollableTabService: ScrollableTabService,
    private msgService: MsgService,
    private confirmDialogService: ConfirmDialogService
  ) {
    this.buttonService.showSaveButton(false);
    this.glAccountId = this.route.parent.snapshot.paramMap.get('glAccountId');
  }


  ngOnInit(): void {

    this.reload$.pipe(
      takeUntil(this.destroy$),
      tap(() => {
        this.loading = true;
        this.disableSave = true;
      }),
      switchMap(() =>
        combineLatest({
          glAccountMeasRatScList: this.glAccountMeasRatScService.getByGlAccountId(this.glAccountId),
          uomRatingScaleList: this.uomRatingScaleService.getByGlAccountIdOnGlAccountMeasRatSc(this.glAccountId)
        })),
      map(({ glAccountMeasRatScList, uomRatingScaleList }) => {
        this.glAccountMeasRatSc = glAccountMeasRatScList;
        this.uomRatingScale = uomRatingScaleList;
        let tmp: GlAccountMeasRatScView[] = [];
        glAccountMeasRatScList.forEach(item => {
          let uomSelected = this.uomRatingScale.find(x => x.uomRatingValue === item.uomRatingValue)
          tmp.push({
            glAccountId: this.glAccountId,
            uomId: item.uomId,
            uomRatingValue: item.uomRatingValue,
            uomRatingValueDescription: (this.secondaryLang ? uomSelected.descriptionLang : uomSelected.description),
            uomCode: item.uomCode,
            uomCodeLang: item.uomCodeLang,
            uomDescr: item.uomDescr,
            uomDescrLang: item.uomDescrLang,
          })
        });

        return tmp;
      })
    )
      .subscribe(values => { this.glAccountMeasRatScView = values; this.loading = false; });
  }


  newValue() {
    this.ref = this.dialogService.open(NewValueComponent, {
      header: this.i18nService.translate('Select value'),

      styleClass: 'min-width p-dialog-width',
      contentStyle: { overflow: 'auto' },
      data: {
        glAccountId: this.glAccountId,

      }
    });

    this.ref.onClose.subscribe((str: string) => {
      if (str === 'reload') {
        this._reload.next();
      }
    });

  }

  changeValue(gamrs: GlAccountMeasRatScView) {
    const { uomId, uomRatingValue } = gamrs;
    if (!this.updatedGlAccountMeasRatSc.find(x => x.uomId === uomId && x.uomRatingValue === uomRatingValue)) this.updatedGlAccountMeasRatSc.push({ uomId, uomRatingValue });
    this.disableSave = false;
  }

  reset() {
    this.dataTable.editingCell = null;
    this._reload.next();
  }

  doNothing(e: any) {
    doNothing(e);
  }

  save() {
    const updated = this.updatedGlAccountMeasRatSc.map(x => this.glAccountMeasRatScView.find(y => x.uomId === y.uomId && x.uomRatingValue === y.uomRatingValue));
    let glAccountMeasRatSc: GlAccountMeasRatSc[] = [];
    updated.forEach(item => {
      const newItem: GlAccountMeasRatSc = { ...item }
      glAccountMeasRatSc.push(newItem);
    })

    const completedElements = glAccountMeasRatSc.filter(x => {
      const { uomCode, uomCodeLang, uomDescr, uomDescrLang } = x;
      return uomCode && uomDescr && (!this.BILING || (uomCodeLang && uomDescrLang));
    }).length;

    if (completedElements === glAccountMeasRatSc.length) {
      this.glAccountMeasRatScService.updateGlAccountMeasRatSc(glAccountMeasRatSc)
        .pipe(takeUntil(this.destroy$))
        .subscribe(
          {
            next: () => {
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

  delete() {


    this.confirmDialogService.deleteMultiElement().then(x => {

      if (x) {
        this.glAccountMeasRatScService.deleteGlAccountMeasRatSc(this.selectedItems)
          .subscribe(
            {
              next: (value) => {
                this.msgService.successDelete();
                this.selectedItems = []
                if (value) this._reload.next();
              },
              error: (error) => {
                this.msgService.error(error.error.message);
              }
            }
          )
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
  }
}
