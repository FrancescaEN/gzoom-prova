import { Injectable } from "@angular/core";
import { ApiClientService } from "app/commons/service/client.service";
import { Observable, map } from "rxjs";
import { TsHolidaysDates } from "../model/tsHolidaysDate";

@Injectable()
export class TsHolidaysDatesService {
  constructor(private client: ApiClientService) {}

  tsHolidaysDates(timesheetId): Observable<TsHolidaysDates[]> {
    return this.client
      .get(`tsHolidaysDates/${timesheetId}`)
      .pipe(map((json) => json.results as TsHolidaysDates[]));
  }
}
