import { Component, EventEmitter, Input, OnInit, Output, Renderer2, ViewChild } from '@angular/core';
import { ToolbarButton, ToolbarButtonType } from './text-editor-configuration';

@Component({
  selector: 'app-text-editor',
  templateUrl: './text-editor.component.html',
  styleUrls: ['./text-editor.component.css']
})
export class TextEditorComponent implements OnInit {
  @Input() text: string;
  @Input() readonly: boolean = false;
  @Input() showOnlyText: boolean = false;
  @Input() width: string;
  @Input() height: string;
  @Input() toolbarButton: ToolbarButton = new ToolbarButton;
  @Input() settings: string;
  @Input() isHtml: boolean = true;
  @Input() required: boolean = false;

  @Output() shareText = new EventEmitter();
  @Output() shareHtml = new EventEmitter();


  @ViewChild('textEditor') private textEditor;

  constructor(private renderer: Renderer2) { }

  ngOnInit(): void {
    this.setSettings();

  }

  setSettings() {
    let btn = ToolbarButtonType;

    if (this.settings == "standard") this.setFalse(btn.IMAGE, btn.VIDEO, btn.FORMULA);

  }

  setFalse(...buttons) {
    buttons.forEach(btn => {
      this.toolbarButton[btn] = false;
    });
  }

  changeText(data) {

    (this.isHtml == false) ? this.shareText.emit(data.textValue) : this.shareText.emit(data.htmlValue);
  }

}
