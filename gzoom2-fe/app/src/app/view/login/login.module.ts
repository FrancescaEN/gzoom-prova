import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommonsModule } from '../../commons/commons.module';
import { ApiModule } from '../../api/api.module';
import { LayoutModule } from '../../layout/layout.module';
import { LoginRoutingModule } from './login-routing.module';
import { NodeService } from '../../shared/node.service';
import { LoginComponent } from './login.component';
import { I18nModule } from 'app/i18n/i18n.module';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { CardModule } from 'primeng/card';
import { PasswordModule } from "primeng/password";
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { AutoFocusModule } from 'primeng/autofocus';
import { RouterModule } from '@angular/router';
import { ResetPasswordComponent } from './reset-password/reset-password.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    CommonsModule,
    ApiModule,
    LayoutModule,
    LoginRoutingModule,
    I18nModule,
    ProgressSpinnerModule,
    CardModule,
    PasswordModule,
    InputTextModule,
    ButtonModule,
    AutoFocusModule,
    RouterModule
  ],
  providers: [
    NodeService
  ],
  declarations: [
    LoginComponent,
    ResetPasswordComponent,
  ]
})
export class LoginModule { }
