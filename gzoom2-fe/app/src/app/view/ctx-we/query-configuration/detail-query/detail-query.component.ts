import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { QueryConfig } from 'app/api/model/queryConfig';
import { QueryConfigService } from 'app/api/service/query-config.service';
import { Subject, map, mergeMap, mergeWith, switchMap, tap } from 'rxjs';
import { I18NService } from 'app/i18n/i18n.service';
import { DataForButton } from 'app/layout/toolbar-data-table/toolbar-data-table-configuration';
import { MsgService } from 'app/commons/service/message.service';
import { ToolbarService } from 'app/commons/service/toolbar.service';

@Component({
  selector: 'app-detail-query',
  templateUrl: './detail-query.component.html',
  styleUrls: ['./detail-query.component.css']
})
export class DetailQueryComponent implements OnInit {
  title: string;
  backLink: string;
  buttonToConditions: DataForButton = {
    icon: 'pi pi-angle-right',
    titleLabel: 'Conditions',
    booleanShow: true
  }

  queryFormGroup = new FormGroup({
    columnFormatting: new FormControl(null, Validators.nullValidator),
    query: new FormControl(null, Validators.required),
  });

  queryConfig: QueryConfig = new QueryConfig;
  queryType: string;
  _reload: Subject<void>;
  constructor(
    private route: ActivatedRoute,
    private readonly queryConfigService: QueryConfigService,
    private readonly i18nService: I18NService,
    private readonly router: Router,
    private readonly msgService: MsgService,
    private toolbarService: ToolbarService
  ) {
    this._reload = new Subject<void>();
    this.toolbarService.setDefaultFormComponentButton();
  }

  ngOnInit(): void {
    this.queryConfig.queryId = this.route.snapshot.parent.params.id;
    this.queryType = this.route.snapshot.parent.params.queryType;
    this.backLink = '../../'    

    const reload = this._reload.pipe(mergeMap(() => this.queryConfigService.getQueryConfig(this.queryConfig.queryId)));
    const w$ = this.route.data.pipe(
      map((data: { obss: QueryConfig }) => data.obss),
      mergeWith(reload),
      tap(qc => this.title = qc.queryCode + ". " + qc.queryName),
      tap(queryConfig => {
        this.queryConfig = queryConfig;
        this.queryFormGroup.patchValue({ columnFormatting: this.queryConfig?.queryColumnsFormatParam, query: this.queryConfig?.queryInfo });
        this.queryFormGroup.markAsPristine();
        this.toolbarService.setDisabledSave(true);
      }),
      switchMap(() => this.queryFormGroup.valueChanges)
    ).subscribe(values => {
      if (!this.queryFormGroup.pristine) {
        this.toolbarService.setDisabledSave(false);
      }

    });
  }

  async save() {

    if (this.queryFormGroup.status == "INVALID") {
      this.msgService.errorFieldsRequired();

    }
    else {
      this.queryConfig.queryInfo = this.queryFormGroup.value.query;
      this.queryConfig.queryColumnsFormatParam = this.queryFormGroup.value.columnFormatting;

      await this.queryConfigService.updateQueryConfig(this.queryConfig)
        .then(() => {
          this.msgService.successUpdate();
          this._reload.next()
        })
        .catch((error) => {
          this.msgService.error(error);
        });
    }
  }

  toConditions() {
    if (this.queryType) {
      this.router.navigate([`/c/CTX_WE/interoperability/query-configuration-query-type/${this.queryType}/conditions/${this.queryConfig.queryId}`]);
    }
    else {
      this.router.navigate([`/c/CTX_WE/interoperability/query-configuration/conditions/${this.queryConfig.queryId}`]);
    }
  }

  canDeactivate(): boolean {
    return (!this.queryFormGroup.pristine);
  }

}
