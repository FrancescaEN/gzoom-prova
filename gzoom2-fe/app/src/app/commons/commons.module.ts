import { APP_INITIALIZER, ModuleWithProviders, NgModule, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

import { AuthService, AuthServiceConfig } from './service/auth.service';
import { AuthGuard } from './service/guard.service';
import { LockoutConfig, LockoutService } from './service/lockout.service';
import { ApplicationVersion } from './model/config';
import { AsClassPipe, AsIdPipe, FullnamePipe } from './commons.pipe';
import { EnumerationService } from '../api/service/enumeration.service';
import { VersionConfigService } from './service/config.service';
import { firstValueFrom } from 'rxjs';

import {
  ApplicationMajorMinorVersionDirective,
  FromYearDirective
} from './commons.directive';
import { CallbackPipe } from './callback.pipe';
import { KeycloakAuthService } from './service/keycloak-auth.service';
import { AuthModeService } from './service/auth-mode.service';
import { NotificationService } from './service/notification/notification.service';

export interface CommonsConfig {
  /* mandatory attributes */
  application?: ApplicationVersion[];
  /* optional attributes */
  authService?: AuthServiceConfig;
  lockout?: LockoutConfig;
}

export function initializeApp(configService: VersionConfigService) {
  return () => firstValueFrom(configService.loadConfig()).then(config => {
    configService.setConfig(config);
  });
}


@NgModule({
  imports: [CommonModule, HttpClientModule],
  declarations: [
    FullnamePipe,
    AsIdPipe,
    AsClassPipe,
    ApplicationMajorMinorVersionDirective,
    FromYearDirective,
    CallbackPipe
  ],
  exports: [
    FullnamePipe,
    AsIdPipe,
    AsClassPipe,
    ApplicationMajorMinorVersionDirective,
    FromYearDirective,
    CallbackPipe
  ]
})
export class CommonsModule {

  /**
   * Configures the CommonsModule.
   *
   * @param  {CommonsConfig} config Module configuration
   * @return {ModuleWithProviders} The module with the providers
   */
  static forRoot(config: CommonsConfig): ModuleWithProviders<CommonsModule> {
    return {
      ngModule: CommonsModule,
      providers: [
        { provide: AuthServiceConfig, useValue: config.authService },
        { provide: LockoutConfig, useValue: config.lockout },
        {
          provide: APP_INITIALIZER,
          useFactory: initializeApp,
          deps: [VersionConfigService],  // Inject the service as a dependency,
          multi: true
        },
        AuthService, // nome classe funziona come placeholder e quindi lo istanzia
        LockoutService,
        EnumerationService,
        {
          provide: AuthGuard,
          useFactory: () => {
            return new AuthGuard(inject(AuthService), inject(HttpClient), inject(LockoutService), inject(KeycloakAuthService), inject(AuthModeService));
          }

        },
        NotificationService,
        KeycloakAuthService,
        AuthGuard,
        AuthModeService
      ]
    };
  }
}
