import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom, Observable } from 'rxjs';

const LOGIN_METHOD_ENDPOINT = '/rest/api/getLoginMethod';

export interface KeycloakConfig {
  url: string;
  realm: string;
  clientId: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthModeService {
  private keycloakEnabled = false;
  private keycloakConfig: KeycloakConfig | null = null;

  constructor(private http: HttpClient) { }

  public getLoginMethod(): Observable<string> {
    return this.http.get<string>(LOGIN_METHOD_ENDPOINT);
  }

  public getKeycloakConfig(): KeycloakConfig | null {
    return this.keycloakConfig;
  }

  /**
   * 🔧 Carica la modalità di autenticazione dal backend (o mock).
   * Puoi adattarlo alla tua API, qui c’è un esempio generico.
   */
  async loadAuthMode(): Promise<void> {
    try {
      // ESEMPIO 1: chiamata reale a un endpoint (decommenta se esiste)
      // const response = await this.http.get<{ keycloak: boolean }>('/api/config/auth-mode').toPromise();
      // this.keycloakEnabled = response?.keycloak ?? false;
      const loginMethod = await lastValueFrom(this.getLoginMethod());
      // ESEMPIO 2: mock temporaneo (se non hai ancora l’endpoint)
      if (loginMethod === 'GzoomKeycloakLogin') {
        this.keycloakEnabled = true; // cambia a false per testare login nativo

        // TODO: Sostituire con chiamata reale al backend quando disponibile
        const config = await lastValueFrom(this.http.get<KeycloakConfig>('/rest/api/keycloak-config'));
        this.keycloakConfig = config;

        // MOCK CONFIGURATION per ora
        //this.keycloakConfig = {
        //  url: 'http://localhost:8180/',
        //  realm: 'test',
        //  clientId: 'test-backend'
        //};

        console.log(`🔐 Modalità autenticazione: ${this.keycloakEnabled ? 'Keycloak' : 'Nativa'}`);
      }

    } catch (err) {
      console.warn('⚠️ Errore nel recupero della modalità di autenticazione. Uso fallback: Nativo.', err);
      this.keycloakEnabled = false;
    }
  }

  /**
   * Ritorna lo stato attuale del flag Keycloak.
   */
  isKeycloakEnabled(): boolean {
    return this.keycloakEnabled;
  }
}
