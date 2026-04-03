import { Injectable } from '@angular/core';
import { ApiClientService } from '../../commons/service/client.service';
import { Observable, lastValueFrom, map } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { StandardImportFieldConfig } from '../model/standardImportFiledConfig';
import { StandardImportFieldConfigEx } from '../model/standardImportFieldConfigEx';


@Injectable()
export class StandardImportFieldConfigService {

    constructor(private client: ApiClientService) { }


    /**
   * Gets the list of StandardImportFieldConfig.
   * 
   * @returns Observable<StandardImportFieldConfig[]>
   */
    getStandardImportFieldConfig(): Observable<StandardImportFieldConfig[]> {
        return this.client
            .get(`standard-import-field-config/standardImportFieldConfig`).pipe(
                map(json => json.results as StandardImportFieldConfig[])
            );
    }

    /**
    * Gets the list of StandardImportFieldConfig.
    * 
    * @returns Observable<StandardImportFieldConfigEx[]>
    */
    getStandardImportFieldConfigEx(dataSourceId: string): Observable<StandardImportFieldConfigEx[]> {
        return this.client
            .get(`standard-import-field-config/standardImportFieldConfigEx/${dataSourceId}`).pipe(
                map(json => json.results as StandardImportFieldConfigEx[])
            );
    }

    /**
     * Create a standardImportFieldConfig.
     * 
     * @param standardImportFieldConfig - StandardImportFieldConfig
     * @returns Promise<StandardImportFieldConfig>
     */
    async createStandardImportFieldConfig(standardImportFieldConfig: StandardImportFieldConfig): Promise<StandardImportFieldConfig> {
        const client$ = this.client.post(`standard-import-field-config/`, JSON.stringify(standardImportFieldConfig));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a standardImportFieldConfig.
     * 
     * @param standardImportFieldConfig - StandardImportFieldConfig
     * @returns Promise<StandardImportFieldConfig>
     */
    updateStandardImportFieldConfig(standardImportFieldConfig: StandardImportFieldConfig): Promise<StandardImportFieldConfig> {
        const client$ = this.client.put(`standard-import-field-config/`, JSON.stringify(standardImportFieldConfig));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)
            })
    };

    /**
     * Delete the standardImportFieldConfig ids passed to it.
     * 
     * @param dataSourceId - string
     * @param standardInterface - string
     * @param internalFieldName - string
     * @param interfaceSeq - string
     * @returns Promise
     */
    deleteStandardImportFieldConfig(dataSourceId: string, standardInterface: string, internalFieldName: string, interfaceSeq: number): Promise<any> {
        const client$ = this.client.delete(`standard-import-field-config/${dataSourceId}/${standardInterface}/${internalFieldName}/${interfaceSeq}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });
    }
}
