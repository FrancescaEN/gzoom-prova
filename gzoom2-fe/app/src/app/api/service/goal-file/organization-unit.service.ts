import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { GenericService } from './generic.service';
import { OrganizationUnit } from '../../model/goal-file/organizationUnit';
import { StringUtils } from '../../../commons/utils/string-utils';
import { ShowUoCode } from '../../../commons/enum/goalfile/ShowUoCode';

export interface OrganizationUnitFilter {
  context: string;
  organizationUnitTypeId?: string;
  showUoCode?: ShowUoCode;
  secondaryLang: boolean;
  fromYear?: number;
  year?: number;
}

@Injectable({
  providedIn: 'root'
})
export class OrganizationUnitService extends GenericService {
  protected makeQueryParams(filter: OrganizationUnitFilter): HttpParams {
    let params = new HttpParams();
    params = params.set('context', filter.context);

    if (StringUtils.notEmpty(filter.organizationUnitTypeId)) {
      params = params.set('organizationUnitTypeId', filter.organizationUnitTypeId);
    }
    if (StringUtils.notEmpty(filter.showUoCode)) {
      params = params.set('showUoCode', filter.showUoCode);
    }
    if (filter.fromYear !== null && filter.fromYear !== undefined) {
      params = params.set('fromYear', filter.fromYear);
    }
    if (filter.year !== null && filter.year !== undefined) {
      params = params.set('endYear', filter.year);
    }
    if (filter.secondaryLang === true) {
      params = params.set('secondaryLang', filter.secondaryLang);
    }

    return params;
  }

  getOrganizationUnits(filter: OrganizationUnitFilter): Observable<OrganizationUnit[]> {
    // https://artexe.atlassian.net/wiki/spaces/GzoomTecnici/pages/3121315940/Lista+delle+unit+responsabili+GET
    const uri = `v1/organization-units?`;
    return this.get('getOrganizationUnits', uri, filter, []).pipe(
      map(json => json as OrganizationUnit[])
    );
  }

  getSupervisorOrganizationUnits(filter: OrganizationUnitFilter): Observable<OrganizationUnit[]> {
    // https://artexe.atlassian.net/wiki/spaces/GzoomTecnici/pages/3122036737/Lista+delle+unit+responsabili+superiori+GET
    const uri = `v1/supervisor-organization-units?`;
    return this.get('getSupervisorOrganizationUnits', uri, filter, []).pipe(
      map(json => json as OrganizationUnit[])
    );
  }
}
