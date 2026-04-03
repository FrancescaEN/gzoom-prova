import { ChangeDetectionStrategy, Component, DestroyRef, OnDestroy, OnInit, computed, effect, inject, input, signal } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { I18NService } from 'app/i18n/i18n.service';
import { LayoutModule } from 'app/layout/layout.module';
import { filter, map, of, startWith, switchMap } from 'rxjs';
import IndicatorMovementsFiltersComponent from '../components/indicator-movements-filters/indicator-movements-filters.component';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Location } from '@angular/common';
import { IndicatorMovementsTableComponent } from '../components/indicator-movements-table/indicator-movements-table.component';
import { detectionModes } from 'app/api/model/detectionMode';
import { TitleListComponent } from "../../../../layout/title-list/title-list.component";
import { DataStorageService } from 'app/commons/service/data-storage.service';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { AddMovementComponent } from '../components/add-movement/add-movement.component';
import { AcctgTransEntry } from 'app/api/model/acctgTransEntry';
import { ButtonService } from 'app/commons/service/button.service';

@Component({
  selector: 'gzoom-indicator-movements',
  standalone: true,
  templateUrl: './indicator-movements.component.html',
  styleUrl: './indicator-movements.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    LayoutModule,
    IndicatorMovementsFiltersComponent,
    IndicatorMovementsTableComponent,
    TitleListComponent
  ],
  providers: [ButtonService]
})
export default class IndicatorMovementsComponent implements OnInit, OnDestroy {
  destroy = inject(DestroyRef);
  public dialogService = inject(DialogService);
  dataStorageService = inject(DataStorageService);
  i18nService = inject(I18NService);
  router = inject(Router);
  location = inject(Location);
  glAccountService = inject(GlAccountService);
  buttonService = inject(ButtonService);

  addDialog = new DynamicDialogRef();
  //input path
  accountTypeEnumId = input.required<string>();
  isReservedAccount = input<string>();

  //secondaryLang
  secondaryLang = signal<boolean>(this.i18nService.getIsSecondaryLang());

  title = input("", {
    alias: 'gpMenu', transform: (gpMenu: string) => this.i18nService.translate('list.CTX_AC.' + gpMenu)
  });

  filterState$ = this.router.events.pipe(
    filter(event => event instanceof NavigationEnd),
    switchMap(() => of(this.location.getState())),
    startWith(this.location.getState()),
    map(({ navigationId, ...values }: { [key: string]: string | string[] | number | boolean }) => values),
  );

  globalFilter = toSignal(this.filterState$);

  detectionModeGlAccount = computed(() => {
    if (this.globalFilter().glAccountId) {
      this.glAccountService.getGlAccount(this.globalFilter().glAccountId as string);
    }
    return null
  })
  private activeIndexKey = "activeIndex";
  activeIndex = signal<number>(JSON.parse(this.dataStorageService.getSessionData(this.router.url, this.activeIndexKey)));
  activatedIndex = signal<number[]>([]);

  detectionModeItems = detectionModes;

  route = inject(ActivatedRoute);
  gpMenu = toSignal<string>(this.route.data.pipe(map(data => data.gpMenu)));
  context = toSignal<string>(this.route.data.pipe(map(data => data.context)));

  constructor() {
    effect(() => {
      if (this.globalFilter().glAccountId) {
        this.glAccountService.getGlAccount(this.globalFilter().glAccountId as string)
          .pipe(
            takeUntilDestroyed(this.destroy),
            map(glAccount => this.detectionModeItems.map((x, index) => {
              if (x.value.detectOrgUnitIdFlag === glAccount.detectOrgUnitIdFlag && x.value.inputEnumId === glAccount.inputEnumId) {
                x.disabled = false;
                this.saveAccordionOpen(index);

              }
              else x.disabled = true;
            }))
          )
          .subscribe()
      }
      else this.detectionModeItems.map(x => x.disabled = false)
    })
  }

  ngOnInit(): void {
    this.activatedIndex.set([this.activeIndex()])
  }

  isEmpty(value: boolean, index: number) {
    //this.activeIndex.update(prev => prev === index ? null : prev);
    //this.detectionModeItems = [...this.detectionModeItems.map((x, i) => i === index ? { ...x, disabled: value } : x)];
  }

  newMovements() {
    this.addDialog = this.dialogService.open(AddMovementComponent, {
      header: this.i18nService.translate('newMovement'),

      styleClass: 'min-width p-dialog-width',
      contentStyle: { overflow: 'auto' },
      data: {
        accountTypeEnumId: this.accountTypeEnumId(),
        isReservedAccount: this.isReservedAccount(),
        glAccountId: this.globalFilter().glAccountId,
        gpMenu: this.gpMenu(),
        context: this.context()
      }
    });

    this.addDialog.onClose.subscribe((result: boolean) => {
      if (result) {
        this.buttonService.reload();
      }
    });
  }
  deleteAccordionClose(indexAccordion) {
    this.dataStorageService.removeSessionData(this.router.url, this.activeIndexKey);
  }

  saveAccordionOpen(indexAccordion) {
    this.activeIndex.set(indexAccordion)
    this.dataStorageService.setSessionData(this.router.url, this.activeIndexKey, indexAccordion);
  }

  ngOnDestroy(): void {
    this.addDialog?.close();
  }
}
