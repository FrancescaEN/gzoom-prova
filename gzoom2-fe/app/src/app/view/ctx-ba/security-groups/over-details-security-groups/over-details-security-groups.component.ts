import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LanguageService } from 'app/api/service/language.service';
import { I18NService } from 'app/i18n/i18n.service';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'gzoom-over-details-security-groups',
  templateUrl: './over-details-security-groups.component.html',
  styleUrls: ['./over-details-security-groups.component.scss']
})
export class OverDetailsSecurityGroupsComponent implements OnInit{


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
        label: this.i18nService.translate("Permission"),
        routerLink: `./permission`,
        routerLinkActiveOptions: { exact: true },
      },
      {
        label: this.i18nService.translate("SecurityGroupPermissionExcept"),
        routerLink: `./functions-excluded`,
        routerLinkActiveOptions: { exact: true },
      },
      {
        label: this.i18nService.translate("Enabled users"),
        routerLink: `./enabled-users`,
        routerLinkActiveOptions: { exact: true },
      }

    ];
  }

}
