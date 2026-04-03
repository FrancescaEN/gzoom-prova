import { Component, computed, signal } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { GlAccount } from "app/api/model/glAccount";
import { I18NService } from "app/i18n/i18n.service";
import { DynamicDialogConfig, DynamicDialogRef } from "primeng/dynamicdialog";


@Component({
    selector: 'gzoom-new-indicator-success',
    templateUrl: 'new-indicator-success.component.html',
})
export class NewIndicatorComponentSuccess {
    glAccount = signal<GlAccount>(this.dialogConfig.data.glAccount);
    description: string;
    desc0: string;
    desc1: string;
    gpMenu = signal<string>(this.dialogConfig.data.gpMenu);
    context = signal<string>(this.dialogConfig.data.context);
    labelGlAccountCreated = computed(() => this.gpMenu() && this.context() ? `glAccountCreated.${this.context()}.${this.gpMenu()}` : null);
    labelNewGlAccount = computed(() => this.gpMenu() && this.context() ? `newGlAccount.${this.context()}.${this.gpMenu()}` : null);
    labelGlAccountCreatedDesc = computed(() => this.gpMenu() && this.context() ? this.i18nService.translate(`glAccountCreatedDesc.${this.context()}.${this.gpMenu()}`) : null);

    constructor(
        private route: ActivatedRoute,
        public dialogConfig: DynamicDialogConfig,
        public ref: DynamicDialogRef,
        private i18nService: I18NService) {
    }

    dialogClose(command: string) {
        this.ref.close({ command, id: this.glAccount().glAccountId });
    }
}
