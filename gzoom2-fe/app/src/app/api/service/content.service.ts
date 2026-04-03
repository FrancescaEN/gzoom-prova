import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from '../../commons/service/client.service';
import { ContentExDataResource } from '../model/contentExDataResource';
import { Content } from '../model/content';


/**
 * @author Leonardo Minaudo
 */
@Injectable()
export class ContentService {

    constructor(private client: ApiClientService) { }


    /**
     * Gets a content list given its contentTypeId.
     * 
     * @returns Observable<Content[]>
     */
    getContentListByContentTypeId(contentTypeId: string): Observable<ContentExDataResource[]> {
        return this.client
            .get(`content/${contentTypeId}`).pipe(
                map(json => json.results as ContentExDataResource[])
            );
    }

    findByContentTypeId(contentTypeId: string): Observable<Content[]> {
        return this.client
            .get(`content/content-type-id/${contentTypeId}`).pipe(
                map(json => json.results as Content[])
            );
    }
}