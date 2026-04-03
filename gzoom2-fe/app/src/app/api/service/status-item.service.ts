import { Injectable } from '@angular/core';

import { Observable, lastValueFrom } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';

import { HttpErrorResponse } from '@angular/common/http';
import { StatusItemExType } from '../model/statusItemExType';
import { StatusItem } from '../model/statusItem';


@Injectable()
export class StatusItemService {

  constructor(private client: ApiClientService) { }

  statusItems(parentTypeId: string): Observable<StatusItem[]> {
    console.log('search statusItem with ' + parentTypeId);
    return this.client
      .get(`status-item/${parentTypeId}`).pipe(
        map(json => json.results as StatusItem[])
      );
  }

  getStatusItemByCode(statusCode: string): Observable<StatusItem[]> {
    return this.client
      .get(`status-item/code/${statusCode}`).pipe(
        map(json => json.results as StatusItem[])
      );
  }

  getStatusItemList(statusTypeId: string): Observable<StatusItem[]> {
    return this.client
      .get(`status-item/status-type-id/${statusTypeId}`).pipe(
        map(json => json.results as StatusItem[])
      );
  }

  getStatusItemStateTo(): Observable<StatusItemExType[]> {
    return this.client
      .get(`status-item/state-to`).pipe(
        map(json => json.results as StatusItemExType[])
      );
  }

  getStatusItemStateFrom(statusTypeId: string): Observable<StatusItemExType[]> {
    return this.client
      .get(`status-item/state-from/${statusTypeId}`).pipe(
        map(json => json.results as StatusItemExType[])
      );
  }

  async createStatusItem(statusItem: StatusItem): Promise<boolean> {
    const client$ = this.client.post(`status-item/`, JSON.stringify(statusItem));
    return await lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while creating in: ${response.error.message}`);
        return Promise.reject(response.error.message);
      });
  };


  async updateStatusItem(statusItem: StatusItem): Promise<boolean> {
    const client$ = this.client.put('status-item/', JSON.stringify(statusItem));
    try {
      const response = await lastValueFrom(client$);
      return response;
    } catch (error) {
      console.error(`Error while updating in: ${error.error.message}`);
      return await Promise.reject(error.error.message);
    }

  };

  async deleteStatusItem(id: string): Promise<boolean> {
    const client$ = this.client.delete(`status-item/${id}`);
    try {
      const response = await lastValueFrom(client$);
      return response;
    } catch (error) {
      console.error(`Error while deleting in: ${error.error.message}`);
      return await Promise.reject(error.error);
    }
  }
}
