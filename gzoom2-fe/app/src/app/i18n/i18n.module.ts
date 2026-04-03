// angular
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HttpClient } from '@angular/common/http';

// i18n
import { I18NService, I18NConfig, load } from './i18n.service';
import {
  I18NPipe,
  I18NNumPipe,
  I18NParsePipe,
  I18NDatePipe,
  I18NDateTimePipe,
  I18NTimestampPipe,
  I18NIconFlagNamePipe,
  I18NLangTypePipe
} from './i18n.pipe';
import { ModuleWithProviders } from '@angular/core';
import { AuthService } from 'app/commons/service/auth.service';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule
  ],
  declarations: [
    I18NPipe,
    I18NNumPipe,
    I18NParsePipe,
    I18NDatePipe,
    I18NDateTimePipe,
    I18NTimestampPipe,
    I18NIconFlagNamePipe,
    I18NLangTypePipe
  ],
  exports: [
    I18NPipe,
    I18NNumPipe,
    I18NParsePipe,
    I18NDatePipe,
    I18NDateTimePipe,
    I18NTimestampPipe,
    I18NIconFlagNamePipe,
    I18NLangTypePipe
  ]
})
export class I18nModule {
  static forRoot(config: I18NConfig): ModuleWithProviders<I18nModule> {
    return {
      ngModule: I18nModule,
      providers: [
        { provide: I18NConfig, useValue: config },
        I18NService,
        /* next line loads the i18n configuration from server during bootstrap */
        { provide: APP_INITIALIZER, useFactory: load, deps: [HttpClient, I18NConfig, I18NService, AuthService], multi: true }
      ]
    };
  }
}

