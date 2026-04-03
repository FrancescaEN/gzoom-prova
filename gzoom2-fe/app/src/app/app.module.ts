// angular modules
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule, APP_INITIALIZER } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";

// ng-bootstrap modules
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { MessageService } from "primeng/api";

// application modules
import { CommonsModule } from "./commons/commons.module";
import { ApiModule } from "./api/api.module";
import { SharedModule } from "./shared/shared.module";
import { AppRoutingModule } from "./app-routing.module";

import { AppComponent } from "./app.component";

import { HTTP_INTERCEPTORS } from "@angular/common/http";

import { AuthInterceptor } from "./commons/model/auth-interceptor";

import { I18nModule } from "./i18n/i18n.module";

import { LOCALE_ID } from "@angular/core";
import { registerLocaleData } from "@angular/common";
import localeIt from "@angular/common/locales/it";
import localeItExtra from "@angular/common/locales/extra/it";
import { ThemeService } from "./commons/service/theme.service";
import { AuthModeService } from "./commons/service/auth-mode.service";
import { KeycloakAuthService } from "./commons/service/keycloak-auth.service";
import { KeycloakAngularModule } from "keycloak-angular";
import { VersionConfigService } from "./commons/service/config.service";
registerLocaleData(localeIt);
registerLocaleData(localeIt, "it-IT", localeItExtra);
import { FaviconService } from "./commons/service/favicon.service";
import { NgIdleKeepaliveModule } from "@ng-idle/keepalive";

const ROOT_PATH = "../rest";
const GZOOM_PATH = "/gzoom/control/box";

/**
 * Factory per inizializzare Keycloak solo se attivo
 */
export function appInitializerFactory(
  keycloakAuth: KeycloakAuthService,
  authMode: AuthModeService,
  versionConfig: VersionConfigService,
  faviconService: FaviconService,
): () => Promise<void> {
  return async () => {
    await authMode.loadAuthMode();
    await versionConfig.loadConfig();

    if (authMode.isKeycloakEnabled()) {
      console.log("🟢 Modalità Keycloak abilitata");
      await keycloakAuth.initKeycloak();
      if (await keycloakAuth.isLoggedIn()) {
        // ora abbiamo token → aggiorniamo favicon
        faviconService.setFaviconFromServer("/rest/node/logo/Company/ICON");
      }
    } else {
      console.log("⚪ Modalità login nativo (Keycloak disattivato)");
    }
  };
}

@NgModule({
  imports: [
    // angular modules
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    // ng-bootstrap
    NgbModule,
    // application modules
    // > libraries
    //CommonsModule.forRoot({
    //application: { name: 'GZoom2', version: '3.0.0rc2' }
    //application: { name: (window as any).appVersion?.name || 'Unknown', version: (window as any).appVersion?.version || 'Unknown'}
    // , i18n: { rootPath: ROOT_PATH }
    //}),
    CommonsModule.forRoot({}),
    ApiModule.forRoot({ rootPath: ROOT_PATH, gzoomPath: GZOOM_PATH }),
    // I18nModule.forRoot(APP_CONFIG.api),
    I18nModule.forRoot({ rootPath: ROOT_PATH }),
    SharedModule.forRoot(),
    // > routes
    AppRoutingModule,
    KeycloakAngularModule,
    NgIdleKeepaliveModule.forRoot(),
  ],
  declarations: [AppComponent],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    {
      provide: LOCALE_ID,
      useValue: navigator.language,
    },
    ThemeService,
    MessageService,
    // APP_INITIALIZER → inizializza Keycloak SOLO se serve
    {
      provide: APP_INITIALIZER,
      useFactory: appInitializerFactory,
      deps: [
        KeycloakAuthService,
        AuthModeService,
        VersionConfigService,
        FaviconService,
      ],
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
