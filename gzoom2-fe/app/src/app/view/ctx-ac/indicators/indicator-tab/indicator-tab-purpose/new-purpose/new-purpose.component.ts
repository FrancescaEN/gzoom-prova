import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { Purpose } from '../indicator-tab-purpose.component';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { WorkEffortPurposeTypeService } from 'app/api/service/work-effort-purpose-type.service';
import { WorkEffortPurposeAccountService } from 'app/api/service/work-effort-purpose-account.service';
import { I18NService } from 'app/i18n/i18n.service';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { SelectItem } from 'primeng/api';
import { MsgService } from 'app/commons/service/message.service';

@Component({
  selector: 'gzoom-new-purpose',
  templateUrl: './new-purpose.component.html',
  styleUrls: ['./new-purpose.component.scss']
})
export class NewPurposeComponent implements OnInit {
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  scrollable: Observable<boolean> = this.scrollabelTabService.isScrollableWidth(960);
  disableSave: boolean = true;
  loading: boolean = true;
  purpose: Purpose[] = [];
  glAccountId: string;
  updatedPurpose: string[] = [];
  chipFilter: SelectItem[] = [];
  selectedPurpose: Purpose[] = [];
  selectAll: boolean = false;
  form: FormGroup = this.formBuilder.group({
    comments: new FormControl<string>(null)
  })

  get comments() { return this.form.get("comments") }

  constructor(
    public dialogConfig: DynamicDialogConfig,
    public ref: DynamicDialogRef,
    private workEffortPurposeTypeService: WorkEffortPurposeTypeService,
    private workEffortPurposeAccountService: WorkEffortPurposeAccountService,
    private i18nService: I18NService,
    private scrollabelTabService: ScrollableTabService,
    private formBuilder: FormBuilder,
    private msgService: MsgService
  ) { }

  ngOnInit(): void {
    this.glAccountId = this.dialogConfig.data.glAccountId;
    this.workEffortPurposeTypeService.getPurposeTabType(this.glAccountId, false).pipe(
      tap(() => {
        this.updatedPurpose = [];
      }),
      map(values => {
        let tmpPurpose = [];
        while (values.length > 0) {
          const workEffortPurposeTypeId = values[0].workEffortPurposeType.workEffortPurposeTypeId;
          const filterValues = values.filter(x => x.workEffortPurposeType.workEffortPurposeTypeId === workEffortPurposeTypeId);

          const arrayLabel = filterValues.map(x => {
            if (x.workEffortType) {
              if (this.secondaryLang) {
                return `${x.workEffortType.descriptionLang} ${x.parentWorkEffortType ? '(' + x.parentWorkEffortType.descriptionLang + ')' : ''}`
              }
              else {
                return `${x.workEffortType.description} ${(x.parentWorkEffortType ? '(' + x.parentWorkEffortType.description + ')' : '')}`
              }
            }

          }).filter(y => y);
          tmpPurpose.push({
            id: workEffortPurposeTypeId,
            description: values[0].workEffortPurposeType.description,
            usedIn: arrayLabel,
          })
          values = values.filter(x => x.workEffortPurposeType.workEffortPurposeTypeId != workEffortPurposeTypeId)
        }

        return tmpPurpose;
      })
    )
      .subscribe(values => {
        this.purpose = values
        this.loading = false;

      });
  }

  addAndSave() {

    this.workEffortPurposeAccountService.create(this.selectedPurpose.map(x => x.id), this.glAccountId, this.comments.value)
      .subscribe(
        {
          next: () => {
            this.msgService.success(this.i18nService.translate("Added purposes"));
            this.ref.close("reload");
          },
          error: (error) => {
            console.log(error)
            this.msgService.error(error.error.message, "error-dialog");
          }
        }
      )
  }

  setChip(event: { data: Purpose }) {
    if (this.selectedPurpose.length > 0)
      this.chipFilter.push({ label: event.data.description, value: event.data.id });
    else
      this.chipFilter = [];
  }

  removeChip(event: { data: Purpose }) {
    const newChipFilter = this.chipFilter.filter(x => x.value !== event.data.id);
    this.chipFilter = [...newChipFilter]
  }

  resetSelection() {
    this.chipFilter = [];
    this.selectedPurpose = [];
  }

  allChip(event) {
    this.selectAll = !this.selectAll;
    this.chipFilter = [];
    if (this.selectedPurpose.length > 0) {
      this.selectedPurpose.forEach(purpose => {
        this.chipFilter.push({ label: purpose.description, value: purpose.id })
      })
    }
  }


}
