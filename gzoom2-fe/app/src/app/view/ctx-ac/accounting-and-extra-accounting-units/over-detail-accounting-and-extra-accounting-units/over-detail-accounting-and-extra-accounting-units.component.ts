import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'gzoom-over-detail-accounting-and-extra-accounting-units',
  templateUrl: './over-detail-accounting-and-extra-accounting-units.component.html',
  styleUrls: ['./over-detail-accounting-and-extra-accounting-units.component.scss']
})
export class OverDetailAccountingAndExtraAccountingUnitsComponent implements OnInit {



  menuDetails: boolean = true;
  itemsn: MenuItem[];
  activeIndexTabView: number = 1;
  glAccountTypeId: string;


  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly router: Router,

  ){}
  
  ngOnInit(): void {

    const routeParams = this.route.snapshot.paramMap;
    console.log(routeParams);
    
    this.glAccountTypeId = routeParams.get('glAccountTypeId');

    this.itemsn = [
      {
        label: this.i18nService.translate("Detection Type"),
        routerLink: `./detection-types`,
        routerLinkActiveOptions: { exact: true },
      },
      {
        label: this.i18nService.translate("Natura"),
        routerLink: `./nature`,
        routerLinkActiveOptions: { exact: true },
      }

    ];
  }

}
