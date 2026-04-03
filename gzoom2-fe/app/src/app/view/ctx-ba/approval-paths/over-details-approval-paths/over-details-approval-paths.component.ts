import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'gzoom-over-details-approval-paths',
  templateUrl: './over-details-approval-paths.component.html',
  styleUrls: ['./over-details-approval-paths.component.scss']
})
export class OverDetailsApprovalPathsComponent implements OnInit {

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
        label: this.i18nService.translate("Predicted States"),
        routerLink: `./predicted-states`,
        routerLinkActiveOptions: { exact: true },
      },
      {
        label: this.i18nService.translate("Passes Allowed"),
        routerLink: `./passes-allowed`,
        routerLinkActiveOptions: { exact: true },
      }

    ];
  }
}
