import { Component, OnInit } from '@angular/core';
import { InterfacciamentoDatiService } from '../../../api/service/interfacciamento-dati.service';
import { I18NService } from '../../../i18n/i18n.service';
import { Subject } from 'rxjs';
import { MsgService } from 'app/commons/service/message.service';
@Component({
  selector: 'app-interfacciamento-dati',
  templateUrl: './interfacciamento-dati.component.html',
  styleUrls: ['./interfacciamento-dati.component.css']
})
export class InterfacciamentoDatiComponent implements OnInit {

  _reload: Subject<void>;
  error = '';
  uploadedFiles: any;
  showError: boolean = false;
  showErrorButton: boolean = false;

  constructor(
    private readonly InterfacciamentoDatiService: InterfacciamentoDatiService,
    private readonly i18nService: I18NService,
    private msgService: MsgService, 
  ) { }

  ngOnInit(): void {

  }

  onClear() {
    this.showErrorButton = false;
    this.showError = false;
    this.error = null;
  }

  onUploadHandler(file: any) {

    this.uploadedFiles = file;

    if (file) {
      this.showErrorButton = false;
      this.showError = false;
      this.error = null;
      this.InterfacciamentoDatiService
        .shareFile(file.files[0])
        .then(() => {          
          this.msgService.success(this.i18nService.translate('Record created'));
        })
        .catch((error) => {
          this.showErrorButton = true;
          this.error = error;
        });
    }
  }

  show() {
    this.showError = true;
  }
}
