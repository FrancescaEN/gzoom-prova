import { Injectable } from "@angular/core";
import { ApiClientService } from "app/commons/service/client.service";
import { Observable, lastValueFrom, map } from "rxjs";
import { EmplPositionType } from "../model/emplPositionType";

@Injectable()
export class EmplPositionTypeService {
  constructor(private client: ApiClientService) { }

  emplPositionTypes(): Observable<EmplPositionType[]> {
    return this.client
      .get("emplPositionType/")
      .pipe(map((json) => json.results as EmplPositionType[]));
  }

  updateEmplPositionTypes(
    emplPositionType: EmplPositionType
  ): Promise<EmplPositionType> {
    const client$ = this.client.put("emplPositionType/", emplPositionType);
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((response) => {
        console.error(`Error while creating in: ${response}`);
        return Promise.reject(response.json() || response);
      });
  }

  deleteEmplPositionTypes(emplPositionType: String[]): Promise<any> {
    const client$ = this.client.delete(`emplPositionType/${emplPositionType}`);
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((error: any) => {
        console.error(`Error while deleting in: ${error.error.message}`);
        return Promise.reject(error.error);
      });
  }

  createEmplPositionTypes(
    emplPositionType: EmplPositionType
  ): Promise<EmplPositionType> {
    const client$ = this.client.post("emplPositionType/", emplPositionType);
    return lastValueFrom(client$)
      .then((response) => response)
      .catch((response) => {
        console.error(`Error while creating in: ${response}`);
        return Promise.reject(response);
      });
  }
}
