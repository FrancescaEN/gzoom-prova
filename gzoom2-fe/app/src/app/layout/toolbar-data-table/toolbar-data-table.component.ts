import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from "@angular/core";
import { Location } from "@angular/common";
import { DataForButton } from "./toolbar-data-table-configuration";
import { ActivatedRoute, Router } from "@angular/router";
import { ToolbarService } from "app/commons/service/toolbar.service";
import { Observable } from "rxjs";

@Component({
  selector: "app-toolbar-data-table",
  templateUrl: "./toolbar-data-table.component.html",
  styleUrls: ["./toolbar-data-table.component.scss"],
})
export class ToolbarDataTableComponent implements OnInit, OnChanges {
  @Input() isEditing: boolean;

  @Input() dataTable: any;
  @Input() gridArray: any[] = [];
  @Input() buttonBack: boolean;
  @Input() buttonNew: boolean;
  @Input() disabledNew: boolean = false;
  @Input() buttonDelete: boolean;
  @Input() buttonSave: boolean;
  @Input() buttonReset: boolean;
  @Input() disabledSave: boolean = false;
  @Input() bldx: DataForButton;
  @Input() bldx2: DataForButton;
  @Input() backLink: string;
  @Input() backLinkRelToThisRoute: string;


  @Output() buttonNewEvent = new EventEmitter<void>();
  @Output() saveEvent = new EventEmitter<void>();
  @Output() deleteEvent = new EventEmitter<void>();
  @Output() resetEvent = new EventEmitter<void>();
  @Output() bldxEvent = new EventEmitter<void>();
  @Output() bldx2Event = new EventEmitter<void>();

  disableNew$;
  disableDelete$;
  disableSave$;
  disableReset$;



  exportArray: any[] = [];
  constructor(
    private route: ActivatedRoute,
    private _location: Location,
    private readonly router: Router,
    private toolbarService: ToolbarService
  ) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes) {
      if (changes["gridArray"]) {
        this.exportArray = [];
        this.gridArray.forEach((element) => {
          this.exportArray.push(element);
        });
      }
    }
  }

  ngOnInit(): void {
    this.gridArray.forEach((element) => {
      this.exportArray.push(element);
    });
    this.buttonNew = true


    this.disableNew$ = this.toolbarService.getIsDisabledNew$();
    this.disableDelete$ = this.toolbarService.getIsDisabledDelete$();
    this.disableSave$ = this.toolbarService.getIsDisabledSave$();
    this.disableReset$ = this.toolbarService.getIsDisabledReset$();


  }

  openNewRow() {
    this.buttonNewEvent.emit();
  }

  deleteRow() {
    this.deleteEvent.emit();
  }

  back() {
    if (this.backLink)
      this.router.navigate([this.backLink]);
    else if (this.backLinkRelToThisRoute)
      this.router.navigate([this.backLinkRelToThisRoute], { relativeTo: this.route })
    else
      this._location.back();
  }

  resetAllAction() {
    this.resetEvent.emit();
  }

  save() {
    this.saveEvent.emit();
  }

  blDxEvent() {
    this.bldxEvent.emit();
  }

  blDx2Event() {
    this.bldx2Event.emit();
  }
}
