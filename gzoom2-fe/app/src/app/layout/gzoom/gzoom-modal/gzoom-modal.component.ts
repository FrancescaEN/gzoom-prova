import {Component, ElementRef, OnDestroy, OnInit, ViewEncapsulation, booleanAttribute, input} from '@angular/core';
import {GzoomGenericComponentComponent} from "../gzoom-generic/gzoom-generic.component";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {ScrollPanelModule} from "primeng/scrollpanel";

@Component({
  selector: 'gzoom-modal',
  standalone: true,
  styleUrl: './gzoom-modal.component.scss',
  templateUrl: './gzoom-modal.component.html',
  imports: [
    ProgressSpinnerModule,
    ScrollPanelModule
  ],
  encapsulation: ViewEncapsulation.None
})
export class GzoomModalComponent extends GzoomGenericComponentComponent implements OnInit, OnDestroy {
  loading: boolean;
  loadingModal = input(false, {transform: booleanAttribute});

  constructor(protected elementRef: ElementRef,
              protected modalDialogRef: DynamicDialogRef) {
    super(elementRef);
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.loading = true;
    // console.log(`${this.selector} init`);
    this.loadData();
  }

  ngOnDestroy(): void {
    // console.log(`${this.selector} destroyed`);
  }

  protected loadData() {
    this.loading = false;
  }

  closeModal() {
    // console.log(`closing ${this.selector}...`)
    this.modalDialogRef.close();
  }
}
