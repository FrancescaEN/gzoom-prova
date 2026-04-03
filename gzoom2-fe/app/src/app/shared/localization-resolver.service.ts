import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';
import { AuthService } from '../commons/service/auth.service';
import { I18NService } from 'app/i18n/i18n.service';
import { AuthModeService } from 'app/commons/service/auth-mode.service';
import { KeycloakAuthService } from 'app/commons/service/keycloak-auth.service';

/**
 * Retrieves the localization to be shown or locks the user out if something wrong happens.
 */
export const localizationResolver: ResolveFn<void> = async (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const i18Service = inject(I18NService);
  const authMode = inject(AuthModeService);
  const keycloak = inject(KeycloakAuthService);

  // Se Keycloak è abilitato, aspetta che l'utente sia loggato
  if (authMode.isKeycloakEnabled()) {
    const loggedIn = await keycloak.isLoggedIn();
    if (!loggedIn) {
      console.log('Utente non loggato → skip i18n init');
      return; // qui blocchi l’inizializzazione
    }
  }

  // Modalità login nativo o Keycloak loggato
  const usr = authService.userProfile();
  if (!usr) return;

  // Ora è sicuro chiamare changeLang
  await i18Service.changeLang(usr.username)
    .catch(err => console.warn('Cannot resolve localization', err));
};
