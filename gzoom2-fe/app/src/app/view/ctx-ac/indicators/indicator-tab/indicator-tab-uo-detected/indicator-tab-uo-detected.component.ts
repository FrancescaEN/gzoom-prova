import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GlAccountRoleService } from 'app/api/service/gl-account-role.service';
import { I18NService } from 'app/i18n/i18n.service';
import { ButtonService } from '../../../../../commons/service/button.service';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { MsgService } from 'app/commons/service/message.service';
import { BehaviorSubject, Observable, Subject, distinctUntilChanged, filter, map, of, switchMap, takeUntil, tap } from 'rxjs';
import { GlAccountRole } from 'app/api/model/glAccountRole';
import { NewUoDetectedComponent } from './new-uo-detected/new-uo-detected.component';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { Table } from 'primeng/table';

@Component({
  selector: 'gzoom-indicator-tab-uo-detected',
  templateUrl: './indicator-tab-uo-detected.component.html',
  styleUrls: ['./indicator-tab-uo-detected.component.scss']
})
export default class IndicatorTabUoDetectedComponent implements OnInit, OnDestroy {
  glAccountId: string;
  private destroy$ = new Subject<void>();
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  scrollable: Observable<boolean> = this.scrollableTabService.isScrollableWidth(960);
  loading: boolean = true;
  loadingSave: boolean = false;
  loadingDelete: boolean = false;
  glAccountRoleList: GlAccountRole[];
  selectedGlAccountRole: GlAccountRole[] = [];
  disableSave: boolean = true;
  updatedItems: GlAccountRole[] = [];

  ref: DynamicDialogRef = new DynamicDialogRef();
  isEnable: Observable<boolean>;
  @ViewChild("dt") private dataTable: Table;

  constructor(
    private glAccountRoleService: GlAccountRoleService,
    private route: ActivatedRoute,
    private i18nService: I18NService,
    private buttonService: ButtonService,
    private glAccountService: GlAccountService,
    public dialogService: DialogService,
    private scrollableTabService: ScrollableTabService,
    private msgService: MsgService,
    private confirmDialogService: ConfirmDialogService
  ) {
    this.buttonService.showSaveButton(false);
    this.glAccountId = this.route.parent.snapshot.paramMap.get('glAccountId');
    this.isEnable = this.glAccountService.isByInputEnumIdAndDetectOrgUnitIdFlag(this.glAccountId, "ACCINP_UO", "Y");

  }


  ngOnInit(): void {

    this.isEnable.pipe(
      filter(x => x === true),
      switchMap(() => this.reload$.pipe(
        tap(() => { this.loading = true }),
        switchMap(() => this.glAccountRoleService.getUoDetectedByGlAccountId(this.glAccountId))
      ))
    )
      .pipe(
        takeUntil(this.destroy$),
        map(x => {
          x.forEach(y => {
            y.fromDate = y.fromDate ? new Date(y.fromDate) : null;
            y.thruDate = y.thruDate ? new Date(y.thruDate) : null;
            y.party.partyName = `${y.party.partyParentRole.parentRoleCode} - ${this.secondaryLang ? y.party.partyNameLang : y.party.partyName}`
          });
          return x;
        })
      ).subscribe(values => {
        this.glAccountRoleList = values;
        this.loading = false;
        this.disableSave = true;
      })
  }

  newGlAccountRole() {
    this.ref = this.dialogService.open(NewUoDetectedComponent, {
      header: this.i18nService.translate('Select Organizational Unit'),

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
  reset() {
    this.dataTable.editingCell = null;
    this._reload.next();
  }

  save() {
    this.loadingSave = true;
    this.glAccountRoleService.updateGlAccountRole(this.updatedItems)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        {
          next: () => {
            this.msgService.successUpdate();
            this._reload.next();
            this.loadingSave = false;
          },
          error: (error) => {
            console.log(error)
            this.msgService.error(error.error.message ?? error.message);
            this.loadingSave = false;
          }
        }
      )

  }
  delete() {
    this.loadingDelete = true;
    this.confirmDialogService.deleteMultiElement().then(x => {
      if (x) {
        this.glAccountRoleService.deleteGlAccountRole(this.selectedGlAccountRole)
          .pipe(takeUntil(this.destroy$))
          .subscribe(
            {
              next: () => {
                this.msgService.successDelete();
                this.selectedGlAccountRole = [];
                this._reload.next();
                this.loadingDelete = false;

              },
              error: (error) => {
                console.log(error)
                this.msgService.error(error.error.message ?? error.message);
                this.loadingDelete = false;
              }
            }
          )
      }
    });

  }
  changeValue(glAccountRole: GlAccountRole) {
    const { fromDate, partyId, roleTypeId } = glAccountRole;
    if (!this.updatedItems.find(x =>
      x.fromDate === fromDate
      && x.partyId === partyId
      && x.roleTypeId === roleTypeId)) this.updatedItems.push(glAccountRole);
    this.disableSave = false;

  }

  doNothing(e: any) {
    e.stopPropagation();
    return;
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
