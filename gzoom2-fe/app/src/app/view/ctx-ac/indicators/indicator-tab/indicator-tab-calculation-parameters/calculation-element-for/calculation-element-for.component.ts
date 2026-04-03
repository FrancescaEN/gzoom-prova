import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { DialogService } from 'primeng/dynamicdialog';
import { BehaviorSubject, Observable, Subject, map, takeUntil } from 'rxjs';
import { GlAccountInputCalcService } from 'app/api/service/gl-account-input-calc.service';
import { GlAccountInputCalc } from 'app/api/model/glAccountInputCalc';
import { CustomMethodService } from 'app/api/service/custom-method.service';
import { CustomMethod } from 'app/api/model/customMethod';
import { orderBy } from 'lodash';
import { SelectItem } from 'primeng/api';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';


@Component({
  selector: 'gzoom-calculation-element-for',
  templateUrl: './calculation-element-for.component.html',
  styleUrls: ['./calculation-element-for.component.scss']
})
export class CalculationElementForComponent implements OnInit, OnDestroy {

  private destroy$ = new Subject<void>();
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  scrollable: Observable<boolean> = this.scrollableTabService.isScrollableWidth(960);
  disableSave: boolean = true;
  loading: boolean = true;
  glAccountInputCalc: GlAccountInputCalc[] = [];
  glAccountId: string;
  customMethod$: Observable<SelectItem[]>



  constructor(
    private route: ActivatedRoute,
    private i18nService: I18NService,
    public dialogService: DialogService,
    private glAccountInputCalcService: GlAccountInputCalcService,
    private customMethodService: CustomMethodService,
    private scrollableTabService: ScrollableTabService
  ) {
    this.glAccountId = this.route.parent.snapshot.paramMap.get('glAccountId');
  }

  ngOnInit(): void {
    this.glAccountInputCalcService.getGlAccountInputCalcByGlAccountIdRef(this.glAccountId)
      .pipe(
        takeUntil(this.destroy$),
        map(x => {
          x.forEach(y => {
            const accountName = y.glAccount?.accountName,
              accountNameLang = y.glAccount?.accountNameLang,
              accountCode = y.glAccount?.accountCode;
            y.glAccount.accountName =
              `${accountCode} - ${(this.secondaryLang
                ? accountNameLang
                : accountName)}`;

          })
          return x;
        })
      ).subscribe(x => { this.glAccountInputCalc = x; this.loading = false })

    this.customMethod$ = this.customMethodService.getCustomMethodList()
      .pipe(map(x => this.dropdownCustomMethod(x)))
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
  }

  dropdownCustomMethod(types: CustomMethod[]): SelectItem[] {
    if (types) {
      const selItem = types.map((x: CustomMethod) => { return { label: x.customMethodId, value: x.customMethodId } });
      return orderBy(selItem, ['label'], ['asc']);
    }
    return []
  }


}
