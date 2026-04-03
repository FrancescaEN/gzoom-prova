import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { I18NService } from 'app/i18n/i18n.service';
import { ButtonService } from '../../../../../commons/service/button.service';
import { WorkEffortPurposeTypeService } from 'app/api/service/work-effort-purpose-type.service';
import { BehaviorSubject, Observable, Subject, map, switchMap, takeUntil, tap } from 'rxjs';
import { WorkEffortPurposeAccount } from 'app/api/model/workEffortPurposeAccount';
import { WorkEffortPurposeAccountService } from 'app/api/service/work-effort-purpose-account.service';
import { MsgService } from 'app/commons/service/message.service';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { NewPurposeComponent } from './new-purpose/new-purpose.component';
import { ScrollableTabService } from 'app/commons/service/scrollable-tab.service';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { Table } from 'primeng/table';


export interface Purpose {
  id: string,
  description: string,
  usedIn: string[],
  comment: string
}


@Component({
  selector: 'gzoom-indicator-tab-purpose',
  templateUrl: './indicator-tab-purpose.component.html',
  styleUrls: ['./indicator-tab-purpose.component.scss']
})
export class IndicatorTabPurposeComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  _reload = new BehaviorSubject<void>(null);
  reload$ = this._reload.asObservable();
  secondaryLang: boolean = this.i18nService.getIsSecondaryLang();
  scrollable: Observable<boolean> = this.scrollableTabService.isScrollableWidth(960);
  disableSave: boolean = true;
  loading: boolean = true;
  purpose: Purpose[] = [];
  glAccountId: string;
  updatedPurpose: string[] = [];

  selectedPurpose: Purpose[] = [];

  ref: DynamicDialogRef = new DynamicDialogRef();
  @ViewChild("dt") private dataTable: Table;

  constructor(private router: Router,
    private route: ActivatedRoute,
    private i18nService: I18NService,
    private buttonService: ButtonService,
    private workEffortPurposeTypeService: WorkEffortPurposeTypeService,
    private workEffortPurposeAccountService: WorkEffortPurposeAccountService,
    private msgService: MsgService,
    public dialogService: DialogService,
    private scrollableTabService: ScrollableTabService,
    private confirmDialogService: ConfirmDialogService
  ) {
    this.buttonService.showSaveButton(false);
    this.glAccountId = this.route.parent.snapshot.paramMap.get('glAccountId');
  }



  ngOnInit(): void {
    this.reload$.pipe(
      tap(() => {
        this.loading = true;
        this.disableSave = true;
      }),
      switchMap(() => this.workEffortPurposeTypeService.getPurposeTabType(this.glAccountId, true))
    )
      .pipe(
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
              comment: values[0].workEffortPurposeAccount?.comments
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

  reset() {
    this.dataTable.editingCell = null;
    this._reload.next();
  }


  doNothing(e: any) {
    e.stopPropagation();
    return;
  }

  changeValue(purposeId: string) {

    if (this.updatedPurpose.indexOf(purposeId) === -1) this.updatedPurpose.push(purposeId);
    this.disableSave = false;
  }

  save() {
    const updated = this.purpose.filter(x => this.updatedPurpose.indexOf(x.id) != -1);
    let workEffortPurposeAccount: WorkEffortPurposeAccount[] = [];
    updated.forEach(wpt => {
      const newWept: WorkEffortPurposeAccount = { workEffortPurposeTypeId: wpt.id, glAccountId: this.glAccountId, comments: wpt.comment }
      workEffortPurposeAccount.push(newWept);
    })

    this.workEffortPurposeAccountService.updateComments(workEffortPurposeAccount)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        {
          next: () => {
            this.msgService.successUpdate();
            this._reload.next();
          },
          error: (error) => {
            this.msgService.error(error.error.message)
          }
        }
      )
  }


  newPurpose() {
    this.ref = this.dialogService.open(NewPurposeComponent, {
      header: this.i18nService.translate('Select purpose'),

      styleClass: 'min-width p-dialog-width',
      contentStyle: { overflow: 'auto' },
      data: {
        glAccountId: this.glAccountId,

      }
    });

    this.ref.onClose.subscribe((str: string) => {
      if (str === 'reload') {
        this._reload.next();
      }
    });

  }

  delete() {
    this.confirmDialogService.deleteMultiElement().then(x => {
      if (x) {
        this.workEffortPurposeAccountService.delete(this.selectedPurpose.map(x => x.id), this.glAccountId)
          .subscribe(
            {
              next: (value) => {
                this.msgService.successDelete();
                this.selectedPurpose = []
                if (value) this._reload.next();
              },
              error: (error) => {
                this.msgService.error(error.error.message);
              }
            }
          )
      }

    })

  }

  canDeactivate(): boolean {
    return !this.disableSave;
  }


  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
    this.ref?.close();
  }
}
