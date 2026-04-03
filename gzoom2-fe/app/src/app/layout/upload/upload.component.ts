import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  @Input() mode: string = 'basic'; // 'advanced' | 'basic' 
  @Input() chooseLabel: string; //label button
  @Input() auto: boolean = false; //if is true send the file after the loading
  @Input() accept: string = null; //https://www.w3schools.com/tags/att_input_accept.asp
  @Input() multiple: boolean = false; //Used to select multiple files at once from file dialog.
  @Input() chooseIcon: string = "pi pi-plus"; //https://primeng.org/icons
  @Input() customUpload: boolean = true;
  @Input() maxFileSize: string = "10485760";

  @Output() myUpload = new EventEmitter();

  @ViewChild('fileUpload') fileUpload: any;

  constructor() { }

  ngOnInit(): void {
    if (this.maxFileSize > "10485760") this.maxFileSize = "10485760";
  }

  myUploader(file: any) {
    this.myUpload.emit(file);
    this.fileUpload.clear();
  }
}
