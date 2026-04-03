import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { GoalStatus } from '../../../commons/model/goal-file/status/goal-status';
import { StringUtils } from '../../../commons/utils/string-utils';
import { Results } from './dto/results/results';
import { GoalStatus as GoalStatusDTO } from './dto/status/goal-status';
import { GenericService } from './generic.service';

export interface GoalFileStatusFilter {
  goalFileId?: string;
  goalFileStatusId?: string;
}

@Injectable({
  providedIn: 'root'
})
export class GoalFileStatusService extends GenericService {
  protected makeQueryParams(filter: GoalFileStatusFilter): HttpParams {
    let params = new HttpParams();

    if (StringUtils.notEmpty(filter.goalFileId)) {
      params = params.set('goalFileId', filter.goalFileId);
    }

    if (StringUtils.notEmpty(filter.goalFileStatusId)) {
      params = params.set('goalFileStatusId', filter.goalFileStatusId);
    }

    return params;
  }

  /**
   * https://artexe.atlassian.net/wiki/spaces/GzoomTecnici/pages/3142287362/Lista+stati+possibili+GET
   * @param goalFileId
   * @param goalFileStatusId
   */
  getGoalFileAvailableStatuses(goalFileId: string,
                               goalFileStatusId: string): Observable<GoalStatus[]> {
    const uri = `v1/goalfile/status/available`;
    return this.get('getGoalFileAvailableStatuses', uri, { goalFileId, goalFileStatusId }, []).pipe(
      map(res => {
        if (Array.isArray(res)) {
          return res.map(GoalStatus.fromGoalStatusAvailable);
        }
        throw new Error('Invalid getGoalFileAvailableStatuses result: ' + res);
      })
    );
  }

  updateGoalFileStatus(goalFileId: string, status: GoalStatus, noteId: string = null): Observable<Results<boolean>> {
    const uri = `/v1/goalfile/${goalFileId}/status${StringUtils.isBlank(noteId) ? '' : `?noteId=${noteId}`}`;
    // todo pass noteId as params query param
    return this.put('updateGoalFileStatus', uri, GoalStatusDTO.fromGoalStatus(status)).pipe(
      map(res => res as Results<boolean>) // todo converter
    );
  }
}
