import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { RoleType } from '../model/role-type';

@Injectable()
export class RoleTypeService {

  constructor(private client: ApiClientService) { }

  roleTypes(): Observable<RoleType[]> {
    return this.client
      .get(`role-types/`).pipe(
        map(json => json.results as RoleType[])
      );
  }

  getRoleTypeByParentTypeId(parentTypeId: string) {
    return this.client
      .get(`role-types/parent-type/${parentTypeId}`).pipe(
        map(json => json.results as RoleType[])
      );
  }

  getRoleTypeByOUAndLikeGOAL(): Observable<RoleType[]> {
    return this.client
      .get('role-types/ou')
      .pipe(
        map(json => json.results as RoleType[])
      )
  }

  updateRoleTypes(roleType: RoleType): Promise<RoleType> {
    console.log('update roleType');
    const client$ = this.client.put('role-types/', roleType);
    return lastValueFrom(client$).then(response => response)
      .catch(response => {
        console.error(`Error while creating in: ${response}`);
        return Promise.reject(response.json() || response);
      });
  }

  deleteRoleTypes(roleType: String[]): Promise<RoleType> {
    const client$ = this.client.delete(`role-types/${roleType}`);
    return lastValueFrom(client$).then(response => response)
      .catch((error: any) => {
        console.error(`Error while deleting in: ${error.error.message}`);
        return Promise.reject(error.error);
      });
  }

  createRoleTypes(roleType: RoleType): Promise<RoleType> {
    console.log('create roleType');

    const client$ = this.client.post('role-types/', roleType);
    return lastValueFrom(client$).then(response => response)
      .catch(response => {
        console.error(`Error while creating in: ${response}`);
        return Promise.reject(response);
      });
  }

}
