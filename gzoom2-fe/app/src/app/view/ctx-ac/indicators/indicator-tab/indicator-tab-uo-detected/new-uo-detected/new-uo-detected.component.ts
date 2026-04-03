import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { GlAccountRole } from 'app/api/model/glAccountRole';
import { PartyRole } from 'app/api/model/partyRole';
import { GlAccountRoleService } from 'app/api/service/gl-account-role.service';
import { PartyRoleService } from 'app/api/service/party-role.service';
import { RoleTypeService } from 'app/api/service/role-type.service';
import { MsgService } from 'app/commons/service/message.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { dropdownCustomMethod, dropdownRoleType } from 'app/commons/utils/dropdownSelectItem';
import { DateMoreThanValidator } from 'app/commons/validators/custom-validator';
import { I18NService } from 'app/i18n/i18n.service';
import { isEmpty, isNull, omitBy } from 'lodash';
import { FilterMatchMode, FilterOperator, SelectItem } from 'primeng/api';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { BehaviorSubject, Observable, Subject, map, switchMap, takeUntil, tap } from 'rxjs';

@Component({
  selector: 'gzoom-new-uo-detected',
  templateUrl: './new-uo-detected.component.html',
  styleUrls: ['./new-uo-detected.component.scss']
})
export class NewUoDetectedComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  partyRoleList: PartyRole[] = [];
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  selectedItems: PartyRole[] = [];
  scrollable: Observable<boolean> = this.scrollabelTabService.isScrollableWidth(960);
  glAccountId: string;
  loading: boolean = true;
  loadingSave: boolean = false;
  form: FormGroup = this.formBuilder.group({
    fromDate: new FormControl<Date>(null, Validators.required),
    thruDate: new FormControl<Date>(null, DateMoreThanValidator('fromDate')),
  })
  chip: SelectItem[] = [];

  disableResetFilter: boolean = true;
  filterForm: FormGroup = this.formBuilder.group({
    matchModeSearch: new FormControl<string>(FilterMatchMode.CONTAINS, { nonNullable: true }),
    search: new FormControl<string>(null),
    roleTypeId: new FormControl<string[]>(null),
  });

  roleTypeList: SelectItem[];


  //form
  get fromDate() { return this.form.get("fromDate") }
  get thruDate() { return this.form.get("thruDate") }

  //filter
  get search() { return this.filterForm.get('search') }
  get matchModeSearch() { return this.filterForm.get('matchModeSearch') }
  get roleTypeId() { return this.filterForm.get('roleTypeId') }


  constructor(
    public dialogConfig: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private partyRoleService: PartyRoleService,
    private i18nService: I18NService,
    private scrollabelTabService: ScrollableTabService,
    private formBuilder: FormBuilder,
    private roleTypeService: RoleTypeService,
    private glAccountRoleService: GlAccountRoleService,
    private msgService: MsgService
  ) { }

  ngOnInit(): void {
    this.glAccountId = this.dialogConfig.data.glAccountId;
    this.reload$.pipe(
      tap(() => {
        this.loading = true;
        const { matchModeSearch, isSecondaryLang, ...values } = omitBy(this.filterForm.value, isNull);
        this.disableResetFilter = isEmpty(values);

      }),
      switchMap(() => this.partyRoleService.getPartyRole({ ...omitBy(this.filterForm.value, isNull), isSecondaryLang: this.secondaryLang }))
    )
      .pipe(
        takeUntil(this.destroy$),
        map(x => {
          x.forEach(y => {
            y.party.partyName = `${y.party.partyParentRole?.parentRoleCode ?? ''} - ${(this.secondaryLang ? y.party?.partyNameLang : y.party?.partyName) ?? ''}`
          });
          return x;
        })
      ).subscribe(values => {
        this.filterForm.markAsPristine();
        this.partyRoleList = values;
        this.loading = false;
      });

    this.roleTypeService.getRoleTypeByOUAndLikeGOAL()
      .pipe(
        takeUntil(this.destroy$),
        map(x => dropdownRoleType(x, this.secondaryLang))
      ).subscribe(x => this.roleTypeList = x);

  }

  addAndSave() {
    if (this.form.valid) {
      this.loadingSave = true;
      let glAccountRole: GlAccountRole[] = [];
      this.selectedItems.map(x => {
        glAccountRole.push({ glAccountId: this.glAccountId, roleTypeId: x.roleTypeId, partyId: x.partyId, fromDate: this.fromDate.value, thruDate: this.thruDate.value })
      })
      this.glAccountRoleService.createGlAccountRole(glAccountRole)
        .subscribe({
          next: () => {
            this.msgService.success(this.i18nService.translate("Added OU"))
            this.ref.close('reload');
            this.loadingSave = false;
          },
          error: (error) => {
            this.msgService.error(error.error.message ?? error, "error-dialog");
            this.loadingSave = false;

          }
        })
    }
    else {
      this.msgService.errorFieldsRequired("error-dialog");
    }
  }

  setChip(event: { data: PartyRole }) {
    if (this.selectedItems.length > 0) {
      const roleTypeDescr = this.secondaryLang ? event.data.roleType.descriptionLang : event.data.roleType.description;
      this.chip.push({ label: `${event.data.party.partyName} (${roleTypeDescr})`, value: event.data.partyId + event.data.roleTypeId });
    }
    else
      this.chip = [];
  }

  removeChip(event: { data: PartyRole }) {
    const newChipFilter = this.chip.filter(x => x.value !== event.data.partyId + event.data.roleTypeId);
    this.chip = [...newChipFilter]
  }

  allChip(event) {
    //this.selectAll = !this.selectAll;
    this.chip = [];
    if (this.selectedItems.length > 0) {
      this.selectedItems.forEach(partyRole => {
        const roleTypeDescr = this.secondaryLang ? partyRole.roleType.descriptionLang : partyRole.roleType.description;

        this.chip.push({ label: `${partyRole.party.partyName} (${roleTypeDescr})`, value: partyRole.partyId + partyRole.roleTypeId })
      })
    }
  }

  resetFilter() {
    this.filterForm.reset();
    this.filter();
  }
  filter() {
    this._reload.next();
  }

  resetSelection() {
    this.chip = [];
    this.selectedItems = [];
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
  }

}
