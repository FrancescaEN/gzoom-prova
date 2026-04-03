import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, lastValueFrom } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import * as moment from 'moment';
import 'moment-timezone';

import { format, isBlank } from '../commons/model/commons';
import { PrimeNGConfig } from 'primeng/api';
import { AuthService } from 'app/commons/service/auth.service';
import { AuthModeService } from 'app/commons/service/auth-mode.service';
import { KeycloakAuthService } from 'app/commons/service/keycloak-auth.service';


interface Localizations {
  language?: string;
  translations: { [x: string]: string; };
  formats: { [x: string]: string | number | boolean; };
  primeng: { [x: string]: any; };
}

export class I18NConfig {
  rootPath: string;
  localizations?: Localizations;
}

/**
 * Provides a function that when executed will load the localization
 * data and return true when finished.
 * Localization data is copied into the config class.
 *
 * @param  http   Angular Http service
 * @param  config I18N (partial configuration)
 * @param i18nService
 * @param authService
 * @return A function that loads the localization data
 */
export function load(http: HttpClient, config: I18NConfig, i18nService: I18NService, authService: AuthService): () => Promise<boolean> {
  return () => {
    const user = authService.userProfile()?.username;
    const http$ = http.get(`${config.rootPath}/profile/i18n` + (user ? `/${user}/` : ''));
    return lastValueFrom(http$).then(json => {
      config.localizations = json as Localizations;
      i18nService.init(config.localizations);
      return true;
    })
      .catch(err => {
        console.error('No way to get the localization data', err);
        config.localizations = { translations: {}, formats: {}, primeng: {} };
        i18nService.init(config.localizations);
        return true;
      });
  }


  // http
  //   .get(`${config.rootPath}/profile/i18n`)
  //   .toPromise()
  //   .then(json => {
  //     config.localizations = json as Localizations;
  //     i18nService.init(config.localizations);
  //     return true;
  //   })
  //   .catch(err => {
  //     console.error('No way to get the localization data', err);
  //     config.localizations = { translations: {}, formats: {}, primeng: {} };
  //     i18nService.init(config.localizations);
  //     return true;
  //   });
}

const NEW_LINES_RE = /\s*(\r\n|\n|\r)\s*/gm;
const SUFFIX_RE = /^((?:.|\s)+)__\{([-\w]+)\}$/; // see https://regex101.com/r/bZ6iU3/2

/**
 * A service to perform translations and internationalization of strings, numbers and dates.
 */
@Injectable()
export class I18NService {
  private _lang?: string;
  private _t: { [x: string]: string; };
  private _f: { [x: string]: string | number | boolean; };
  private _primeng: { [x: string]: any; };
  private _langType: string;
  private _nameIcon: string[];
  private _secondaryLang: boolean;

  private loggedIn: boolean = false;

  private static removeSuffix(text) {
    const matches = SUFFIX_RE.exec(text);
    return matches ? matches[1] : text;
  }

  constructor(private config: I18NConfig, private http: HttpClient, private configPrimeng: PrimeNGConfig, private authMode: AuthModeService, private keycloak: KeycloakAuthService) { }

  async init(localizations: Localizations) {
    if (this.authMode.isKeycloakEnabled()) {
      const loggedIn = await this.keycloak.isLoggedIn();
      if (!loggedIn) {
        console.log('Utente non loggato → skip i18n init');
        return; // qui blocchi l’inizializzazione
      }
    }
    this.loggedIn = true;
    this._lang = localizations.language;
    this._t = localizations.translations || {};
    this._f = localizations.formats || {};
    this._primeng = localizations.primeng || {}; // for label primeng

    this.configPrimeng.setTranslation(this._primeng);
    this.retrieveLanguageType().pipe(tap(data => {
      this._langType = data;
      this.retrieveIsSecondaryLang();
    })).subscribe();
    this.retrieveNameIconFlag().subscribe(data => this._nameIcon = data.results);
    if (this._lang) {
      // moment.locale(this._lang);
      moment.tz().locale(this._lang);
    }
  }

  /**
   * Translates a label.
   *
   * @param text  The input label, possibly including format placeholders and an
   *              optional suffix to better identify the context.
   *              Placeholders have the following format:
   *                {n} where n is a 0-based index of the value argument
   *              Suffix has the following format:
   *                __{letters} where letters are alphanumeric characters
   * @param args  The remaining parameters are used in value substitution of placeholders
   * @returns The localized string, with placeholders substituted by input values eventually
   */
  translate(text: string, ...args: any[]) {
    const text2 = text?.replace(NEW_LINES_RE, ' '); // sanity fix
    if (!this._t) {
      return '[No i18n] ' + format(I18NService.removeSuffix(text2), ...args);
    }
    const trans = this._t[text2];
    const fmt = !isBlank(trans) ? trans : I18NService.removeSuffix(text2);
    return format(fmt, ...args);
  }

  /**
   * Retrieves the first day of the week supplied with the localization formats or the default one
   * if none was specified.
   *
   * @param defDay The default day of week
   * @returns The default day of the week
   */
  // firstDay(defDay: number): number {
  //   const f: any = this._f['first-day'];
  //   return isFinite(f) ? (isString(f) ? parseInt(f, 10) : f) : defDay;
  // }

  /**
   * Retrieves the short date formats supplied with the localization data or the default one if none
   * was specified.
   *
   * @param defFmt The default short date format.
   * @returns The short date format
   */
  dateShortFormat(defFmt) {
    const f: string = this._f['date-short'] as string;
    return !isBlank(f) ? f : defFmt;
  }

  /**
   * Retrieves the short date-time formats supplied with the localization data or the default one if none
   * was specified.
   *
   * @param defFmt The default short date-time format.
   * @returns The short date-time format
   */
  dateTimeShortFormat(defFmt) {
    const f: string = this._f['date-time-short'] as string;
    return !isBlank(f) ? f : defFmt;
  }

  /**
   * Retrieves the timestamp formats supplied with the localization data or the default one if none
   * was specified.
   *
   * @param defFmt The default timestamp format.
   * @returns The timestamp format
   */
  timestampFormat(defFmt) {
    const f: string = this._f.timestamp as string;
    return !isBlank(f) ? f : defFmt;
  }

  /**
   * Retrieves the decimal-separator, thousand-separator , ecc... supplied with the localization data or the default one if none
   * was specified.
   *
   * @param text The input format, possibly
   * @param args The remaining parameters are used in value substitution of placeholders
   * @returns The localized format, with placeholders substituted by input values eventually
   */
  getFormat(text: string, ...args: any[]) {
    const f: string = this._f[text] as string;
    return !isBlank(f) ? f : args[text];
  }

  /**
   * Retrieves the all labels from the primeng locale
   *
   */
  // getPrimeng(): any {
  //   return this._primeng;
  // }

  getLang(): string {
    return this._lang;
  }

  changeLang(user: string): Promise<void> {
    const http$ = this.http.get(`${this.config.rootPath}/profile/i18n/${user}/`);
    return lastValueFrom(http$).then(json => {
      this.config.localizations = json as Localizations;
      this.init(this.config.localizations);

    })
      .catch(err => {
        console.error('No way to get the localization data', err);
        this.config.localizations = { translations: {}, formats: {}, primeng: {} };
        this.init(this.config.localizations);
      });

    // return this.http
    // .get(`${this.config.rootPath}/profile/i18n/${user}/`)
    // .toPromise()
    // .then(json => {
    //   this.config.localizations = json as Localizations;
    //   this.init(this.config.localizations);

    // })
    // .catch(err => {
    //   console.error('No way to get the localization data', err);
    //   this.config.localizations = { translations: {}, formats: {}, primeng: {} };
    //   this.init(this.config.localizations);
    // });
  }

  getLanguageType() {
    return this._langType;
  }

  getNameIconFlag(): string[] {
    return this._nameIcon;
  }

  getIsSecondaryLang(): boolean {
    return this._secondaryLang;
  }

  // retrieveLanguages() {
  //   return this.http.get(`${this.config.rootPath}/profile/i18n/languages`).pipe(
  //     map(json => json as string[])
  //   );
  // }

  retrieveLanguageType(): Observable<string> {
    if (!this.loggedIn) {
      return new Observable<string>(observer => {
        observer.complete();
      });
    }
    return this.http.get<string>(`${this.config.rootPath}/profile/i18n/language-type`);
  }

  retrieveNameIconFlag() {
    return this.http.get(`${this.config.rootPath}/profile/i18n/languages`).pipe(
      map(json => json as any)
    );
  }

  retrieveIsSecondaryLang() {
    this.http.get(`${this.config.rootPath}/profile/i18n/languages`).pipe(map(json => json as any))
      .subscribe(x => {
        let languages = x.results;
        this._secondaryLang = languages.length > 1 && languages[1].indexOf(this.getLang()) >= 0;
      });
  }
}
