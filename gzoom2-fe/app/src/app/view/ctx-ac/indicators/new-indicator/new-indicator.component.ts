import { Component, OnDestroy, OnInit, Signal, effect, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { I18NService } from 'app/i18n/i18n.service';
import { SelectItem } from 'primeng/api';
import { DialogService, DynamicDialogConfig, DynamicDialogInjector, DynamicDialogRef } from 'primeng/dynamicdialog';
import { DetectionMode } from '../indicators.component';
import { UomService } from 'app/api/service/uom.service';
import { dropdownPartyEx, dropdownRoleType, dropdownUom } from 'app/commons/utils/dropdownSelectItem';
import { Observable, Subject, distinctUntilChanged, filter, map, of, switchMap, takeUntil, tap } from 'rxjs';
import { orderBy } from 'lodash';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { GlAccount } from 'app/api/model/glAccount';
import { MsgService } from 'app/commons/service/message.service';
import { Permission, UserPermissionService } from 'app/shared/user-permission.service';
import { Context } from 'app/commons/enum/context';
import { ActivatedRoute, Route } from '@angular/router';
import { RoleTypeService } from 'app/api/service/role-type.service';
import { PartyService } from 'app/api/service/party.service';

@Component({
  selector: 'gzoom-new-indicator',
  templateUrl: './new-indicator.component.html',
  styleUrls: ['./new-indicator.component.scss']
})
export class NewIndicatorComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  BILING: boolean = this.i18nService.getLanguageType() == "BILING";
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  visible: boolean = true;
  activeIndex: number[] = [0, 1, 2];
  form: FormGroup = this.formBuilder.group({
    accountName: new FormControl<string>(null, Validators.required),
    accountNameLang: new FormControl<string>(null, this.BILING ? Validators.required : Validators.nullValidator),
    accountCode: new FormControl<string>(null, Validators.maxLength(100)),
    sequenceId: new FormControl<number>(null, [Validators.maxLength(20)]),
    description: new FormControl<string>(null),
    descriptionLang: new FormControl<string>(null),
    glAccountTypeId: new FormControl<string>(null, Validators.required),
    workEffortPurposeTypeId: new FormControl<string[]>(null),
    detectionMode: new FormControl<DetectionMode>(null, Validators.required),
    defaultUomId: new FormControl<string>(null, Validators.required),
    periodTypeId: new FormControl<string>(null, Validators.required),
    respCenterRoleTypeId: new FormControl<string>(null),
    respCenterId: new FormControl<string>(null),
  });
  accountTypeEnumId: string;
  isReservedAccount: string;

  glAccountType: SelectItem[];
  workEffortPurposeType: SelectItem[];
  detectionModeItems: SelectItem<DetectionMode>[];
  periodType: SelectItem[];
  uom$ = this.uomService.uoms().pipe(map(x => dropdownUom(x, this.secondaryLang)));

  isRespSignal = toSignal(
    this.userPermissionService.hasPermission(Context.CTX_AC, Permission.RESP)
      .pipe(
        filter(isResp => isResp === true),
        tap(isResp => {
          this.respCenterId.setValidators(Validators.required);
          this.respCenterRoleTypeId.setValidators(Validators.required);
          this.activeIndex = [0, 1, 2, 3];
        })
      )
  );
  respCenterRoleType$: Observable<SelectItem[]>;
  respCenterId$: Observable<SelectItem[]>;


  get accountName() { return this.form.get("accountName") }
  get accountNameLang() { return this.form.get("accountNameLang") }
  get accountCode() { return this.form.get("accountCode") }
  get sequenceId() { return this.form.get("sequenceId") }
  get description() { return this.form.get("description") }
  get descriptionLang() { return this.form.get("descriptionLang") }
  get glAccountTypeId() { return this.form.get("glAccountTypeId") }
  get workEffortPurposeTypeId() { return this.form.get("workEffortPurposeTypeId") }
  get detectionMode() { return this.form.get("detectionMode") }
  get defaultUomId() { return this.form.get("defaultUomId") }

  get respCenterRoleTypeId() { return this.form.get("respCenterRoleTypeId") }
  get respCenterId() { return this.form.get("respCenterId") }

  constructor(
    private i18nService: I18NService,
    private formBuilder: FormBuilder,
    public dialogConfig: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private uomService: UomService,
    private glAccountService: GlAccountService,
    private msgService: MsgService,
    private userPermissionService: UserPermissionService,
    private route: ActivatedRoute,
    private roleTypeService: RoleTypeService,
    private partyService: PartyService
  ) {

    effect(() => {
      if (this.isRespSignal()) {
        this.respCenterRoleType$ = this.roleTypeService.getRoleTypeByParentTypeId("ORGANIZATION_UNIT").pipe(
          map(x => dropdownRoleType(x, this.secondaryLang))
        );


        this.respCenterId$ = this.respCenterRoleTypeId.valueChanges.pipe(
          distinctUntilChanged(),
          switchMap((value) => {
            if (value) return this.partyService.getUOGestoreByRespCenterRoleTypeId(Context.CTX_AC, value)
            return of([])
          }),
          map(x => dropdownPartyEx(x, this.secondaryLang))
        );

        this.respCenterRoleTypeId.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(
          value => {
            if (value) {
              this.respCenterId.enable()
            }
            else {
              this.respCenterId.setValue(null);
              this.respCenterId.disable()
            }
          }
        )
      }
    })
  }
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
  }

  ngOnInit(): void {

    this.glAccountType = this.dialogConfig.data.glAccountType;
    this.workEffortPurposeType = this.dialogConfig.data.workEffortPurposeType;
    this.detectionModeItems = this.dialogConfig.data.detectionModeItems;
    this.periodType = this.dialogConfig.data.periodType;
    this.accountTypeEnumId = this.dialogConfig.data.accountTypeEnumId;
    this.isReservedAccount = this.dialogConfig.data.isReservedAccount;

    //  this.form =
  }

  create() {
    if (this.form.valid) {
      const { detectionMode, workEffortPurposeTypeId, ...values } = this.form.value;
      let glAccount: GlAccount = new GlAccount();
      glAccount = { ...glAccount, ...values };
      glAccount.accountTypeEnumId = this.accountTypeEnumId;
      glAccount.detectOrgUnitIdFlag = detectionMode.detectOrgUnitIdFlag;
      glAccount.inputEnumId = detectionMode.inputEnumId;
      // console.log(glAccount)
      this.glAccountService.createGlAccount(glAccount, workEffortPurposeTypeId)
        .subscribe({
          next: newGlAccount => {
            this.ref.close(newGlAccount);
          },
          error: err => {
            this.msgService.error(err.error.message, "error-dialog");
          }
        }

        )
    }
    else {
      this.msgService.error(this.i18nService.translate(
        "All mandatory fields must be filled in"
      ), "error-dialog")
    }
  }
}
