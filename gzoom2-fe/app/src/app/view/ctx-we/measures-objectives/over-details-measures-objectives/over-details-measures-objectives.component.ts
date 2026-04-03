import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WorkEffortMeasRatScExUomRatingScale } from 'app/api/model/workEffortMeasRatScExUomRatingScale';
import { LanguageService } from 'app/api/service/language.service';
import { UomService } from 'app/api/service/uom.service';
import { WorkEffortMeasRatScService } from 'app/api/service/work-effort-meas-rat-sc.service';
import { I18NService } from 'app/i18n/i18n.service';
import { Uom } from 'app/api/model/uom';
import { MenuItem } from 'primeng/api';
import { lastValueFrom, map, mergeMap, mergeWith, Subject } from 'rxjs';



@Component({
  selector: 'gzoom-over-details-measures-objectives',
  templateUrl: './over-details-measures-objectives.component.html',
  styleUrls: ['./over-details-measures-objectives.component.scss']
})
export class OverDetailsMeasuresObjectivesComponent implements OnInit {
  menuDetails: boolean = true;
  itemsn: MenuItem[];
  activeIndexTabView: number = 1;
  _reload: Subject<void>;
  workEffortMeasureId: string;
  uom: Uom;

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly workEffortMeasRatScService: WorkEffortMeasRatScService,
    private readonly uomService: UomService,
    private readonly router: Router,

  ) { this._reload = new Subject<void>(); }

  async ngOnInit(): Promise<void> {

    this.itemsn = [

    ];

    let element = {
      label: this.i18nService.translate("Measurement detail"),
      routerLink: `./detail`,
      routerLinkActiveOptions: { exact: true },
    }

    this.itemsn = [element, ...this.itemsn]

    this.route.paramMap.subscribe(paramMap => {
      this.workEffortMeasureId = paramMap.get('workEffortMeasureId');
    });

    const wemrs$ = this.workEffortMeasRatScService.getRatingScaleWEM(this.workEffortMeasureId);

    wemrs$.pipe(
      map(e => ({
        label: this.i18nService.translate("Measurement scale"),
        routerLink: `./rating-scale`,
        routerLinkActiveOptions: { exact: true },
      })),
    )
    .subscribe(item => {
      this.itemsn = [item, ...this.itemsn]
    });

    
    

  }

}
