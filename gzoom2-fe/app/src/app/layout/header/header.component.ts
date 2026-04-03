import { distinctUntilChanged, filter, map } from 'rxjs/operators';
import { Observable, lastValueFrom, of } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import * as _ from 'lodash';
import { AuthService, UserProfile } from '../../commons/service/auth.service';
import { ActivatedRoute, Event, NavigationEnd, Router, RouterEvent } from '@angular/router';
import { LogoutService } from '../../commons/service/logout.service';
import { LoginService } from '../../commons/service/login.service';
import { ChangePasswordService } from '../../shared/change-password/change-password.service';
import { UserPreferenceService } from '../../api/service/user-preference.service';
import { UserPreference } from '../../shared/user-preference';
import { NodeService } from '../../shared/node.service';
import { SelectItem } from '../../commons/model/selectitem';
import { ApiConfig } from '../../commons/model/api-config';
import { I18NService } from '../../i18n/i18n.service';
import { Message } from '../../commons/model/message';
import { Node } from '../../view/node/node';
import { ApiClientService } from 'app/commons/service/client.service';
import { UserLoginValidPartyRole } from 'app/api/model/userLoginValidPartyRole';
import { Title } from '@angular/platform-browser';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { TwoFactorAuthComponent } from 'app/view/login/two-factor-auth/two-factor-auth.component';
import { TwoFactorAuthDisableComponent } from 'app/view/login/two-factor-auth-disable/two-factor-auth-disable.component';
import { HeaderService } from 'app/api/service/header.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { ThemeService } from 'app/commons/service/theme.service';
import { VersionConfigService } from 'app/commons/service/config.service';
import { ApplicationVersion } from 'app/commons/model/config';
import { VersionComponent } from './version/version.component';
import { FaviconService } from 'app/commons/service/favicon.service';
import { KeycloakAuthService } from 'app/commons/service/keycloak-auth.service';
import { AuthModeService } from 'app/commons/service/auth-mode.service';

const CHANGE_PASS_ENDPOINT = 'change-password';
const CHANGE_LANG_ENDPOINT = 'change-language';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  user: UserProfile;
  node: Node;
  legacyAppVersions: String;
  restVersions: String;
  displayChangePassword = false;
  displayChangeTheme = false;
  displayUserDialog = false;
  error = '';
  langType: string;
  organizationType: string;
  organizations: UserLoginValidPartyRole[];
  organizationSelectItem: SelectItem[] = [];
  organizationSelected = 'Company';
  allowChangePassword: boolean = true;
  msgs: Message[] = [];
  languages: String[] = [];
  currentPassword: String;
  newPassword: String;
  newPasswordVerify: String;
  serverUrl: string;
  urlParam = 'GP_HOMEPAGE';
  keycloakEnabled: boolean = false;
  
  private readonly changePassUrl: string;
  private readonly changeLangUrl: string;

  mailHost: String;
  userPreference: UserPreference = new UserPreference();
  enableTwoFa$: Observable<boolean>;
  enableTwoFamanage2fauth$: Observable<boolean>;
  userPreference2fAuth$: Observable<boolean>;
  userPrefValue2Fauth: string;
  twofauthDisableUrl: string;
  twofauthUrl: string;
  currentRoute: any;
  enableldap: any;
  versions: ApplicationVersion[] = [];

  ref: DynamicDialogRef | undefined;

  switchTitleLogo = toSignal(this.headerService.isSwitchTitleLogo())

  menu: any[] = [];

  constructor(private router: Router,
              private route: ActivatedRoute,
              private readonly authSrv: AuthService,
              private readonly logoutSrv: LogoutService,
              private readonly userPreferenceService: UserPreferenceService,
              private readonly nodeService: NodeService,
              private readonly i18nService: I18NService,
              private readonly loginService: LoginService,
              private readonly changePasswordService: ChangePasswordService,
              private apiConfig: ApiConfig,
              public dialogService: DialogService,
              private client: ApiClientService,
              private titleService: Title,
              private headerService: HeaderService,
              private themeService: ThemeService,
              private versionConfigService: VersionConfigService,
              private keycloak: KeycloakAuthService,
              private authMode: AuthModeService,
              private faviconService: FaviconService
  ) {
    this.user = authSrv.userProfile();
    this.versions = versionConfigService.getConfig();
    this.changePassUrl = `${apiConfig.rootPath}/${CHANGE_PASS_ENDPOINT}`;
    this.changeLangUrl = `${apiConfig.rootPath}/${CHANGE_LANG_ENDPOINT}`;
    this.keycloakEnabled = this.authMode.isKeycloakEnabled();
  }

  async ngOnInit() {

    const qru = this.route.snapshot.queryParams['returnUrl'];
    this.twofauthUrl = qru && qru !== 'c/2FAuth' && qru !== 'c/2FAuth' ? qru : 'c/2FAuth';
    this.twofauthDisableUrl = qru && qru !== 'c/2FAuthDisable' && qru !== 'c/2FAuthDisable' ? qru : 'c/2FAuthDisable';

    this.route.data.subscribe(data => {
      if (data['menu'] && data['menu'].children) {
        this.menu = data['menu'].children
      } else {
        console.log('Menu or children property does not exist in route data.');
      }
    });


    this.route.data.pipe(
      map((data: { node: Node }) => data.node)
    ).subscribe((data) => {
      this.node = data;
    });

    this.route.data.pipe(
      map((data: { theme: UserPreference }) => data.theme)
    ).subscribe((data) => {
      this.userPreference = data;
      this.setTheme(this.userPreference.userPrefValue);
    });


    this.router.events.pipe(
      filter((event: Event | RouterEvent) => event instanceof NavigationEnd),
      distinctUntilChanged()
    ).subscribe(data => {
      this.urlParam = data['url'];
      this.urlParam = this.urlParam && this.urlParam.indexOf('legacy') > 0 ? this.urlParam.substring(this.urlParam.lastIndexOf('/') + 1, this.urlParam.length) : 'GP_HOMEPAGE';
    });


    const mailHost$ = this.client.get('/profile/mail-host').pipe(map(json => json as string));
    this.enableTwoFa$ = mailHost$.pipe(
      map(mailHost => {
        if (mailHost && mailHost != 'N') {
          return true
        } else {
          return false
        }
      }));

    const manage2fauth$ = this.client.get('/profile/manage-2fauth').pipe(map(json => json as string));
    this.enableTwoFamanage2fauth$ = manage2fauth$.pipe(
      map(manage2fauth => {
        if (manage2fauth) {
          return true
        } else {
          return false
        }
      }));


    const up$ = this.userPreferenceService.getUserPreference('2FAEnabled');
    this.userPreference2fAuth$ = up$.pipe(map((data) => {
      if (data && (data.userPrefValue == 'Y')) {
        this.userPrefValue2Fauth = data.userPrefValue;
        return true
      } else if (data && (data.userPrefValue == 'N')) {
        this.userPrefValue2Fauth = data.userPrefValue;
        return true
      } else {
        return false
      }
    }));

    const ldap$ = this.client.get('/profile/isLdap').pipe(map(json => json as string));
    await lastValueFrom(ldap$).then(
      data => {
        this.enableldap = data;
      });


    this.langType = this.i18nService.getLanguageType();
    this.client.get('/profile/i18n/languages').subscribe(json => {
      this.languages = json.results as String[];
      //console.log('languages available ' + this.languages);
    });

    this.userPreferenceService.getUserPreference('ORGANIZATION_PARTY').subscribe(
      data => {
        //console.log('data.userPrefValue:' + data.userPrefValue);
        if (data.userPrefValue && data.userPrefValue !== 'DEFAULT') {
          this.organizationSelected = data.userPrefValue;
        }

        //console.log('organizationSelected:' + this.organizationSelected);
      }
    );

    this.userPreferenceService.getOrganizationMultiType().subscribe(
      data => {
        this.organizationType = data;
        //console.log('Organization type: ' + this.organizationType);
      });

    this.nodeService.nodeXmlRcpUrl().subscribe(
      data => {
        this.serverUrl = data.substring(0, data.indexOf('/gzoom'));
        //console.log('Server URL: ' + this.serverUrl)
      }
    )

    const organizationsReload = this.userPreferenceService.getOrganizations()
      .subscribe(
        data => {
          this.organizationSelectItem = this.organization2SelectItems(data);
          let title = this.organizationSelectItem.find(item =>
            item.value === this.organizationSelected
          );
          this.titleService.setTitle(title.label);
        }
      );


    const loginService$ = this.loginService.getUserLogin();
    const usr = lastValueFrom(loginService$).then(
      userLogin => {
        if (userLogin) {
          if (!this.keycloakEnabled && userLogin.requirePasswordChange == 'Y')
            this.changePasswordService.openPopup(userLogin);
        }
      });

    this.nodeService.nodeLegacyVersions().subscribe(
      (legacyVersions: string) => {
        this.legacyAppVersions = legacyVersions;
      }
    );

    this.nodeService.nodeRestVersions().subscribe(
      (restVersions: string) => {
        this.restVersions = restVersions;
      }
    );

    this.client.get('/api/getEnableChangePassword').subscribe({
        next: (boolResponse: boolean) => { this.allowChangePassword = boolResponse },
        error: (err) => console.log(err)
      }
    )
  }

  async ngAfterViewInit() {
    const logoElement = document.querySelector<HTMLImageElement>('#loginLogo');
    if (!logoElement) return;

    if (this.authMode.isKeycloakEnabled() && this.keycloak.isLoggedIn()) {
      // usa il token già acquisito
      const url = `/rest/node/logo/{{ organizationSelected }}/LOGO`;
      this.faviconService.setLogoFromServer(logoElement, url);
    } else {
      // login nativo o Keycloak non loggato
      logoElement.src = 'assets/logo_placeholder.png';
    }
  }

  toggleSidebar() {
    if (this.menu.length > 0) {
      const dom: any = document.querySelector('body');
      const menu: any = document.querySelector('#sidebar');
      if (menu.classList.contains('collapse')) {
        dom.classList.remove('push-right');
      } else {
        dom.classList.add('push-right');
      }
      menu.classList.toggle('collapse');
    }
  }

  logout() {
    this.logoutSrv.logout()
  }

  changePasswordDialog() {
    const loginService$ = this.loginService.getUserLogin();
    const usr = lastValueFrom(loginService$).then(
      userLogin => {
        if (userLogin) {
          this.changePasswordService.openPopup(userLogin);
        }
      });
  }

  changeThemeDialog(val) {
    this.displayChangeTheme = val;
  }

  setTheme(theme) {
    this.themeService.switchStyle(theme)
    localStorage.setItem('DEFAULT_THEME', theme)
    this.displayChangeTheme = false;
  }

  setOrganization() {
    console.log('setOrganization: ' + this.organizationSelected);
    const newOrganization: UserPreference = new UserPreference();
    newOrganization.userPrefValue = this.organizationSelected;
    newOrganization.userPrefTypeId = 'ORGANIZATION_PARTY';
    this.userPreferenceService.updateUserPreference(newOrganization).subscribe({
      next: () => {
        window.location.reload()
      },
      error: (error) => {
        console.error('Error while updating in:', error);
        this.error = error.message;
      }
    });
  }

  saveChangeTheme(theme): void {
    this.userPreference.userPrefTypeId = 'VISUAL_THEME';
    this.userPreference.userPrefValue = theme;
    this.userPreferenceService.updateUserPreference(this.userPreference).subscribe({
      next: () => {
        this.setTheme(theme)
      },
      error: (error) => {
        console.error('Error while updating in:', error);
        this.error = error.message;
      }
    });
  }

  changeLang(lang: String) {
    const body = JSON.stringify({
      username: this.user.username,
      externalLoginKey: this.user.externalLoginKey,
      lang: lang
    });
    this.client.post(this.changeLangUrl, body)
      .subscribe({
        next: (data: any) => {
          console.log('change language:' + data);
          window.location.reload();
        },
        error: err => {
          console.log('error change language', err);
        }
      })
  }


  organization2SelectItems(organization: UserLoginValidPartyRole[]): SelectItem[] {
    if (organization == null) {
      return [];
    }
    return _.map(organization['results'], u => {
      let label = u.partyGroup.groupName;
      if (this.i18nService.getIsSecondaryLang()) {
        label = u.partyGroup.groupNameLang;
      }
      return {
        label: label, value: u.partyId
      };
    });
  }

  openDialogMobile() {
    this.displayUserDialog = true;
  }

  twoFactorEnable() {

    this.ref = this.dialogService.open(TwoFactorAuthComponent, {
      header: 'Two Factory Authentication',

      styleClass: 'min-width p-dialog-width',
      contentStyle: { overflow: 'auto' }
    });

    this.ref.onClose.subscribe(
      () => {
        location.reload();
      }
    )

  }

  twoFactorDisable() {

    this.ref = this.dialogService.open(TwoFactorAuthDisableComponent, {
      header: 'Two Factory Authentication',
      styleClass: 'min-width p-dialog-width',
      contentStyle: { overflow: 'auto' },
      data: {}
    });

    this.ref.onClose.subscribe(
      () => {
        location.reload();
      }
    )
  }

  openInfo(position?: string) {
    this.ref = this.dialogService.open(VersionComponent, {
      header: 'Info',
      contentStyle: { overflow: 'auto' },
      position: position ?? 'topright',
      data: {
        versions: this.versions
      }
    });

  }


}
