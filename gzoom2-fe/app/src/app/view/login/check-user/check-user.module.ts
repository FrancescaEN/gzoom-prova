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
import { CheckUserComponent } from './check-user.component';
import { CheckUserRoutingModule } from './check-user-routing.module';
import { LdapService } from 'app/commons/service/ldap.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    CommonsModule,
    ApiModule,
    LayoutModule,
    CheckUserRoutingModule,
    I18nModule,
    ProgressSpinnerModule,
    CardModule,
    PasswordModule,
    InputTextModule,
    ButtonModule,
    AutoFocusModule
  ],
  providers: [
    NodeService,
    LdapService
  ],
  declarations: [
    CheckUserComponent
  ]
})
export class CheckUserModule { }
