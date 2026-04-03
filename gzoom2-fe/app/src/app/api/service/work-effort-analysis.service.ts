import { Injectable } from '@angular/core';
import { WorkEffortAnalysis } from '../model/workEffortAnalysis';
import { WorkEffortAnalysisTarget } from 'app/api/model/workEffortAnalysisTarget';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from 'app/commons/service/client.service';
import { DetailKPI } from '../model/detailKPI';
import { Score } from '../model/score';
import { HttpErrorResponse } from '@angular/common/http';
import { WorkEffortAnalysisEx } from '../model/workEffortAnalysisEx';



@Injectable()
export class WorkEffortAnalysisService {

  constructor(private client: ApiClientService) { }

  getWorkEffortAnalysis(analysisId: string): Observable<WorkEffortAnalysis> {

    return this.client
      .get(`work-effort-analysis-id/${analysisId}`).pipe(
        map(json => json as WorkEffortAnalysis)
      );
  }

  getWorkEffortAnalysisList(): Observable<WorkEffortAnalysis[]> {
    return this.client
      .get(`work-effort-analysis`).pipe(
        map(json => json as WorkEffortAnalysis[])
      );
  }

  getWorkEffortAnalysisExList(context: string): Observable<WorkEffortAnalysisEx[]> {
    return this.client
      .get(`work-effort-analysis-ex/context/${context}`).pipe(
        map(json => json.results as WorkEffortAnalysisEx[])
      );
  }

  getWorkEffortAnalysisExById(analysisId: string): Observable<WorkEffortAnalysisEx> {
    return this.client
      .get(`work-effort-analysis-ex/${analysisId}`).pipe(
        map(json => json as WorkEffortAnalysisEx)
      );
  }

  async createWorkEffortAnalysis(WorkEffortAnalysis: WorkEffortAnalysis): Promise<WorkEffortAnalysis> {
    const client$ = this.client.post(`work-effort-analysis`, JSON.stringify(WorkEffortAnalysis));
    return await lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while creating in: ${response.error.message}`);
        return Promise.reject(response.error.message);
      });
  };

  async updateWorkEffortAnalysis(workEffortAnalysis: WorkEffortAnalysis): Promise<boolean> {
    const client$ = this.client.put(`work-effort-analysis`, JSON.stringify(workEffortAnalysis));
    try {
      const response = await lastValueFrom(client$);
      return response;
    } catch (error) {
      console.error(`Error while updating in: ${error.error.message}`);
      return await Promise.reject(error.error.message);
    }

  };

  async deleteWorkEffortAnalysis(workEffortAnalysisId: string): Promise<boolean> {
    const client$ = this.client.delete(`work-effort-analysis/${workEffortAnalysisId}`);
    try {
      const response = await lastValueFrom(client$);
      return response;
    } catch (error) {
      console.error(`Error while deleting in: ${error.error.message}`);
      return await Promise.reject(error.error);
    }
  }


  /**
   * Gets the list of analyses in a context.
   * 
   * @param context - Context.
   * @returns Observable array of WorkEffortAnalysis.
   */
  getWorkEffortAnalysisWithContext(context: string): Observable<WorkEffortAnalysis[]> {
    console.log('search workEffortAnalysis list');
    return this.client
      .get(`work-effort-analysis/${context}`).pipe(
        map(json => json.results as WorkEffortAnalysis[])
      );
  }

  /**
   * Gets the header.
   * 
   * @param analysisId - Analysis Id.
   * @param workEffortId - Work Effort Id.
   * @returns Observable array of WorkEffortAnalysis.
   */
  getWorkEffortAnalysisHeader(analysisId: string, workEffortId: string): Observable<WorkEffortAnalysis[]> {
    console.log('search workEffortAnalysis Header');
    return this.client
      .get(`work-effort-analysis-targets-header/${analysisId}/${workEffortId}`).pipe(
        map(json => json.results as WorkEffortAnalysis[])
      );

  }

  /**
   * Gets the list of work effort with out a workEffortId.
   * 
   * @param context - Context.
   * @param analysisId - Analysis Id.
   * @returns Observable array.
   */
  getWorkEffortAnalysisTargetSummary(context: string, analysisId: string): Observable<any[]> {
    console.log('search workEffortAnalysis Summary');
    return this.client
      .get(`work-effort-analysis-targets/${context}/${analysisId}`).pipe(
        map(json => json.results as any[]),
      );
  }

  /**
   * Gets the header.
   * 
   * @param analysisId - Analysis Id.
   * @param workEffortId - Work Effort Id.
   * @returns Observable array.
   */
  getWorkEffortAnalysisTargetHeaderOne(analysisId: string, workEffortId: string, rangeDefault: string): Observable<any[]> {

    return this.client
      .get(`work-effort-analysis-targets/header/${analysisId}/${workEffortId}/${rangeDefault}`).pipe(
        map(json => json.results as any[]),

      );
  }

  /**
   * Gets the list of work effort.
   * 
   * @param context - Context.
   * @param analysisId - Analysis Id.
   * @param dateControl - dateControl by comments.
   * @param rangeDefault - rangeDefault by comments.
   * @param showOrgUnit - showOrgUnit by comments.
   * @returns Observable array WorkEffortAnalysisTarget.
   */
  getWorkEffortAnalysisTargetList(context: string, analysisId: string, dateControl: string, rangeDefault: string, showOrgUnit: string): Observable<WorkEffortAnalysisTarget[]> {

    return this.client
      .get(`work-effort-analysis-targets/list/${context}/${analysisId}/${dateControl}/${rangeDefault}/${showOrgUnit}`).pipe(
        map(json => json.results as WorkEffortAnalysisTarget[]),
      );
  }

  /**
   * Gets the list of work effort with a workEffortId.
   * 
   * @param analysisId - Analysis Id.
   * @param workEffortId - Work Effort Id.
   * @param dateControl - dateControl by comments.
   * @param rangeDefault - rangeDefault by comments.
   * @param showOrgUnit - showOrgUnit by comments.
   * @returns Observable array WorkEffortAnalysisTarget.
   */
  getWorkEffortAnalysisTargetListWithWE(analysisId: string, workEffortId: string, dateControl: string, rangeDefault: string, showOrgUnit: string): Observable<WorkEffortAnalysisTarget[]> {
    return this.client
      .get(`work-effort-analysis-targets/list-with-work-effort/${analysisId}/${workEffortId}/${dateControl}/${rangeDefault}/${showOrgUnit}`).pipe(
        map(json => json.results as WorkEffortAnalysisTarget[])
      )
  }

  /**
   * Gets the header.
   * 
   * @param context - Context.
   * @param analysisId - Analysis Id.
   * @returns Observable array WorkEffortAnalysisTarget.
   */
  getWorkEffortAnalysisTargetHeaderMore(context: string, analysisId: string): Observable<WorkEffortAnalysisTarget[]> {
    return this.client
      .get(`work-effort-analysis-targets/header-more/${context}/${analysisId}`).pipe(
        map(json => json.results as WorkEffortAnalysisTarget[])
      )
  }

  /**
   * Gets the indicators when detailKPI from comments equals SCORE.
   * 
   * @param analysisId - Analysis id.
   * @param workEffortId - Work effort id.
   * @param dateControl - dateControl by comments.
   * @returns Observable array DetailKPI
   */
  getDetailKPIScore(analysisId: string, workEffortId: string, dateControl: string, rangeDefault: string, purposeKPIList: string[]): Observable<DetailKPI[]> {
    return this.client
      .get(`work-effort-analysis-targets/detailKPIScore/${analysisId}/${workEffortId}/${dateControl}/${rangeDefault}/${purposeKPIList}`).pipe(
        map(json => json.results as DetailKPI[])
      )
  }

  /**
   * Gets the indicators when detailKPI from comments equals PERIOD.
   * 
   * @param analysisId - Analysis id.
   * @param workEffortId - Work effort id.
   * @param dateControl - dateControl by comments.
   * @returns Observable array DetailKPI
   */
  getDetailKPIPeriod(analysisId: string, workEffortId: string, dateControl: string, rangeDefault: string, purposeKPIList: string[]): Observable<DetailKPI[]> {
    return this.client
      .get(`work-effort-analysis-targets/detailKPIPeriod/${analysisId}/${workEffortId}/${dateControl}/${rangeDefault}/${purposeKPIList}`).pipe(
        map(json => json.results as DetailKPI[])
      )
  }


  getPdoScore(workEffortId: string): Observable<Score[]> {
    return this.client
      .get(`work-effort-analysis-targets/pdoScore/${workEffortId}`).pipe(
        map(json => json.results as Score[])
      )
  }

  getKPIscore(workEffortId: string, analysisRefDate: Date): Observable<Score[]> {
    return this.client
      .get(`work-effort-analysis-targets/KPIscore/${workEffortId}/${analysisRefDate}`).pipe(
        map(json => json.results as Score[])
      )
  }

  getDetailPdoScore(workEffortId: string, glFiscelTypeId: string): Observable<Score[]> {
    return this.client
      .get(`work-effort-analysis-targets/detailPdoScore/${workEffortId}/${glFiscelTypeId}`).pipe(
        map(json => json.results as Score[])
      )
  }

  getPdoAccount(glAccountId: string): Observable<Score[]> {
    return this.client
      .get(`work-effort-analysis-targets/pdoAccount/${glAccountId}`).pipe(
        map(json => json.results as Score[])
      )
  }

  getPdoAccountWEMI(glAccountId: string, workEffortMeasureId: string): Observable<Score[]> {
    return this.client
      .get(`work-effort-analysis-targets/pdoAccount/${glAccountId}/${workEffortMeasureId}`).pipe(
        map(json => json.results as Score[])
      )
  }

  getPdoAccountOrgUnit(glAccountId: string, orgUnitRoleTypeId: string, orgUnitId: string): Observable<Score[]> {
    return this.client
      .get(`work-effort-analysis-targets/pdoAccount/${glAccountId}/${orgUnitRoleTypeId}/${orgUnitId}`).pipe(
        map(json => json.results as Score[])
      )
  }



  getPdoScorekpi(workEffortMeasureId: string): Observable<Score[]> {
    return this.client
      .get(`work-effort-analysis-targets/pdoScorekpi/${workEffortMeasureId}`).pipe(
        map(json => json.results as Score[])
      )
  }

  getWEAWithRefDateAndWorkEffortTypeId(workEffortAnalysis: WorkEffortAnalysis): Promise<WorkEffortAnalysis> {
    const client$ = this.client.post('work-effort-analysis/refDate&&workEffortTypeId', JSON.stringify(workEffortAnalysis));
    return lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while updating in: ${response.error.message}`);
        return Promise.reject(response.error.message)

      })
  }


}
