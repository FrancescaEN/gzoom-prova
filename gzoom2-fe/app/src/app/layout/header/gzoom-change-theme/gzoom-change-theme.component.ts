import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
@Component({
    selector: 'gzoom-change-theme',
    templateUrl: './gzoom-change-theme.component.html',
    styleUrl: './gzoom-change-theme.component.scss',
})
export class GzoomChangeThemeComponent {

  themes = [
    { key: 'THEME_GREEN', label: 'Green', color: '#b6c5b5', labelLegacy: 'GPLUS_GREEN_ACC' },
    { key: 'THEME_BLUE', label: 'Blue', color: '#86abde', labelLegacy: 'GPLUS_BLUE_ACC' },
    { key: 'THEME_VIOLET', label: 'Violet', color: '#d0b4de', labelLegacy: 'GPLUS_VIOLET_ACC' },
    { key: 'THEME_BLUE_LIGHT', label: 'Blue Light', color: '#3880ff', labelLegacy: 'GPLUS_BLUE_LIGHT' },
    { key: 'THEME_GREEN_LIGHT', label: 'Green Light', color: '#2dd36f', labelLegacy: 'GPLUS_GREEN_LIGHT' },
    { key: 'THEME_VIOLET_LIGHT', label: 'Violet Light', color: '#c93d82', labelLegacy: 'GPLUS_VIOLET_LIGHT' },
    { key: 'THEME_HIGH_CONTRAST', label: 'High Contrast', color: '#000', labelLegacy: 'GPLUS_HIGH_CONTRAST' },
  ];
 

  @Output() saveChangeTheme = new EventEmitter<string>();

  @Input() displayChangeTheme : boolean = false;
  @Output() displayChangeThemeChange = new EventEmitter<boolean>();

  themeChange(theme){
    this.saveChangeTheme.emit(theme);
  }

  onHide(){    
    this.displayChangeTheme = false;
    this.displayChangeThemeChange.emit(this.displayChangeTheme)
  }

}
