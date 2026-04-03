import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { GlAccount } from 'app/api/model/glAccount';
import { GlAccountInputCalc } from 'app/api/model/glAccountInputCalc';
import { GlAccountInputCalcService } from 'app/api/service/gl-account-input-calc.service';
import { GlAccountService } from 'app/api/service/gl-account.service';
import { MsgService } from 'app/commons/service/message.service';
import { I18NService } from 'app/i18n/i18n.service';
import { SelectItem } from 'primeng/api';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { Subscription } from 'rxjs';

@Component({
  selector: 'gzoom-update-operand',
  templateUrl: './update-operand.component.html',
  styleUrls: ['./update-operand.component.scss']
})
export class UpdateOperandComponent implements OnInit, OnDestroy {

  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  glAccountIdRef: string;
  selectedIndicator: GlAccount;
  glAccountInputCalcId: string;
  glAccount$: Subscription;

  constructor(
    public dialogConfig: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private readonly i18nService: I18NService,
    private msgService: MsgService,
    private glAccountInputCalcService: GlAccountInputCalcService,
    private glAccountService: GlAccountService
  ) { }


  ngOnInit(): void {
    this.glAccountIdRef = this.dialogConfig.data.glAccountIdRef;
    this.glAccountInputCalcId = this.dialogConfig.data.glAccountInputCalcId;
    this.glAccount$ = this.glAccountService.getGlAccount(this.glAccountIdRef).subscribe(x => this.selectedIndicator = x)

  }

  save() {


    this.glAccountInputCalcService.updateGlAccountIdRef(this.glAccountInputCalcId, this.selectedIndicator?.glAccountId)
      .subscribe({
        next: (result) => {
          this.msgService.successUpdate()
          this.ref.close("reload");
        },
        error: (error) => {
          this.msgService.error(error.error.message ?? error, "error-dialog-param")
        }
      })

  }

  resetSelection() {
    this.selectedIndicator = null;
  }

  ngOnDestroy(): void {
    this.glAccount$?.unsubscribe();
  }


}
