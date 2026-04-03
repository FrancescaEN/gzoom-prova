import { Injectable } from '@angular/core';

import { lastValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';


import { UomRatingScale } from '../model/uomRatingScale';
import { HttpErrorResponse } from '@angular/common/http';
import { forEach } from 'lodash';
import { UomType } from 'app/view/ctx-ac/uom-type/uom_type';
import { Uom } from 'app/api/model/uom';

@Injectable({
  providedIn: 'root'
})
export class UomService {

  constructor(private client: ApiClientService) { }

  getAllUom(): Observable<Uom[]> {
    return this.client
      .get('uom').pipe(
        map(json => json.results as Uom[])
      );
  }

  uomTypes(): Observable<UomType[]> {
    return this.client
      .get('uom/type').pipe(
        map(json => json.results as UomType[])
      );
  }

  createUomType(uomType: UomType): Promise<UomType> {
    console.log('create UomType');
    const client$ = this.client.post('uom/type', JSON.stringify(uomType));
    return lastValueFrom(client$).then(response => response)
      .catch(error => {
        console.error(`Error while creating in: ${error.error.message}`);
        return Promise.reject(error.error);
      });
  }

  updateUomType(uomTypeId: string, uomType: UomType): Promise<UomType> {
    console.log('update UomType');

    const client$ = this.client.put(`uom/type/${uomTypeId}`, JSON.stringify(uomType));
    return lastValueFrom(client$).then(response => response)
      .catch((error: any) => {
        console.error(`Error while updating in: ${error.error.message}`);
        return Promise.reject(error.error);
      });
  }

  deleteUomType(uomTypeId: string): Promise<UomType> {
    console.log('delete UomType with ' + uomTypeId);
    const client$ = this.client.delete(`uom/type/${uomTypeId}`);
    return lastValueFrom(client$).then(response => response)
      .catch((error: any) => {
        console.error(`Error while deleting in: ${error.error.message}`);
        return Promise.reject(error.error);
      });
  }

  uoms(): Observable<Uom[]> {
    return this.client
      .get('uom/value').pipe(
        map(json => json.results as Uom[])
      );
  }

  getUomById(uomId: string): Observable<Uom> {
    console.log('search uom with ' + uomId);
    return this.client
      .get(`uom/value/${uomId}`).pipe(
        map(json => json as Uom)
      );
  }

  isRatingScale(uomId: string): Observable<boolean> {
    return this.client
      .get(`uom/isRatingScale/${uomId}`).pipe(
        map(json => json as boolean)
      );
  }

  isRatingScaleByGlAccount(glAccontId: string): Observable<boolean> {
    return this.client
      .get(`uom/isRatingScale/gl-account/${glAccontId}`).pipe(
        map(json => json as boolean)
      );
  }


  /**
   * "1.2-2"
   */
  formatNumber(uom: Uom): String {
    var format = "1.";
    if (uom.decimalScale != null) {
      format += uom.decimalScale + "-" + uom.decimalScale;
    } else format += "0-0";
    console.log("- fomar=" + format);
    return format;
  }

  patternRegExp(uom: Uom): String {
    var format = "^[0-9]+(.[0-9]{0,@})?$";
    if (uom.decimalScale == null) {
      format = format.replace('@', '0');
    } else format = format.replace('@', String(uom.decimalScale));
    console.log("- pattern=" + format);
    return format;
  }

  createUom(uom: Uom): Promise<string> {
    console.log('create Uom');
    const client$ = this.client.post(`uom/`, JSON.stringify(uom))
    return lastValueFrom(client$).then(response => response)
      .catch((response: HttpErrorResponse) => {
        console.error(`Error while creating in: ${response}`);
        return Promise.reject(response);
      });
  }

  updateUom(uomId: string, uom: Uom): Promise<Uom> {
    console.log('update Uom');
    const client$ = this.client.put(`uom/${uomId}`, uom);
    return lastValueFrom(client$).then(response => response)
      .catch((response: any) => {
        console.error(`Error while updating in: ${response}`);
        return Promise.reject(response);
      });
  }

  deleteUom(uoms: String): Promise<Uom> {
    const client$ = this.client.delete(`uom/${uoms}`);
    return lastValueFrom(client$).then(response => response)
      .catch((error: HttpErrorResponse) => {
        console.error(`Error while exec query: ${error.error.message}`);
        return Promise.reject(error.error.message);
      });
  }

}
