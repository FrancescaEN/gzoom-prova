import { CommonModule } from '@angular/common';
import { Component, DestroyRef, computed, inject, input, numberAttribute } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { I18NService } from 'app/i18n/i18n.service';
import { TableDetailChartComponent, TableDetailChartHeader } from 'app/shared/components/table-detail-chart/table-detail-chart.component';
import { orderBy } from 'lodash';
import { Observable, finalize, map, mergeMap, reduce, tap } from 'rxjs';

export interface TableDetailPreventionTrend {
  processName: string,
  measNotImplemented: number
}

@Component({
  selector: 'gzoom-table-detail-prevention-trend',
  standalone: true,
  imports: [CommonModule, TableDetailChartComponent],
  templateUrl: './table-detail-prevention-trend.component.html',
})
export default class TableDetailPreventionTrendComponent {
  private destroy = inject(DestroyRef);
  i18n = inject(I18NService);
  riskService = inject(RiskService);

  secondaryLang = this.i18n.getIsSecondaryLang();
  defaultYear = new Date().getFullYear();
  defaultFromYear = this.defaultYear - 3;

  year = input.required({ transform: numberAttribute });
  areaId = input.required<string>();
  orgUnitId = input<string>();

  loading = true;

  header: TableDetailChartHeader = {
    labels: ["processo", "misNoAttuate"],
    keys: [
      "processName",
      "measNotImplemented",
    ]
  }

  data$ = computed<Observable<TableDetailPreventionTrend[]>>(() => {
    if (this.year() && this.areaId()) {
      this.loading = true;

      return this.riskService.getRiskPreventionProcess(this.year(), this.areaId(), this.orgUnitId())
        .pipe(
          mergeMap(x => x),
          mergeMap(x => x.processes),
          map(p => ({
            processName: (p.processEtch ? `${p.processEtch} - ` : '') + p[this.secondaryLang ? 'processNameLang' : 'processName'],
            measNotImplemented: p.processSummary?.measNotImplemented
          } as TableDetailPreventionTrend)),
          reduce((acc: TableDetailPreventionTrend[], val) => [...acc, val], []),
          map(x => orderBy(x, "processName")),
          finalize(() => this.loading = false),
          takeUntilDestroyed(this.destroy),
        );
    }
  })
}
