import { Injectable, inject } from '@angular/core';
import { Router, UrlSerializer } from '@angular/router';
import { Area } from 'app/api/model/dashboards/area';
import { AreaLevel, Level } from 'app/api/model/dashboards/level';
import { PeriodArea, PeriodFactor, PeriodMeasureType, PeriodProcess } from 'app/api/model/dashboards/period';
import { Process } from 'app/api/model/dashboards/process';
import { AuthService } from 'app/commons/service/auth.service';
import { ApiClientService } from 'app/commons/service/client.service';
import { Observable, map } from 'rxjs';

/**
 * This service contains API calls for the Anti-Corruption Dashboard.
 */
@Injectable({
  providedIn: 'root'
})
export class RiskService {
  /* Services */
  private client = inject(ApiClientService);
  private router = inject(Router);
  private serializer = inject(UrlSerializer);
  private authService = inject(AuthService);
  private ROOT = "/dashboard/v1.0.0";

  getAreaPro(orgUnitId: string): Observable<Area[]> {
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/area/pro`], { queryParams: { user: this.authService.userProfile()?.username, orgUnit: orgUnitId } });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as Area[])
      );
  }

  getAreaMis(organizationUnitId: string): Observable<Area[]> {
    console.log(`getAreaMis organizationUnitId: ${organizationUnitId}`)
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/area/mis`],
      { queryParams: { user: this.authService.userProfile()?.username, orgUnit: organizationUnitId } });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as Area[])
      );
  }

  getRiskAreaByYear(year: number,
                    orgUnitId?: string): Observable<Area[]> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/actual/${year}`], {
      queryParams: queryParams
    });
    const url = this.serializer.serialize(tree);

    return this.client.get(url).pipe(
      map(json => json as Area[])
    );
  }

  getRiskPeriodAreaByYearTrend(startYear: number, endYear: number, orgUnitId?: string): Observable<PeriodArea[]> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username,
      startYear: startYear,
      endYear: endYear
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/trend`], 
      { queryParams: queryParams }
    );
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodArea[])
      );
  }

  getRiskProcessByYear(year: number, orgUnitId?: string, areaId?: string): Observable<Process[]> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username,
      year: year
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    if (areaId !== null && areaId !== undefined && areaId !== '') {
      queryParams.areaId = areaId;
    }
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/process`], {
      queryParams: queryParams
    });
    const url = this.serializer.serialize(tree);

    return this.client.get(url).pipe(
      map(json => json as Process[])
    );
  }

  getRiskProcessTrendByArea(startYear: number, endYear: number, areaId: string, orgUnitId: string): Observable<PeriodProcess[]> {
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/process/trend`], { queryParams: { startYear, endYear, areaId, user: this.authService.userProfile()?.username, orgUnit: orgUnitId } });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodProcess[])
      );
  }

  getRiskProcessByAreaAndYear(year: number, areaId: string, orgUnitId: string): Observable<PeriodProcess[]> {
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/process/trend`], { queryParams: { endYear: year, areaId, user: this.authService.userProfile()?.username, orgUnit: orgUnitId } });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodProcess[])
      );
  }

  getRiskProScoreByYear(year: number, orgUnitId?: string, areaId?: string): Observable<AreaLevel[]> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username,
      year: year
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    if (areaId !== null && areaId !== undefined && areaId !== '') {
      queryParams.areaId = areaId;
    }
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/pro-score`], {
      queryParams: queryParams
    });
    const url = this.serializer.serialize(tree);

    return this.client.get(url).pipe(
      map(json => json as AreaLevel[])
    );
  }

  getRiskPreventionTrend(startYear: number, endYear: number, orgUnitId?: string): Observable<PeriodArea[]> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username,
      startYear: startYear,
      endYear: endYear
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/trend/prevention`], 
      { 
        queryParams: queryParams
      });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodArea[])
      );
  }

  getRiskPreventionTrendProcess(startYear: number, endYear: number, areaId: string, orgUnitId?: string): Observable<PeriodProcess[]> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username,
      startYear: startYear,
      endYear: endYear,
      areaId: areaId
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/process/trend/prevention`], 
      { 
        queryParams: queryParams 
      }
    );
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodProcess[])
      );
  }

  getRiskPreventionProcess(year: number, areaId: string, orgUnitId: string): Observable<PeriodProcess[]> {
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/process/trend/prevention`], { queryParams: { endYear: year, areaId, orgUnit: orgUnitId } });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodProcess[])
      );
  }

  getRiskPreventionStateByYear(year: number, orgUnitId?: string): Observable<PeriodArea[]> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username,
      year: year
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/state/prevention`], 
      { 
        queryParams: queryParams 
      });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodArea[])
      );
  }

  getRiskFactorsByYear(year: number, orgUnitId?: string): Observable<PeriodFactor> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username,
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/factors/${year}`], 
      { 
        queryParams: queryParams 
      });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodFactor)
      );
  }

  getRiskFactorsByYearAndLevelId(year: number, levelId: number, orgUnitId?: string): Observable<PeriodFactor> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username,
      year, levelId
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/factors/level`], 
      { 
        queryParams: queryParams 
      });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodFactor)
      );
  }

  getRiskFactorsTrendByLevelId(startYear: number, endYear: number, levelId: number, orgUnitId?: string): Observable<PeriodFactor[]> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username,
      startYear, endYear, levelId
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/factors/trend`], 
      { 
        queryParams: queryParams 
      });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodFactor[])
      );
  }

  getRiskLevels(orgUnitId?: string): Observable<Level[]> {
    const queryParams: any = {}
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/levels`], 
      { 
        queryParams: queryParams 
      });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as Level[])
      );
  }

  getRiskMeasureTypesByYear(year: number, orgUnitId: string): Observable<PeriodMeasureType> {
    const queryParams: any = {
      user: this.authService.userProfile()?.username,
    }
    if (orgUnitId !== null && orgUnitId !== undefined && orgUnitId !== '') {
      queryParams.orgUnit = orgUnitId;
    }
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/measure-type/${year}`], 
      { 
        queryParams: queryParams 
      });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodMeasureType)
      );
  }

  getRiskMeasureTypesByYearAndLevelId(year: number, levelId: number, orgUnitId: string): Observable<PeriodMeasureType> {
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/measure-type/level`], { queryParams: { year, levelId, user: this.authService.userProfile()?.username, orgUnit: orgUnitId } });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodMeasureType)
      );
  }

  getRiskMeasureTypesTrendByLevelId(startYear: number, endYear: number, levelId: number, orgUnitId: string): Observable<PeriodMeasureType[]> {
    const tree = this.router.createUrlTree([`${this.ROOT}/risks/measure-type/trend`], { queryParams: { startYear, endYear, levelId, user: this.authService.userProfile()?.username, orgUnit: orgUnitId } });
    const url = this.serializer.serialize(tree);

    return this.client.get(url)
      .pipe(
        map(json => json as PeriodMeasureType[])
      );
  }
}
