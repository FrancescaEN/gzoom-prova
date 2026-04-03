import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommonsModule } from '../../../commons/commons.module';
import { ApiModule } from '../../../api/api.module';
import { LayoutModule } from '../../../layout/layout.module';
import { NodeService } from '../../../shared/node.service';
import { I18nModule } from 'app/i18n/i18n.module';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { CardModule } from 'primeng/card';
import { PasswordModule } from "primeng/password";
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { AutoFocusModule } from 'primeng/autofocus';
import { LdapService } from 'app/commons/service/ldap.service';
import { DialogModule } from 'primeng/dialog';
import { ContactMechService } from 'app/api/service/contact-mech.service';
import { Login2fauthRoutingModule } from './login-2fauth-routing.module';
import { Login2fauthComponent } from './login-2fauth.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    CommonsModule,
    ApiModule,
    LayoutModule,
    I18nModule,
    ProgressSpinnerModule,
    CardModule,
    PasswordModule,
    InputTextModule,
    ButtonModule,
    AutoFocusModule,
    DialogModule,
    Login2fauthRoutingModule
  ],
  providers: [
    NodeService,
    LdapService,
    ContactMechService
  ],
  declarations: [
    Login2fauthComponent
  ]
})
export default class TwoFactorAuthModule { }
