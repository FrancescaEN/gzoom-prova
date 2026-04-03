import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { GlAccount } from 'app/api/model/glAccount';
import { GlAccountInputCalc } from 'app/api/model/glAccountInputCalc';
import { GlAccountInputCalcService } from 'app/api/service/gl-account-input-calc.service';
import { MsgService } from 'app/commons/service/message.service';
import { I18NService } from 'app/i18n/i18n.service';
import { SelectItem } from 'primeng/api';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';

@Component({
  selector: 'gzoom-new-parameter',
  templateUrl: './new-parameter.component.html',
  styleUrls: ['./new-parameter.component.scss']
})
export class NewParameterComponent implements OnInit {

  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  glAccountId: string;
  selectedIndicator: GlAccount;
  glFiscalType: SelectItem[] = [];
  form: FormGroup = this.formBuilder.group({
    glFiscalTypeId: new FormControl<string>(null),
    factorCalculator: new FormControl<string>(null, [Validators.required, Validators.maxLength(60)]),
    inputSequenceNum: new FormControl<string>(null, [Validators.required, Validators.maxLength(20)])
  });

  get glFiscalTypeId() { return this.form.get('glFiscalTypeId') }
  get factorCalculator() { return this.form.get('factorCalculator') }
  get inputSequenceNum() { return this.form.get('inputSequenceNum') }


  constructor(
    public dialogConfig: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private readonly i18nService: I18NService,
    private formBuilder: FormBuilder,
    private msgService: MsgService,
    private glAccountInputCalcService: GlAccountInputCalcService
  ) { }

  ngOnInit(): void {
    this.glAccountId = this.dialogConfig.data.glAccountId;
    this.glFiscalType = this.dialogConfig.data.glFiscalType;

  }

  addAndSave() {
    if (this.form.valid) {

      const glAccountInputCalc: GlAccountInputCalc = { ...this.form.value, glAccountId: this.glAccountId, glAccountIdRef: this.selectedIndicator?.glAccountId }
      this.glAccountInputCalcService.create(glAccountInputCalc)
        .subscribe({
          next: (newItem) => {
            this.msgService.success(this.i18nService.translate("Added factor"))
            this.ref.close(newItem);
          },
          error: (error) => {
            this.msgService.error(error.error.message ?? error, "error-dialog-param")
          }
        })
    }
    else {
      this.msgService.errorFieldsRequired("error-dialog-param");
    }
  }



  resetSelection() {
    this.selectedIndicator = null;
  }

}
