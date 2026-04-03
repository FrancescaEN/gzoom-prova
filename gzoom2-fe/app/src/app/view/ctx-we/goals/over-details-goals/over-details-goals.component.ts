import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'gzoom-over-details-goals',
  templateUrl: './over-details-goals.component.html',
  styleUrls: ['./over-details-goals.component.scss']
})
export class OverDetailsGoalsComponent implements OnInit {
  

  menuDetails: boolean = true;
  itemsn: MenuItem[];
  activeIndexTabView: number = 1;

  constructor(
    private route: ActivatedRoute,
    private readonly i18nService: I18NService,
    private readonly languageService: LanguageService,
    private readonly router: Router,

  ){}
  
  ngOnInit(): void {
    this.itemsn = [
      {
        label: this.i18nService.translate("Details objective"),
        routerLink: `./DO`,
        routerLinkActiveOptions: { exact: true },
      },
      {
        label: this.i18nService.translate("Data History"),
        routerLink: `./SD`,
        routerLinkActiveOptions: { exact: true },
      }

    ];
  }

}
