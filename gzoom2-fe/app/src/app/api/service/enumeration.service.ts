import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { Enumeration } from '../model/enumeration';
import { Filter } from 'app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration';

@Injectable()
export class EnumerationService {
  constructor(private client: ApiClientService) { }

  enumerations(enumTypeId: string): Observable<Enumeration[]> {
    return this.client
      .get(`enumeration/${enumTypeId}`).pipe(
        map(json => json.results as Enumeration[])
      );
  }

  /**
     * Gets the list of work_effort_assoc pagination.
     * 
     * @returns Observable<WorkEffortView[]>
     */
  getEnumerationsFilter(filter: Filter): Observable<Enumeration[]> {

    return this.client
      .post(`enumeration/filter`, filter).pipe(
        map(json => json.results as Enumeration[])
      );
  }
}
