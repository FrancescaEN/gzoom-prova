import { CommonModule } from '@angular/common';
import { Component, DestroyRef, computed, inject, input, numberAttribute, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { I18NService } from 'app/i18n/i18n.service';
import { TableDetailChartComponent, TableDetailChartHeader } from 'app/shared/components/table-detail-chart/table-detail-chart.component';
import { orderBy } from 'lodash';
import { finalize, map, mergeMap, reduce } from 'rxjs';

export interface TableDatailRiskTrend {
  processName: string,
  proScore: number,
  levelName: string
}
@Component({
  selector: 'gzoom-table-detail-risk-trend',
  standalone: true,
  imports: [CommonModule, TableDetailChartComponent],
  templateUrl: './table-detail-risk-trend.component.html'
})
export class TableDetailRiskTrendComponent {
  private destroy = inject(DestroyRef);
  i18n = inject(I18NService);
  riskService = inject(RiskService);

  secondaryLang = this.i18n.getIsSecondaryLang();
  defaultYear = new Date().getFullYear();
  defaultFromYear = this.defaultYear - 3;

  year = input.required({ transform: numberAttribute });
  areaId = input.required<string>();
  orgUnitId = input<string>();

  header: TableDetailChartHeader = {
    labels: ["processo", "proScore", "proRisk"],
    keys: [
      "processName",
      "proScore",
      "levelName"
    ]
  }

  loading = true;

  data$ = computed(() => {
    if (this.year() && this.areaId()) {
      this.loading = true;
      return this.riskService.getRiskProcessByAreaAndYear(this.year(), this.areaId(), this.orgUnitId())
        .pipe(
          mergeMap(periodProcess => periodProcess ?? []),
          mergeMap(period => period.processes),
          map(p => ({
            processName: (p.processEtch ? `${p.processEtch} - ` : '') + p[this.secondaryLang ? 'processNameLang' : 'processName'],
            proScore: p.proScore,
            levelName: p.processLevel[this.secondaryLang ? 'levelNameLang' : 'levelName']
          } as TableDatailRiskTrend)),
          reduce((acc: TableDatailRiskTrend[], val) => [...acc, val], []),
          map(x => orderBy(x, "processName")),
          finalize(() => this.loading = false),
          takeUntilDestroyed(this.destroy)
        );
    }
  })




}
