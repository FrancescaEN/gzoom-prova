import { ChangeDetectionStrategy, Component, OnDestroy, OnInit, computed } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GlAccount } from 'app/api/model/glAccount';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { CardDetail } from 'app/layout/gzoom-card-detail/gzoom-card-detail.component';
import { MenuItem, SelectItem } from 'primeng/api';
import { Observable, Subject, distinctUntilChanged, map, mergeAll, mergeWith, switchMap, takeUntil, tap } from 'rxjs';
import { getStatusItemSeverity } from '../indicators.component';
import { StatusItemService } from 'app/api/service/status-item.service';
import { dropdownStatusItem } from 'app/commons/utils/dropdownSelectItem';
import { I18NService } from 'app/i18n/i18n.service';
import { IndicatorService } from '../indicator.service';
import { ButtonService } from '../../../../commons/service/button.service';
import { isEqual } from 'lodash';
import { Location } from '@angular/common';
import { UomService } from 'app/api/service/uom.service';
import { toSignal } from '@angular/core/rxjs-interop';


@Component({
  selector: 'gzoom-indicator-tab',
  templateUrl: './indicator-tab.component.html',
  styleUrls: ['./indicator-tab.component.scss'],
})
export class IndicatorTabComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  secondaryLang: boolean;
  glAccountId: string;
  details: CardDetail[] = [
    { title: "ID", description: "" },
    { title: "Code", description: "" },
    { title: "Title", description: "" },
  ];


  glAccount$: Observable<GlAccount>;
  glAccount: GlAccount;

  menu: MenuItem[];

  statusItems: SelectItem[];
  statusItem$: Observable<SelectItem[]> = this.statusItemService.getStatusItemList("GL_ACCOUNT")
    .pipe(
      takeUntil(this.destroy$),
      map(x => dropdownStatusItem(x, this.secondaryLang)));

  showSave: boolean = true;
  disableSave: boolean;
  backState = this.location.getState();
  gpMenu = toSignal<string>(this.route.data.pipe(map(x => x.gpMenu)));
  context = toSignal<string>(this.route.data.pipe(map(x => x.context)));
  labelGlAccountList = computed(() => this.gpMenu() && this.context() ? `glAccountList.${this.context()}.${this.gpMenu()}` : null);
  labelGlAccountTab = computed(() => this.gpMenu() && this.context() ? `glAccountTab.${this.context()}.${this.gpMenu()}` : null);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private readonly i18nService: I18NService,
    private glAccountService: GlAccountService,
    private statusItemService: StatusItemService,
    private indicatorService: IndicatorService,
    protected buttonService: ButtonService,
    private uomService: UomService) {
    this.secondaryLang = this.i18nService.getIsSecondaryLang();
    this.glAccountId = this.route.snapshot.paramMap.get('glAccountId');
    this.addDetail({ title: "ID", description: this.glAccountId })
  }


  ngOnInit(): void {
    this.buttonService.getShowSaveButton().pipe(takeUntil(this.destroy$)).subscribe(x => setTimeout(() => this.showSave = x, 0));
    this.buttonService.getDisableSaveButton().pipe(takeUntil(this.destroy$))
      .pipe(distinctUntilChanged((prev, curr) => curr === this.disableSave))
      .subscribe(x => this.disableSave = x);

    this.menu = [
      {
        label: this.i18nService.translate(this.labelGlAccountTab()),
        routerLink: '.',
        routerLinkActiveOptions: { exact: true },
      },
      {
        label: this.i18nService.translate("purpose") + '*',
        routerLink: 'purpose',
      },
      {
        label: this.i18nService.translate("uo-detected"),
        routerLink: 'uo-detected',
        disabled: true
      },
      {
        label: this.i18nService.translate("organization"),
        routerLink: 'organization',
      },
      {
        label: this.i18nService.translate("calculation-parameters"),
        routerLink: 'calculation-parameters',
        //disabled: true
      },
      {
        label: this.i18nService.translate("value-list"),
        routerLink: 'value-list',
        disabled: true
      }
    ];

    this.statusItem$.pipe(
      takeUntil(this.destroy$)
    ).subscribe(x => {
      this.indicatorService.setStatusItems(x);
      this.statusItems = x
    });
    this.indicatorService.getGlAccount().pipe(
      mergeWith(
        this.uomService.isRatingScaleByGlAccount(this.glAccountId).pipe(
          tap(isRatingScale => this.indicatorService.setEnableValueList(isRatingScale)),
          switchMap(() => this.glAccountService.getGlAccount(this.glAccountId))
        )
      )
    ).pipe(
      takeUntil(this.destroy$),
    ).subscribe(glAccount => {
      this.glAccount = { ...glAccount };
      this.disableSave = true;
      this.addDetail({ title: "Code", description: glAccount.accountCode });
      this.addDetail({ title: "Title", description: this.secondaryLang ? glAccount.accountNameLang : glAccount.accountName });


      this.menu = [...this.menu.map(menuItem => {


        switch (menuItem.routerLink) {
          /*case "calculation-parameters":
            menuItem.disabled = !!!glAccount.calcCustomMethodId;
            break;*/
          case "uo-detected":
            menuItem.disabled = !(glAccount.detectOrgUnitIdFlag === "Y" && glAccount.inputEnumId === "ACCINP_UO");
            break;
        }
        return menuItem;
      })];
    }

    )

    this.indicatorService.getEnableValueList().pipe(takeUntil(this.destroy$),
      distinctUntilChanged(),
    )
      .subscribe(value => {

        this.menu = [...this.menu.map(menuItem => {
          if (menuItem.routerLink === 'value-list') {
            menuItem.disabled = !value;
          }
          return menuItem;
        })];

      });

    this.indicatorService.getCurrentStatus().pipe(takeUntil(this.destroy$))
      .subscribe(
        value => this.glAccount.currentStatusId = value
      );

    this.indicatorService.getTitle().pipe(takeUntil(this.destroy$))
      .subscribe(
        value => this.addDetail({ title: "Title", description: value })
      );

    this.indicatorService.getCode().pipe(takeUntil(this.destroy$))
      .subscribe(
        value => this.addDetail({ title: "Code", description: value })
      );


  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
  }

  addDetail(detail: CardDetail) {
    let index = this.details.findIndex(x => x.title == detail.title);
    this.details[index] = detail;
  }

  getStatusItemSeverity(currentStatusId: string): string {

    switch (currentStatusId) {
      case 'GLACC_ACTIVE':
        return 'success';

      case 'GLACC_CLOSED':
        return 'info';

      case 'GLACC_OPEN':
        return 'warning';
      default:
        return null;
    }
  }

  getStatusItemDescription(currentStatusId: string): string {
    let status = this.statusItems.find(x => x.value === currentStatusId);
    if (status) {
      return status.label;
    }
    return currentStatusId ?? this.i18nService.translate("No state");
  }

  toIndicators() {
    this.router.navigate(['../../'], { relativeTo: this.route, state: this.backState })
  }
}
