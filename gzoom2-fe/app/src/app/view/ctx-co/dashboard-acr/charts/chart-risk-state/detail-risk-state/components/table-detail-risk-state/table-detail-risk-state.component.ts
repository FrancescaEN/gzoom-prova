import { Component, inject, input, numberAttribute, signal } from '@angular/core';
import { toSignal, toObservable } from '@angular/core/rxjs-interop';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { I18NService } from 'app/i18n/i18n.service';
import { TableDetailChartComponent, TableDetailChartHeader } from 'app/shared/components/table-detail-chart/table-detail-chart.component';
import { catchError, combineLatest, filter, map, mergeMap, of, reduce, switchMap, tap } from 'rxjs';

export interface TableDetailRiskState {
  levelName: string,
  areaName: string,
  processName: string
}
@Component({
  selector: 'gzoom-table-detail-risk-state',
  standalone: true,
  imports: [TableDetailChartComponent],
  templateUrl: './table-detail-risk-state.component.html',
})
export class TableDetailRiskStateComponent {
  riskService = inject(RiskService);
  i18n = inject(I18NService);
  secondaryLang = this.i18n.getIsSecondaryLang();

  year = input.required({ transform: numberAttribute });
  orgUnitId = input<string>();

  header: TableDetailChartHeader = {

    labels: ["areaRischio", "processAtRisk", "proRisk"],
    keys: ["areaName", "processName", "levelName"]
  };

  loading = signal<boolean>(true);

  item = toSignal(
    combineLatest([
      toObservable(this.year),
      toObservable(this.orgUnitId)
    ]).pipe(
      filter(([year]) => !!year),
      switchMap(([year, orgUnitId]) =>
        this.riskService.getRiskProcessByYear(year, orgUnitId)
          .pipe(
            mergeMap(processes => processes ?? []),
            map(process => ({
              levelName: process.processLevel[this.secondaryLang ? 'levelNameLang' : 'levelName'],
              areaName: process.area[this.secondaryLang ? 'areaNameLang' : 'areaName'],
              processName: (process.processEtch ? `${process.processEtch} - ` : '') + process[this.secondaryLang ? 'processNameLang' : 'processName'],
            } as TableDetailRiskState)),
            reduce((acc: TableDetailRiskState[], val) => [...acc, val], []),
            tap(() => this.loading.set(false)),
            catchError(() => {
              this.loading.set(false);
              return of([] as TableDetailRiskState[]);
            })
          )
      )
    ),
    { 
      initialValue: [] as TableDetailRiskState[],
    }
  );

}
