import { Injectable } from '@angular/core';
import { CustomTimePeriod } from 'app/api/model/customTimePeriod';

import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class CustomTimePeriodService {

  constructor(private client: ApiClientService) { }

  customTimePeriods(periodTypeId: string): Observable<CustomTimePeriod[]> {
    // console.log('search customTimePeriods with ' + periodTypeId);
    return this.client
      .get(`customtimeperiods/${periodTypeId}`).pipe(
        map(json => json.results as CustomTimePeriod[])
      );
  }

  /**
   * Gets the list of custom_time_period.
   *
   * @returns Observable<CustomTimePeriod[]>
   */
  getCustomTimePeriod(): Observable<CustomTimePeriod[]> {
    return this.client
      .get(`customtimeperiods/`).pipe(
        map(json => json.results as CustomTimePeriod[])
      );
  }

  getCustomTimePeriodForIndicatorMovement(acctgTransId: string, acctgTransEntrySeqId: string): Observable<CustomTimePeriod> {
    return this.client
      .get(`customtimeperiods/indicator-movement/${acctgTransId}/${acctgTransEntrySeqId}`).pipe(
        map(json => json as CustomTimePeriod)
      );
  }

  /**
   * Create a custom_time_period.
   *
   * @param CustomTimePeriod - CustomTimePeriod
   * @returns Promise<CustomTimePeriod>
   */
  async createCustomTimePeriod(CustomTimePeriod: CustomTimePeriod): Promise<CustomTimePeriod> {
    const client$ = this.client.post(`customtimeperiods/`, JSON.stringify(CustomTimePeriod));
    return await lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while creating in: ${response.error.message}`);
        return Promise.reject(response.error.message);
      });
  };

  /**
   * Update a custom_time_period.
   *
   * @param CustomTimePeriod - CustomTimePeriod
   * @returns Promise<CustomTimePeriod>
   */
  updateCustomTimePeriod(CustomTimePeriod: CustomTimePeriod): Promise<CustomTimePeriod> {
    const client$ = this.client.put('customtimeperiods/', JSON.stringify(CustomTimePeriod));
    return lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while updating in: ${response.error.message}`);
        return Promise.reject(response.error.message)

      })

  };

  /**
   * Delete the custom_time_period ids passed to it.
   *
   * @param id - string[]
   * @returns Promise
   */
  deleteCustomTimePeriod(id: string[]): Promise<any> {
    const client$ = this.client.delete(`customtimeperiods/${id}`);
    return lastValueFrom(client$).then(response => response)
      .catch((error: any) => {
        console.error(`Error while deleting in: ${error.error.message}`);
        return Promise.reject(error.error);
      });

  }

}
