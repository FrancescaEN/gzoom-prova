import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, ResolveFn } from '@angular/router';

import { lastValueFrom } from 'rxjs';

import { NodeService } from './node.service';
import { Node } from '../view/node/node';

/**
 * Retrieves the menus to be shown or locks the user out if something wrong happens.
 */
export const nodeResolver: ResolveFn<void | Node> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<void | Node> => {
  const nodeService = inject(NodeService);
  //const lockoutService = inject(LockoutService);
  const nodeService$ = nodeService.node("Company");
  return lastValueFrom(nodeService$).then(node => { return node; })
    .catch(err => {
      console.error('Cannot retrieve node', err);
      //this.lockoutService.lockout(); cancella tutta la sessione e ti rimanda all'homepage
    });
}
