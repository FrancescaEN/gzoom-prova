import { TableModule } from "primeng/table";
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AccordionModule } from "primeng/accordion"; //accordion and accordion tab
import { DropdownModule } from "primeng/dropdown";
import { DialogModule } from "primeng/dialog";
import { AutoCompleteModule } from "primeng/autocomplete";
import { SharedModule, ConfirmationService } from "primeng/api";
import { ButtonModule } from "primeng/button";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { ToastModule } from "primeng/toast";
import { TooltipModule } from "primeng/tooltip";
import { SpinnerModule } from "primeng/spinner";
import { CardModule } from "primeng/card";
import { CalendarModule } from "primeng/calendar";
import { CommonsModule } from "../../commons/commons.module";
import { ApiModule } from "../../api/api.module";
import { LayoutModule } from "../../layout/layout.module";
import { TimesheetRoutingModule } from "./timesheet-routing.module";
import { TimesheetService } from "../../api/service/timesheet.service";
import { TimeEntryDetailComponent } from "./time-entry/time-entry-detail-table/time-entry-detail.component";
import { PartyService } from "../../api/service/party.service";
import { UomService } from "../../api/service/uom.service";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { I18nModule } from "app/i18n/i18n.module";
import { TimesheetTableComponent } from "../timesheet/timesheet/timesheet-table/timesheet-table.component";
import { ProgressSpinnerModule } from "primeng/progressspinner";
import { MessagesModule } from "primeng/messages";
import { MessageModule } from "primeng/message";
import { RateTypeService } from "app/api/service/rate-type.service";
import { TimeEntryDetailCalendarComponent } from "./time-entry/time-entry-detail-calendar/time-entry-detail-calendar.component";
import { InputNumberModule } from "primeng/inputnumber";
import { LanguageService } from "app/api/service/language.service";
import { TsHolidaysDatesService } from "app/api/service/ts-holidays-dates.service";
import { CanDeactivateGuard } from "app/shared/can-deactivate.guard";

@NgModule({
  imports: [
    NgbModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    CommonsModule,
    ApiModule,
    LayoutModule,
    AccordionModule,
    TableModule,
    DialogModule,
    SharedModule,
    ButtonModule,
    ConfirmDialogModule,
    SpinnerModule,
    CalendarModule,
    ToastModule,
    DropdownModule,
    AutoCompleteModule,
    TooltipModule,
    TimesheetRoutingModule,
    I18nModule,
    CardModule,
    ProgressSpinnerModule,
    MessageModule,
    MessagesModule,
    InputNumberModule,
  ],
  declarations: [
    TimeEntryDetailComponent,
    TimesheetTableComponent,
    TimeEntryDetailCalendarComponent,
  ],
  providers: [
    TsHolidaysDatesService,
    TimesheetService,
    RateTypeService,
    LanguageService,
    PartyService,
    UomService,
    ConfirmationService,
    CanDeactivateGuard
  ],
})
export default class TimesheetModule { }
