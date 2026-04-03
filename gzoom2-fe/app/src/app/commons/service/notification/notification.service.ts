import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';
import { I18NService } from '../../../i18n/i18n.service';
import { OperationResult } from './operation-result';

@Injectable()
export class NotificationService {
  constructor(private readonly messageService: MessageService,
              private readonly i18nService: I18NService // when merging, keep i18nLocalService
  ) {}

  private shownErrorMessages = new Set<string>();
  private shownSuccessMessages = new Set<string>();
  private shownWarningMessages = new Set<string>();

  /**
   * Process operation result:
   * 1. Show error notifications if any.
   * 2. Show warning notifications if any.
   * 3. If no error occurred:
   *  3.1. Show success notification.
   *  3.2. Execute successHandler.
   * @param result OperationResult object
   * @param toasterId primeNG toaster ID if any. If not specified, default toaster is used.
   * @return true if no error occurred, otherwise false.
   */
  handleResult(result: OperationResult,
               toasterId: string = null): boolean {
    result.errors.forEach(error => this.handleError(error, toasterId));
    result.warnings.forEach(warning => this.handleWarning(warning, toasterId));

    if (result.errors.length === 0) {
      this.handleSuccess(result.successMessage, toasterId);
      result.successHandler();
      return true;
    }

    return false;
  }

  /**
   * Show success notification.
   * @param message Success message
   * @param toasterId primeNG toaster ID if any. If not specified, default toaster is used.
   */
  handleSuccess(message: string = '',
                toasterId: string = null): void {

    const key = `${message}-${toasterId}`;
    if (this.shownSuccessMessages.has(key)) return;
    this.shownSuccessMessages.add(key);

    // rimuovo dopo 300ms eventuali duplicati
    setTimeout(() => this.shownSuccessMessages.delete(key), 300);

    console.log('Notification Service handleSuccess');
    this.messageService.add({
      severity: 'success',
      summary: this.i18nService.translate('Success'),
      detail: message,
      key: toasterId
    });
  }

  /**
   * Show error notification.
   * @param message Warning message
   * @param toasterId primeNG toaster ID if any. If not specified, default toaster is used.
   */
  handleWarning(message: string = '',
                toasterId: string = null): void {

    const key = `${message}-${toasterId}`;
    if (this.shownWarningMessages.has(key)) return;
    this.shownWarningMessages.add(key);

    // rimuovo dopo 300ms eventuali duplicati
    setTimeout(() => this.shownWarningMessages.delete(key), 300);

    this.messageService.add({
      severity: 'warn',
      summary: this.i18nService.translate('Warning'),
      detail: message,
      key: toasterId
    });
  }

  /**
   * Show error notification.
   * @param message Error message
   * @param toasterId primeNG toaster ID if any. If not specified, default toaster is used.
   */
  handleError(message: string = '',
              toasterId: string = null): void {

    const key = `${message}-${toasterId}`;
    if (this.shownErrorMessages.has(key)) return;
    this.shownErrorMessages.add(key);

    // rimuovo dopo 300ms eventuali duplicati
    setTimeout(() => this.shownErrorMessages.delete(key), 300);

    this.messageService.add({
      severity: 'error',
      summary: this.i18nService.translate('Error'),
      detail: message,
      key: toasterId
    });
  }
}
