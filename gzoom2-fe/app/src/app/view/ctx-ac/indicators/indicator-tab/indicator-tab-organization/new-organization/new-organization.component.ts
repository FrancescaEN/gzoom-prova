import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { I18NService } from 'app/i18n/i18n.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { SelectItem } from 'primeng/api';
import { findIndex, remove } from 'lodash';
import { MsgService } from 'app/commons/service/message.service';
import { PartyService } from 'app/api/service/party.service';
import { dropdownParty, dropdownPartyEx } from 'app/commons/utils/dropdownSelectItem';
import { DateMoreThanValidator } from 'app/commons/validators/custom-validator';
import { GlAccountOrganizationService } from 'app/api/service/gl-account-organization.service';
@Component({
  selector: 'gzoom-new-organization',
  templateUrl: './new-organization.component.html',
  styleUrls: ['./new-organization.component.scss']
})
export class NewOrganizationComponent implements OnInit {
  party: Observable<SelectItem[]>;
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  selectedItems: SelectItem[] = [];
  scrollable: Observable<boolean> = this.scrollabelTabService.isScrollableWidth(960);
  glAccountId: string;
  loading: boolean = true;
  form: FormGroup = this.formBuilder.group({
    fromDate: new FormControl<Date>(null),
    thruDate: new FormControl<Date>(null, DateMoreThanValidator('fromDate')),
  })
  chip: SelectItem[] = [];

  get fromDate() { return this.form.get("fromDate") }
  get thruDate() { return this.form.get("thruDate") }


  constructor(
    public dialogConfig: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private i18nService: I18NService,
    private scrollabelTabService: ScrollableTabService,
    private formBuilder: FormBuilder,
    private msgService: MsgService,
    private partyService: PartyService,
    private glAccountOrganizationService: GlAccountOrganizationService
  ) { }

  ngOnInit(): void {
    this.glAccountId = this.dialogConfig.data.glAccountId;
    this.party = this.partyService.getByRoleTypeIdAndNotInGlAccount("INTERNAL_ORGANIZATIO", this.glAccountId)
      .pipe(
        map(x => dropdownParty(x, this.secondaryLang)),
        tap(() => this.loading = false)
      )
  }

  setChip(event: { data: SelectItem }) {
    if (this.selectedItems.length > 0)
      this.chip.push(event.data);
    else
      this.chip = [];
  }

  removeChip(event: { data: SelectItem }) {
    const newChip = this.chip.filter(x => x.value !== event.data.value);
    this.chip = [...newChip]
  }

  allChip(event) {
    this.chip = [];
    if (this.selectedItems.length > 0) {
      this.selectedItems.forEach(value => {
        this.chip.push(value)
      })
    }
  }

  resetSelection() {
    this.chip = [];
    this.selectedItems = [];
  }


  addAndSave() {
    if (this.form.valid) {
      this.glAccountOrganizationService.create(this.selectedItems.map(x => x.value), this.glAccountId, this.fromDate.value, this.thruDate.value)
        .subscribe(
          {
            next: () => {
              this.msgService.success(this.i18nService.translate("Added organizations"));
              this.ref.close("reload");
            },
            error: (error) => {
              console.log(error)
              this.msgService.error(error.error.message, "error-dialog");
            }
          }
        )
    }
    else {
      if (this.form.controls['thruDate'].errors != null && this.form.controls['thruDate'].errors['date_more_than']) {

        this.msgService.errorDate(this.fromDate.value, this.thruDate.value, "error-dialog");
      }
      else {
        this.msgService.errorFieldsRequired("error-dialog");
      }
    }

  }
}
