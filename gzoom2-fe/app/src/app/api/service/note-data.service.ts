import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from '../../commons/service/client.service';
import { NoteData } from '../model/noteData';

@Injectable()
export class NoteDataService {

  constructor(private client: ApiClientService) { }

  getNoteDataByPrefValue(prefValue): Observable<NoteData> {
    return this.client
      .get(`note-data/pref-value/${prefValue}`).pipe(
        map(json => json as NoteData)
      );
  }

}
