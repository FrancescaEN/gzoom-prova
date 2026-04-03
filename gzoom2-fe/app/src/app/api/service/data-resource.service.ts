import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { ApiClientService } from '../../commons/service/client.service';
import { WorkEffortContentEx } from '../model/workEffortContentEx';


@Injectable()
export class DataResourceService {

    constructor(private client: ApiClientService) { }

    uploadFile(file: File, dataResourceId): Promise<WorkEffortContentEx> {
        const formData = new FormData();
        formData.append("file", file);

        const client$ = this.client.postFormData(`data-resource/uploadFile/${dataResourceId}`, formData);
        return lastValueFrom(client$).then(response => response)
            .catch(response => {
                console.error(`Error while upload file: ${response.error.message}`);
                return Promise.reject(response.error.message);
            });
    }
}
