import { NgModule } from '@angular/core';
import { EditorModule } from 'primeng/editor';

@NgModule({
    imports: [
        EditorModule,
    ],
    declarations: [],
    providers: [],
    exports: [
        EditorModule
    ]
})
export class TextEditorModule { }
