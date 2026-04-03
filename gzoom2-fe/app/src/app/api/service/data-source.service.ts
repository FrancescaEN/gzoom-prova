import { Injectable } from '@angular/core';
import { ApiClientService } from '../../commons/service/client.service';
import { DataSource } from '../model/dataSource';
import { Observable, lastValueFrom, map } from 'rxjs';
import { DataSourceEx } from '../model/dataSourceEx';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class DataSourceService {

    constructor(private client: ApiClientService) { }


    /**
   * Gets the list of data_source.
   * 
   * @returns Observable<DataSource[]>
   */
    getDataSource(): Observable<DataSource[]> {
        return this.client
            .get(`data-source/dataSource`).pipe(
                map(json => json.results as DataSource[])
            );
    }

    /**
   * Gets the data_source by data_source_id.
   * 
   * @returns Observable<DataSource>
   */
    getDataSourceById(dataSourceId): Observable<DataSource> {
        return this.client
            .get(`data-source/${dataSourceId}`).pipe(
                map(json => json as DataSource)
            );
    }

    /**
    * Gets the list of data_source_ex.
    * 
    * @returns Observable<DataSourceEx[]>
    */
    getDataSourceEx(): Observable<DataSourceEx[]> {
        return this.client
            .get(`data-source/dataSourceEx`).pipe(
                map(json => json.results as DataSourceEx[])
            );
    }

    /**
     * Create a data_source.
     * 
     * @param dataSource - DataSource
     * @returns Promise<DataSource>
     */
    async createDataSource(dataSource: DataSource): Promise<DataSource> {
        const client$ = this.client.post(`data-source/`, JSON.stringify(dataSource));
        return await lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while creating in: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    };

    /**
     * Update a data_source.
     * 
     * @param dataSource - DataSource
     * @returns Promise<DataSource>
     */
    updateDataSource(dataSource: DataSource): Promise<DataSource> {
        const client$ = this.client.put(`data-source/`, JSON.stringify(dataSource));
        return lastValueFrom(client$).then(response => response)
            .catch((response: HttpErrorResponse) => {
                console.error(`Error while updating in: ${response.error.message}`);
                return Promise.reject(response.error.message)
            })
    };

    /**
     * Delete the data_source ids passed to it.
     * 
     * @param id - string
     * @returns Promise
     */
    deleteDataSource(id: string): Promise<any> {
        const client$ = this.client.delete(`data-source/${id}`);
        return lastValueFrom(client$).then(response => response)
            .catch((error: any) => {
                console.error(`Error while deleting in: ${error.error.message}`);
                return Promise.reject(error.error);
            });
    }
}
