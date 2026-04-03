import { CommonModule } from '@angular/common';
import { Component, DestroyRef, computed, inject, input, numberAttribute, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RiskService } from 'app/api/service/dashboards/risk.service';
import { I18NService } from 'app/i18n/i18n.service';
import { TableDetailChartComponent, TableDetailChartHeader } from 'app/shared/components/table-detail-chart/table-detail-chart.component';
import { orderBy } from 'lodash';
import { finalize, map, mergeMap, reduce } from 'rxjs';

export type TableDetailStateRiskFactors = {
  factorName: string,
  factorPct: number
}
@Component({
  selector: 'gzoom-table-detail-state-risk-factors',
  standalone: true,
  imports: [CommonModule, TableDetailChartComponent],
  templateUrl: './table-detail-state-risk-factors.component.html',
  styles: ``
})
export class TableDetailStateRiskFactorsComponent {
  private destroy = inject(DestroyRef);
  i18n = inject(I18NService);
  riskService = inject(RiskService);

  secondaryLang = this.i18n.getIsSecondaryLang();
  loading = true;

  year = input.required({ transform: numberAttribute });
  levelId = input.required({ transform: numberAttribute });
  orgUnitId = input<string>();

  header: TableDetailChartHeader = {
    labels: ["fattoreRischio", "fatPerc"],
    keys: [
      "factorName",
      "factorPct",
    ]
  }



  data$ = computed(() => {
    if (this.year() && this.levelId() !== null && this.levelId() !== undefined) {
      this.loading = true;
      return this.riskService.getRiskFactorsByYearAndLevelId(this.year(), this.levelId(), this.orgUnitId())
        .pipe(
          mergeMap(x => x.factors),
          map(factor => ({
            factorName: this.secondaryLang ? factor.factorNameLang : factor.factorName,
            factorPct: factor.factorPct
          } as TableDetailStateRiskFactors)),
          reduce((acc: TableDetailStateRiskFactors[], val) => [...acc, val], []),
          map(x => orderBy(x, "factorName")),
          finalize(() => this.loading = false),
          takeUntilDestroyed(this.destroy)
        );
    }
  })

}
