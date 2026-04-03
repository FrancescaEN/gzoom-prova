import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent {
  @Input() icon: string = "pi pi-search";
  @Input() label: string;
  @Input() header: string;
  @Input() draggable: boolean = true;

  @Output() clickOnShow = new EventEmitter<any>();

  @Input() visible: boolean = false;

  showDialog() {
    this.visible = true;
    this.clickOnShow.emit();
  }
}
