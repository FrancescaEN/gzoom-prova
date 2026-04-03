import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { I18nModule } from "app/i18n/i18n.module";
import { ProgressSpinnerModule } from "primeng/progressspinner";
import { CardModule } from "primeng/card";
import { PasswordModule } from "primeng/password";
import { InputTextModule } from "primeng/inputtext";
import { ButtonModule } from "primeng/button";
import { AutoFocusModule } from "primeng/autofocus";
import { LoginCardComponent } from "./login-card.component";
import { CommonsModule } from "app/commons/commons.module";
import { ApiModule } from "app/api/api.module";
import { LayoutModule } from "app/layout/layout.module";
import { LoginRoutingModule } from "../login-routing.module";
import { NodeService } from "app/shared/node.service";
import { LoginCardRoutingModule } from "./login-card-routing.module";
import { RouterModule } from "@angular/router";
import { DialogModule } from "primeng/dialog";
import { MessagesModule } from "primeng/messages";
import { NoteDataService } from "app/api/service/note-data.service";

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
    LoginCardRoutingModule,
    RouterModule,
    DialogModule,
    MessagesModule,
  ],
  providers: [NodeService, NoteDataService],
  declarations: [LoginCardComponent],
})
export class LoginCardModule {}
