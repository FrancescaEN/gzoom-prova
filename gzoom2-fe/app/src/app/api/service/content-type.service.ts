import { Injectable } from '@angular/core';
import { ContentType } from 'app/api/model/contentType';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiClientService } from '../../commons/service/client.service';


/**
 * @author Leonardo Minaudo
 */
@Injectable()
export class ContentTypeService {

    constructor(private client: ApiClientService) { }


    /**
     * Gets a contentType list given its parentTypeId.
     * 
     * @returns Observable<ContentType[]>
     */
    getContentTypeWithParentTypeId(parentTypeId: string): Observable<ContentType[]> {
        return this.client
            .get(`content-type/${parentTypeId}`).pipe(
                map(json => json.results as ContentType[])
            );
    }

}
