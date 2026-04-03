import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-dialog-grid-image',
  templateUrl: './dialog-grid-image.component.html',
  styleUrls: ['./dialog-grid-image.component.css']
})
export class DialogGridImageComponent {
  @Input() selectFile: any;
  @Input() header: string;
  @Output() selectedImage = new EventEmitter<any>();
  @Output() clickOnShow = new EventEmitter<any>();

  visible;

  selectFileOpen(event) {
    this.visible = true;
    this.selectFile = undefined;
    this.clickOnShow.emit(event);
  }
  selectedImageFun(image) {
    this.visible = false;
    this.selectedImage.emit(image);
  }

}
