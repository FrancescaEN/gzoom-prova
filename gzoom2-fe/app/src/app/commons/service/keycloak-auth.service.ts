// src/app/commons/service/keycloak-auth.service.ts
import { Injectable } from '@angular/core';
import { KeycloakEventType, KeycloakService } from 'keycloak-angular';
import { AuthModeService } from './auth-mode.service';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

import { Idle, DEFAULT_INTERRUPTSOURCES } from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';

@Injectable({
  providedIn: 'root'
})
export class KeycloakAuthService {
  private keycloakEnabled = false;
  private userIsIdle = false;


  //private readonly IDLE_TIME = 45;   // secondi di idle
  private readonly TIMEOUT_TIME = 20;     // secondi countdown "tolleranza" dopo idle
  
  
  // Dopo 10 secondi idle ed inizio del timeout che scadrà a 25 secondi (idle + timeout) e butterà fuori l'utente
  // Con il token di 30 secondi, avremo che a 20 secondi tenta il refresh ma che se siamo in idle venga saltato 
  // e venga buttato fuori. Se invece no idle viene refreshato il token.
  
  // token piu' lungo -> viene buttato fuori ad idle + timout

  
  constructor(
    private keycloak: KeycloakService,
    private authMode: AuthModeService,
    private router: Router,
    private authService: AuthService,
    private idle: Idle,
    private keepalive: Keepalive
  ) { }
  

  /**
   * Inizializza Keycloak solo se abilitato dal backend o config.
   */
  async initKeycloak(): Promise<void> {
    // Recupera il flag dal servizio
    await this.authMode.loadAuthMode();
    this.keycloakEnabled = this.authMode.isKeycloakEnabled();

    if (!this.keycloakEnabled) {
      console.log('⚪ Login nativo attivo — Keycloak non inizializzato.');
      return Promise.resolve();
    }

    const config = this.authMode.getKeycloakConfig();
    if (!config) {
      console.error('❌ Configurazione Keycloak mancante! Impossibile inizializzare.');
      return Promise.resolve();
    }

    try {
      const authenticated = await this.keycloak.init({
        config: {
          url: config.url,
          realm: config.realm,
          clientId: config.clientId
        },
        initOptions: {
          onLoad: 'check-sso', // forza la pagina di login Keycloak
          pkceMethod: 'S256',
          checkLoginIframe: false
        },
        enableBearerInterceptor: true,
        bearerPrefix: 'Bearer',
        bearerExcludedUrls: [
          '/assets', '/favicon.ico', '/api/config'
        ]
      });

      if (this.keycloakEnabled) {
        this.configureIdle();

        this.keycloak.keycloakEvents$
          .subscribe(async event => {
            if (event.type === KeycloakEventType.OnTokenExpired) {

             const now = Date.now();
             const nowStr = new Date(now).toLocaleString();

             // Scadenza del token (timestamp in secondi → ms)
             const tokenExpiryUnix = this.keycloak.getKeycloakInstance().tokenParsed?.exp ?? 0;
             const tokenExpiry = tokenExpiryUnix * 1000;
             const tokenExpiryStr = new Date(tokenExpiry).toLocaleString();

             console.log(`🔄 Token scaduto!`);
             console.log(`   Ora attuale: ${nowStr}`);
             console.log(`   Scadenza token: ${tokenExpiryStr}`);  
             console.log(`   Tempo passato dalla scadenza (ms): ${now - tokenExpiry}`);

             if (!this.userIsIdle) {
                await this.safeRefresh();
              } else {
                console.log('⛔ Token expired ma utente idle token non rinnovato...');
              }
            }
          });
      }

      console.log('🟢 Keycloak inizializzato correttamente');
      if (authenticated && this.router !== null && this.router.url === '/') {
        const token = await this.keycloak.getToken();
        const idtoken = await this.getIdToken();

        //console.log('🔑 Token :' + token);
        //console.log('🔑 idtoken :' + idtoken);

        this.authService.save(token, true);
        this.router.navigate(['/c/dashboard']);

      }
    } catch (error) {
      console.error('❌ Errore inizializzazione Keycloak', error);
      throw error;
    }
  }

  // ---------------- IDLE CONFIG ----------------

  private configureIdle() {
    const keycloakInstance: any = (this.keycloak as any).getKeycloakInstance();
    if (!keycloakInstance || !keycloakInstance.refreshTokenParsed) {
      console.warn('Keycloak instance non disponibile per idle timing');
      return;
    }
    // tempo rimanente in secondi
    const refreshRemaining = keycloakInstance.refreshTokenParsed.exp - Math.floor(Date.now() / 1000);
    console.log('🕒 Refresh remaining: ' + refreshRemaining + 's');

    this.idle.setIdle(refreshRemaining);
    this.idle.setTimeout(this.TIMEOUT_TIME);
    this.idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);

    this.keepalive.interval(refreshRemaining);

    this.keepalive.onPing.subscribe(() => {
      if (!this.userIsIdle) {
        this.safeRefresh();
      }
    });

    this.idle.onIdleStart.subscribe(() => {
      this.userIsIdle = true;
      console.log('🟡 Utente inattivo');
    });

    this.idle.onIdleEnd.subscribe(() => {
      this.userIsIdle = false;
      console.log('🟢 Utente tornato attivo');
      this.safeRefresh();
    });

    this.idle.onTimeoutWarning.subscribe((countdown) => {
      console.log(`⏳ Logout tra ${countdown}s`);
    });

    this.idle.onTimeout.subscribe(() => {
      console.log('🔴 Timeout idle -> logout');
      this.logout();
    });

    this.idle.watch();
  }

  // ---------------- REFRESH SAFE ----------------

  private async safeRefresh() {
    if (!this.keycloakEnabled) return;

    if (this.userIsIdle) {
      console.log('⛔ Skip refresh: utente idle');
      return;
    }

    try {
      // aggiorno il token
      console.log("- token update...");
      const refreshed = await this.keycloak.updateToken(0);
      if (refreshed) {
        // recupero il token aggiornato e lo persisto
        const token = await this.keycloak.getToken();
        this.authService.save(token, true);
        console.log('🔄 Token refreshato e persistito');
      }
    } catch {
      console.error('❌ Refresh fallito');
      await this.logout();
    }
  }

  // ---------------- API PUBBLICA ----------------

  async login(): Promise<void> {
    if (this.keycloakEnabled) {
      return this.keycloak.login();
    } else {
      console.log('⚪ Keycloak disabilitato — uso login nativo.');
      return Promise.resolve();
    }
  }

  async logout(): Promise<void> {
    console.log('- keycloak logout...');
    this.idle.stop();
    if (this.keycloakEnabled) {
      localStorage.removeItem('auth-token');
      return this.keycloak.logout(window.location.origin);
    } else {
      console.log('⚪ Logout locale (Keycloak disattivato).');
      localStorage.removeItem('auth-token');
      return Promise.resolve();
    }
  }

  async getToken(): Promise<string> {
    if (this.keycloakEnabled) {
      return await this.keycloak.getToken();
    } else {
      return localStorage.getItem('auth-token') ?? '';
    }
  }

  async getIdToken(): Promise<string> {
    const keycloakInstance: any = (this.keycloak as any).getKeycloakInstance();
    return keycloakInstance.idToken;
  }

  async isLoggedIn(): Promise<boolean> {
    if (this.keycloakEnabled) {
      return await this.keycloak.isLoggedIn();
    } else {
      return !!localStorage.getItem('auth-token');
    }
  }


  async changePassword(): Promise<void> {
    if (!this.keycloakEnabled) {
      console.log('- Keycloak disabilitato — cambio password non disponibile.');
      return;
    }

    const config = this.authMode.getKeycloakConfig();
   
    //const redirectUri = window.location.origin + '/gzoom2/c/dashboard'; 
    const redirectUri = window.location.origin + '/gzoom2/c/dashboard?from=kc-update-password';

    const url =
      `${config.url}/realms/${config.realm}/protocol/openid-connect/auth` +
      `?client_id=${config.clientId}` +
      `&redirect_uri=${encodeURIComponent(redirectUri)}` +
      `&response_type=code` +
      `&scope=openid` +
      `&kc_action=UPDATE_PASSWORD`;

    console.log('- Redirect cambio password:', url);

    window.location.href = url;
  }


}
