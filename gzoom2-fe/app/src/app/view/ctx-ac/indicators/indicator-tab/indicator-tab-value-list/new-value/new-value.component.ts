import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { WorkEffortPurposeTypeService } from 'app/api/service/work-effort-purpose-type.service';
import { WorkEffortPurposeAccountService } from 'app/api/service/work-effort-purpose-account.service';
import { I18NService } from 'app/i18n/i18n.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { SelectItem } from 'primeng/api';
import { MsgService } from 'app/commons/service/message.service';
import { GlAccountMeasRatScService } from 'app/api/service/gl-account-meas-rat-sc.service';
import { UomRatingScaleService } from 'app/api/service/uom-rating-scale.service';
import { dropdownUom, dropdownUomRatingScale } from 'app/commons/utils/dropdownSelectItem';
import { GlAccountMeasRatSc } from 'app/api/model/glAccountMeasRatSc';

@Component({
  selector: 'gzoom-new-value',
  templateUrl: './new-value.component.html',
  styleUrls: ['./new-value.component.scss']
})
export class NewValueComponent implements OnInit {
  glAccountId: string;
  BILING: boolean = this.i18nService.getLanguageType() == "BILING";
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  uomRatingScales: Observable<SelectItem<{ uomId: string, uomRatingValue: number }>[]>;

  form: FormGroup = this.formBuilder.group({
    uomRatingScale: new FormControl<{ uomId: string, uomRatingValue: number }>(null, Validators.required),
    uomCode: new FormControl<string>(null, Validators.required),
    uomCodeLang: new FormControl<string>(null, this.BILING ? Validators.required : Validators.nullValidator),
    uomDescr: new FormControl<string>(null, Validators.required),
    uomDescrLang: new FormControl<string>(null, this.BILING ? Validators.required : Validators.nullValidator),
  })

  get uomRatingScale() { return this.form.get("uomRatingScale") }
  get uomCode() { return this.form.get("uomCode") }
  get uomCodeLang() { return this.form.get("uomCodeLang") }
  get uomDescr() { return this.form.get("uomDescr") }
  get uomDescrLang() { return this.form.get("uomDescrLang") }


  constructor(
    public dialogConfig: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private i18nService: I18NService,
    private formBuilder: FormBuilder,
    private msgService: MsgService,
    private glAccountMeasRatScService: GlAccountMeasRatScService,
    private uomRatingScaleService: UomRatingScaleService
  ) { }

  ngOnInit(): void {
    this.glAccountId = this.dialogConfig.data.glAccountId;

    this.uomRatingScales = this.uomRatingScaleService.getUomRatingScalesExcludingGlAccount(this.glAccountId).pipe(
      map(x => dropdownUomRatingScale(x, this.secondaryLang))
    );
  }

  addAndSave() {
    if (this.form.valid) {
      const { uomRatingScale, ...values } = this.form.value;
      const { uomId, uomRatingValue } = uomRatingScale;
      let gamrs = new GlAccountMeasRatSc();
      gamrs.glAccountId = this.glAccountId;
      gamrs.uomId = uomId;
      gamrs.uomRatingValue = uomRatingValue;

      this.glAccountMeasRatScService.createGlAccountMeasRatSc({ ...gamrs, ...values })
        .subscribe(
          {
            next: () => {
              this.msgService.success(this.i18nService.translate("Added value"));
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
      this.msgService.errorFieldsRequired("error-dialog");
    }

  }

}
