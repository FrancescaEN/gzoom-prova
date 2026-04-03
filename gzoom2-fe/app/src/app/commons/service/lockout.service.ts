import { Injectable, Optional } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { AuthService } from './auth.service';
import { AuthModeService } from './auth-mode.service';
import { KeycloakAuthService } from './keycloak-auth.service';

const LOGIN_ROUTE = '/login';

export class LockoutConfig {
  loginRoute?: string;
}

@Injectable()
export class LockoutService {
  private loginRoute: string = LOGIN_ROUTE;
  public readonly events = new Subject<void>();

  constructor(
    private router: Router,
    private authService: AuthService,
    private authMode: AuthModeService,
    private keycloakAuth: KeycloakAuthService,
    @Optional() config?: LockoutConfig
  ) {
    if (config?.loginRoute) {
      this.loginRoute = config.loginRoute;
    }
  }

  async lockout(url?: string) {
    this.events.next();
    this.authService.lockout();

    if (this.authMode.isKeycloakEnabled()) {
      // ✅ Nessun redirect manuale, lascia gestire a Keycloak
      await this.keycloakAuth.logout();
    } else {
      const extras = url ? { queryParams: { returnUrl: url } } : undefined;
      this.router.navigate([this.loginRoute], extras);
    }
  }

  lockoutLogout() {
    this.events.next();
    this.authService.lockout();
  }

  async returnToLogin() {
    if (this.authMode.isKeycloakEnabled()) {
      // Stessa logica: lascia gestire il flusso al logout Keycloak
      await this.keycloakAuth.logout();
    } else {
      this.router.navigate([this.loginRoute]);
    }
  }
}
