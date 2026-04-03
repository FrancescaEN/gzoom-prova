import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-grid-image',
  templateUrl: './grid-image.component.html',
  styleUrls: ['./grid-image.component.css']
})
export class GridImageComponent {
  @Input() images: any[];

  @Output() selectedImage = new EventEmitter<any>();

  ngOnChange(changes: SimpleChanges) {
    /*if (changes.images) {
      console.log("change images")
      this.images = null;
    }*/
  }


  imageClick(image) {
    this.selectedImage.emit(image);
  }

}
