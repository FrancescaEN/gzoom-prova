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
import { NewOrganizationComponent } from './new-organization/new-organization.component';
import { MsgService } from 'app/commons/service/message.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { Table } from 'primeng/table';

interface GlAccountOrganizationInterface {
  organizationPartyId: string,
  organizationDescr: string,
  fromDate: Date,
  thruDate: Date
}

@Component({
  selector: 'gzoom-indicator-tab-organization',
  templateUrl: './indicator-tab-organization.component.html',
  styleUrls: ['./indicator-tab-organization.component.scss']
})
export class IndicatorTabOrganizationComponent implements OnInit, OnDestroy {
  glAccountId: string;
  private destroy$ = new Subject<void>();
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  scrollable: Observable<boolean> = this.scrollableTabService.isScrollableWidth(960);
  disableSave: boolean = true;
  loading: boolean = true;
  glAccountOrganization: GlAccountOrganization[] = [];
  party: Party[] = [];

  glAccountOrganizationView: GlAccountOrganizationInterface[] = [];

  selectedItems: GlAccountOrganizationInterface[] = [];
  updatedItems: string[] = [];
  ref: DynamicDialogRef = new DynamicDialogRef();
  @ViewChild("dt") private dataTable: Table;

  constructor(private router: Router,
    private route: ActivatedRoute,
    private i18nService: I18NService,
    private glAccountOrganizationService: GlAccountOrganizationService,
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
          glAccountOrganizationList: this.glAccountOrganizationService.getGlAccountOrganizationByGlAccountId(this.glAccountId),
          partyList: this.partyService.getPartyByRoleTypeId('INTERNAL_ORGANIZATIO')
        })),
      map(({ glAccountOrganizationList, partyList }) => {
        this.glAccountOrganization = glAccountOrganizationList;
        this.party = partyList;
        let tmpGAO: GlAccountOrganizationInterface[] = [];
        glAccountOrganizationList.forEach(gao => {
          let partySelected = this.party.find(x => x.partyId === gao.organizationPartyId)
          tmpGAO.push({
            organizationPartyId: partySelected.partyId,
            organizationDescr: (this.secondaryLang ? partySelected.partyNameLang : partySelected.partyName),
            fromDate: gao.fromDate ? new Date(gao.fromDate) : null,
            thruDate: gao.thruDate ? new Date(gao.thruDate) : null
          })
        });

        return tmpGAO;
      })
    )
      .subscribe(values => { this.glAccountOrganizationView = values; this.loading = false; });
  }

  newOrganization() {
    this.ref = this.dialogService.open(NewOrganizationComponent, {
      header: this.i18nService.translate('Select organization'),

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

  changeValue(organizationPartyId: string) {

    if (this.updatedItems.indexOf(organizationPartyId) === -1) this.updatedItems.push(organizationPartyId);
    this.disableSave = false;
  }


  reset() {
    this.dataTable.editingCell = null;
    this._reload.next();
  }

  save() {
    const updated = this.glAccountOrganizationView.filter(x => this.updatedItems.indexOf(x.organizationPartyId) != -1);
    let glAccountOrganization: GlAccountOrganization[] = [];
    updated.forEach(gao => {
      const newGAO: GlAccountOrganization = { glAccountId: this.glAccountId, organizationPartyId: gao.organizationPartyId, fromDate: gao.fromDate, thruDate: gao.thruDate }
      glAccountOrganization.push(newGAO);
    });

    this.glAccountOrganizationService.update(glAccountOrganization)
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

  delete() {
    this.confirmDialogService.deleteMultiElement().then(
      x => {
        if (x) {
          this.glAccountOrganizationService.delete(this.selectedItems.map(x => x.organizationPartyId), this.glAccountId)
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
      }
    )


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
