import { Injectable } from '@angular/core';
import { ConfirmationService, PrimeIcons } from 'primeng/api';
import { I18NService } from 'app/i18n/i18n.service';


/**
 * API service that operates on confirm dialog.
 */
@Injectable({
    providedIn: 'root'
})
export class ConfirmDialogService {

    constructor(private readonly i18nService: I18NService, private readonly confirmationService: ConfirmationService) { }

    confirm(message: string, header: string, icon: string): boolean {

        this.confirmationService.confirm({
            message: message,
            header: header,
            icon: icon,

            accept: () => {
                return true;
            },
            reject: () => {
                return false;
            }
        });
        return false;
    }

    delete({
        message = this.i18nService.translate('Do you want to delete this record?'),
        header = this.i18nService.translate('Delete Confirmation'),
        icon = PrimeIcons.TRASH,
        acceptButtonStyleClass = "p-button-rounded dialog-button-accept",
        rejectButtonStyleClass = "p-button-rounded dialog-button-reject",
        acceptLabel = this.i18nService.translate("Yes")
    } = {}): Promise<boolean> {
        return new Promise(resolve => {
            this.confirmationService.confirm({
                message,
                header,
                icon,
                acceptButtonStyleClass,
                rejectButtonStyleClass,
                acceptLabel,
                accept: () => {
                    resolve(true);
                },
                reject: () => {
                    resolve(false);
                }
            })
        })
    }

    deleteMultiElement(key?: string, {
        message = this.i18nService.translate('Delete selected items?'),
        header = this.i18nService.translate('Delete Confirmation'),
        icon = PrimeIcons.TRASH,
        acceptButtonStyleClass = "p-button-rounded dialog-button-accept",
        rejectButtonStyleClass = "p-button-rounded dialog-button-reject",
        acceptLabel = this.i18nService.translate("Yes")
    } = {}): Promise<boolean> {
        return new Promise(resolve => {
            this.confirmationService.confirm({
                key,
                message,
                header,
                icon,
                acceptButtonStyleClass,
                rejectButtonStyleClass,
                acceptLabel,
                accept: () => {
                    resolve(true);
                },
                reject: () => {
                    resolve(false);
                }
            })
        })
    }

    unsaved({
        message = this.i18nService.translate('There are unsaved items, are you sure want to leave the page?'),
        header = this.i18nService.translate('Attention'),
        icon = PrimeIcons.EXCLAMATION_TRIANGLE,
        acceptButtonStyleClass = "p-button-rounded dialog-button-accept",
        rejectButtonStyleClass = "p-button-rounded dialog-button-reject",
        acceptLabel = this.i18nService.translate("Yes")
    } = {}): Promise<boolean> {
        return new Promise(resolve => {
            this.confirmationService.confirm({
                message,
                header,
                icon,
                acceptButtonStyleClass,
                rejectButtonStyleClass,
                acceptLabel,
                accept: () => {
                    resolve(true);
                },
                reject: () => {
                    resolve(false);
                }
            })
        })
    }


    confirmClearCalcCustomMethod({
        message = this.i18nService.translate('With the deletion of the Calculation Formula all associated Calculation Parameters will be deleted, continue?'),
        header = this.i18nService.translate('Saving confirmation'),
        icon = PrimeIcons.INFO_CIRCLE,
        acceptButtonStyleClass = "p-button-rounded dialog-button-accept",
        rejectButtonStyleClass = "p-button-rounded dialog-button-reject",
        acceptLabel = this.i18nService.translate("Yes")
    } = {}): Promise<boolean> {
        return new Promise(resolve => {
            this.confirmationService.confirm({
                message,
                header,
                icon,
                acceptButtonStyleClass,
                rejectButtonStyleClass,
                acceptLabel,
                accept: () => {
                    resolve(true);
                },
                reject: () => {
                    resolve(false);
                }
            })
        })
    }


}
