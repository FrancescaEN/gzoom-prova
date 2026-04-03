import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonsModule } from '../commons/commons.module';
import { ApiModule } from '../api/api.module';
import { SharedModule } from '../shared/shared.module';
// import { ConfirmationService, FilterService, MessageService } from 'primeng/api';
import { ConfirmationService, FilterService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TreeTableModule } from 'primeng/treetable';
import { DropdownModule } from 'primeng/dropdown';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EqualValidator } from '../commons/equal-validator.directive';
import { I18nModule } from 'app/i18n/i18n.module';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { ToastModule } from 'primeng/toast';
import { SlideMenuModule } from 'primeng/slidemenu';
import { layoutComponents } from '.';
import { ToolbarModule } from 'primeng/toolbar';
import { NgxGaugeModule } from 'ngx-gauge';
import { ChartComponent } from './chart/chart.component';
import { ChartModule } from 'primeng/chart';
import { ReportDownloadService } from 'app/api/service/report-download.service';
import { ProgressBarModule } from 'primeng/progressbar';
import { SidebarModule } from 'primeng/sidebar';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { IconCustomComponent } from './icon-custom/icon-custom.component';
import { LanguageService } from '../api/service/language.service';
import { TableCalendarComponent } from './tables/table-calendar-timesheet/table-calendar.component';
import { TableEditingCellComponent } from './tables/table-editing-cell/table-editing-cell.component';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { InformationBarComponent } from './information-bar/information-bar.component';
import { RadioButtonComponent } from './radio-button/radio-button.component';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ConfirmDialogService } from '../commons/service/confirm-dialog.service'
import { CalendarModule } from 'primeng/calendar';
import { TableEditingCellService } from 'app/commons/service/table-editing-cell.service';
import { PrimaryBoardComponent } from './primary-board/primary-board.component';
import { MessagesModule } from 'primeng/messages';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { SpinnerComponent } from './spinner/spinner.component';
import { DialogChartComponent } from './dialog-chart/dialog-chart.component';
import { TextEditorModule } from './text-editor/text-editor.module';
import { UploadModule } from './upload/upload.module';
import { TableEditingCellPaginationComponent } from './tables/table-editing-cell-pagination/table-editing-cell-pagination.component';
import { ScrollerModule } from 'primeng/scroller';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { DialogComponent } from './dialog/dialog.component';
import { DialogGridImageComponent } from './dialog-grid-image/dialog-grid-image.component';
import { DialogService, DynamicDialogModule } from 'primeng/dynamicdialog';
import { TagModule } from 'primeng/tag';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { OverlayScreenComponent } from './overlay-screen/overlay-screen.component';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ToolbarService } from 'app/commons/service/toolbar.service';
import { MsgService } from 'app/commons/service/message.service';
import { AccordionModule } from 'primeng/accordion';
import { MultiSelectModule } from 'primeng/multiselect';
import { ChipModule } from 'primeng/chip';
import { MenuModule } from 'primeng/menu';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';
import { GzoomTabMenuComponent } from './gzoom-tab-menu/gzoom-tab-menu.component';
import { NgOptimizedImage } from '@angular/common';
import { AutoFocusModule } from 'primeng/autofocus';
import { HeaderService } from 'app/api/service/header.service';
import { GzoomChangeThemeComponent } from './header/gzoom-change-theme/gzoom-change-theme.component';
import { DefaultOrderKeyValuePipe } from 'app/commons/pipe/default-order-key-value.pipe';
import { FormatPercentNumberStylePipe } from 'app/commons/pipe/formatPercentNumberStyle.pipe';

// import {GzoomLabelComponent} from "./form/gzoom-label/gzoom-label.component";

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    CommonsModule,
    ApiModule,
    SharedModule,
    NgbModule,
    DialogModule,
    ToastModule,
    DropdownModule,
    TreeTableModule,
    ProgressSpinnerModule,
    FormsModule,
    ReactiveFormsModule,
    ButtonModule,
    I18nModule,
    BreadcrumbModule,
    TableModule,
    ToastModule,
    SlideMenuModule,
    ToolbarModule,
    NgxGaugeModule,
    ChartModule,
    ProgressBarModule,
    SidebarModule,
    OverlayPanelModule,
    InputNumberModule,
    InputTextModule,
    CardModule,
    RadioButtonModule,
    CalendarModule,
    MessagesModule,
    ConfirmDialogModule,
    TextEditorModule,
    UploadModule,
    ScrollerModule,
    ScrollPanelModule,
    DynamicDialogModule,
    AutoCompleteModule,
    TagModule,
    InputTextareaModule,
    AccordionModule,
    MultiSelectModule,
    ChipModule,
    MenuModule,
    TabViewModule,
    TabMenuModule,
    NgOptimizedImage,
    AutoFocusModule,
    DefaultOrderKeyValuePipe
    // GzoomLabelComponent
  ],
  declarations: [
    EqualValidator,
    layoutComponents,
    ChartComponent,
    IconCustomComponent,
    TableCalendarComponent,
    TableEditingCellComponent,
    TableEditingCellPaginationComponent,
    InformationBarComponent,
    RadioButtonComponent,
    PrimaryBoardComponent,
    SpinnerComponent,
    DialogChartComponent,
    FormatPercentNumberStylePipe,
    DialogComponent,
    DialogGridImageComponent,
    OverlayScreenComponent,
    GzoomTabMenuComponent,
    GzoomChangeThemeComponent
  ],
  exports: [
    layoutComponents,
    CommonModule,
    RouterModule,
    CommonsModule,
    ApiModule,
    SharedModule,
    NgbModule,
    DialogModule,
    ToastModule,
    DropdownModule,
    TreeTableModule,
    ProgressSpinnerModule,
    FormsModule,
    ReactiveFormsModule,
    ButtonModule,
    I18nModule,
    BreadcrumbModule,
    TableModule,
    ToastModule,
    SlideMenuModule,
    ToolbarModule,
    NgxGaugeModule,
    ChartModule,
    ProgressBarModule,
    SidebarModule,
    OverlayPanelModule,
    InputNumberModule,
    InputTextModule,
    CardModule,
    RadioButtonModule,
    CalendarModule,
    MessagesModule,
    ConfirmDialogModule,
    TextEditorModule,
    UploadModule,
    ScrollerModule,
    ScrollPanelModule,
    DynamicDialogModule,
    AutoCompleteModule,
    TagModule,
    InputTextareaModule,
    AccordionModule,
    MultiSelectModule,
    ChipModule,
    MenuModule,
    TabViewModule,
    TabMenuModule,
    NgOptimizedImage,
    AutoFocusModule
  ],
  providers: [
    FilterService,
    TableEditingCellService,
    ReportDownloadService,
    // MessageService,
    LanguageService,
    ConfirmDialogService,
    DialogService,
    ConfirmationService,
    ToolbarService,
    MsgService,
    HeaderService
  ]
})

export class LayoutModule {}

