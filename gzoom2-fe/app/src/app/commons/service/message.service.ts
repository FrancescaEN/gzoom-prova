import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';
import { I18NService } from 'app/i18n/i18n.service';
import { getDate } from '../utils/dateUtils';


/**
 * API service that operates on message.
 */
@Injectable({
    providedIn: 'root'
})
export class MsgService {

    constructor(
        private readonly msgService: MessageService,
        private readonly i18nService: I18NService) { }

    warn(message: string, key?: string): void {
        this.msgService.add({ severity: 'warn', summary: this.i18nService.translate('Warning'), detail: message, key: key })
    }

    success(message: string, key?: string): void {
        this.msgService.add({ severity: 'success', summary: this.i18nService.translate('Success'), detail: message, key: key });
    }

    successCreate(key?: string) {
        this.success(this.i18nService.translate('New item added'), key)
    }

    successCreateWithId(id: string, key?: string) {
        this.success(this.i18nService.translate('New item added') + ' [' + id + ']', key);
    }

    successUpdate(key?: string) {
        this.success(this.i18nService.translate('Update Confirmation'), key);
    }

    successUpdateWithId(id: string, key?: string) {
        this.success(this.i18nService.translate('Update Confirmation') + ' [' + id + ']', key);
    }

    successDelete(key?: string) {
        this.success(this.i18nService.translate('Delete confirmation'), key);
    }

    successDeleteWithId(id: string, key?: string) {
        this.success(this.i18nService.translate('Delete confirmation') + ' [' + id + ']', key);
    }

    error(message: string, key?: string): void {
        this.msgService.clear(key ?? "error");
        this.msgService.add({ severity: 'error', summary: this.i18nService.translate('Error'), detail: message, key: key ?? "error" });
    }

    errorFieldsRequired(key?: string) {
        this.error(this.i18nService.translate('All mandatory fields must be filled in'), key);
    }

    errorDate(fromDate: Date, thruDate: Date, key?: string) {
        this.error(this.i18nService.translate('The start date {0} is greater than that of the end {1}', getDate(fromDate), getDate(thruDate)), key);
    }

    errorExistingRecord(key?: string) {
        this.error(this.i18nService.translate('Existing record'), key);
    }

    errorWithId(message: string, id: string, key?: string) {
        this.error(message + " [" + id + "]", key);
    }
}
