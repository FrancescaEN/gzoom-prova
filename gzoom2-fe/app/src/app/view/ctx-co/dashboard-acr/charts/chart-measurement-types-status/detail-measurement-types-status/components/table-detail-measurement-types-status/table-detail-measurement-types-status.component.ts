import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, DestroyRef, inject, input, numberAttribute, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { I18NService } from 'app/i18n/i18n.service';
import { TableDetailChartComponent, TableDetailChartHeader } from 'app/shared/components/table-detail-chart/table-detail-chart.component';
import { orderBy } from 'lodash';
import { catchError, combineLatest, filter, map, mergeMap, of, reduce, switchMap, tap } from 'rxjs';

export type TableDetailMeasurementTypesStatus = {
  measureTypeName: string,
  measureTypePct: number
}
@Component({
  selector: 'gzoom-table-detail-measurement-types-status',
  standalone: true,
  imports: [CommonModule, TableDetailChartComponent],
  templateUrl: './table-detail-measurement-types-status.component.html',
  styles: ``,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TableDetailMeasurementTypesStatusComponent {
  private destroy = inject(DestroyRef);
  i18n = inject(I18NService);
  riskService = inject(RiskService);
  loading = signal<boolean>(true);

  secondaryLang = this.i18n.getIsSecondaryLang();

  year = input.required({ transform: numberAttribute });
  levelId = input.required({ transform: numberAttribute });
  orgUnitId = input<string>();

  header: TableDetailChartHeader = {
    labels: ["tipoMisura", "misPerc"],
    keys: [
      "measureTypeName",
      "measureTypePct",
    ]
  }

  data = toSignal(
      combineLatest([
        toObservable(this.year),
        toObservable(this.levelId),
        toObservable(this.orgUnitId)
      ]).pipe(
        filter(([year, levelId]) => !!year && !!levelId),
        switchMap(([year, levelId, orgUnitId]) =>
          this.riskService.getRiskMeasureTypesByYearAndLevelId(year, levelId, orgUnitId)
            .pipe(
              mergeMap(period => period?.measureTypes ?? []),
              map(m => ({
                measureTypeName: this.secondaryLang ? m.measureTypeNameLang : m.measureTypeName,
                measureTypePct: m.measureTypePct
              } as TableDetailMeasurementTypesStatus)),
              reduce((acc: TableDetailMeasurementTypesStatus[], val) => [...acc, val], []),
              map(x => orderBy(x, "measureTypeName")),
              tap(() => this.loading.set(false)),
              catchError(() => {
                this.loading.set(false);
                return of([] as TableDetailMeasurementTypesStatus[]);
              })
            )
        )
      ),
      { 
        initialValue: [] as TableDetailMeasurementTypesStatus[],
      }
    );
}
