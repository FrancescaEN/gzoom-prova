import { Injectable } from "@angular/core";
import { Observable, lastValueFrom } from "rxjs";
import { map } from "rxjs/operators";
import { ApiClientService } from "app/commons/service/client.service";
import { HttpErrorResponse } from "@angular/common/http";
import { WorkEffortNoteExNoteData } from "../model/workEffortNoteExNoteData";
import { WorkEffortPartyAssignment } from "../model/workEffortPartyAssignment";
import { InfoPage } from "app/layout/tables/table-editing-cell-pagination/table-editing-cell-pagination-configuration";

/**
 *  @author Alex Tivoli
 */
@Injectable()
export class WorkEffortPartyAssignmentService {
  constructor(private client: ApiClientService) { }

  /**
   * Gets the list of  work_effort_party_assignment.
   *
   * @returns Observable<WorkEffortPartyAssignment[]>
   */
  getWorkEffortPartyAssignmentList(): Observable<WorkEffortPartyAssignment[]> {
    return this.client
      .get(`work-effort-party-assignment`)
      .pipe(map((json) => json.results as WorkEffortPartyAssignment[]));
  }

  /**
   * Gets the list of work_effort_assoc pagination.
   * 
   * @returns Observable<WorkEffortAssocEx[]>
   */
  getWorkEffortPartyAssignmentListFilter(infoPage: InfoPage): Observable<any> {

      return this.client
          .post(`work-effort-party-assignment/filter`, infoPage).pipe(
              map(json => json as WorkEffortPartyAssignment[])
          );
  }

  /**
   * Create a  work_effort_party_assignment.
   *
   * @param WorkEffortPartyAssignment - WorkEffortPartyAssignment
   * @returns Promise<WorkEffortPartyAssignment>
   */
  async createWorkEffortPartyAssignment(
    workEffortPartyAssignment: WorkEffortPartyAssignment
  ): Promise<WorkEffortPartyAssignment> {
    const client$ = this.client.post(
      `work-effort-party-assignment`,
      workEffortPartyAssignment
    );
    return await lastValueFrom(client$)
      .then((response) => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while creating in: ${response.error.message}`);
        return Promise.reject(response.error.message);
      });
  }

  /**
   * Update a  work_effort_party_assignment.
   *
   * @param WorkEffortPartyAssignment - WorkEffortPartyAssignment
   * @returns Promise<WorkEffortPartyAssignment>
   */
  updateWorkEffortPartyAssignment(
    workEffortPartyAssignment: WorkEffortPartyAssignment
  ): Promise<WorkEffortPartyAssignment> {
    const client$ = this.client.put(
      "work-effort-party-assignment",
      workEffortPartyAssignment
    );
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while updating in: ${response.error.message}`);
        return Promise.reject(response.error.message);
      });
  }

  /**
   * Delete the work_effort_party_assignment ids passed to it.
   *
   * @param workEffortPartyAssignment - WorkEffortPartyAssignment
   * @returns Promise
   */
  deleteWorkEffortPartyAssignment(workEffortPartyAssignment: WorkEffortPartyAssignment): Promise<any> {
    const client$ = this.client.post(
      "work-effort-party-assignment/delete",
      workEffortPartyAssignment
    );
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((error: any) => {
        console.error(`Error while deleting in: ${error.error.message}`);
        return Promise.reject(error.error);
      });
  }
}
