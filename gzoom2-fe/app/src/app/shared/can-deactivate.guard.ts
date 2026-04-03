import { Injectable, inject } from '@angular/core';
import { CanDeactivateFn } from '@angular/router';
import { ConfirmDialogService } from 'app/commons/service/confirm-dialog.service';
import { Observable } from 'rxjs';

export interface CanComponentDeactivate {
  canDeactivate: () => Observable<boolean> | Promise<boolean> | boolean;
}

@Injectable()
export class CanDeactivateGuard {
  constructor(private confirmDialogService: ConfirmDialogService) { }

  canDeactivate(component: CanComponentDeactivate) {
    return component.canDeactivate ? component.canDeactivate() : true;
  }

  canDeactiveUnsaved(component: CanComponentDeactivate) {
    if (component.canDeactivate) {
      if (component.canDeactivate()) {
        return this.confirmDialogService.unsaved().then(x => { return x }).catch(() => { return true })
      }
    }
    return true
  }
}

export const canDeactivateGuard: CanDeactivateFn<CanComponentDeactivate> = (component: CanComponentDeactivate) => {
  return inject(CanDeactivateGuard).canDeactivate(component);
}


export const canDeactivateUnsavedGuard: CanDeactivateFn<CanComponentDeactivate> = (component: CanComponentDeactivate) => {
  return inject(CanDeactivateGuard).canDeactiveUnsaved(component);
}