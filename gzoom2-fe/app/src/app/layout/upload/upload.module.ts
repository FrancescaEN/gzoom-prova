import { NgModule } from '@angular/core';
import { FileUploadModule } from 'primeng/fileupload';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
    imports: [
        FileUploadModule,
        HttpClientModule
    ],
    declarations: [],
    providers: [],
    exports: [
        FileUploadModule,
        HttpClientModule
    ]
})
export class UploadModule { }
