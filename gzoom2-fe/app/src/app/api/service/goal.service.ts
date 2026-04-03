import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from '../../commons/service/client.service';
import { Goal } from '../../commons/model/goal-file/Goal';
import { ArrayUtils } from '../../commons/utils/array-utils';
import { GenericService } from './commons/generic.service';
import { Note } from '../../commons/model/goal-file/note/Note';
import { Attachment } from '../../commons/model/goal-file/content/Attachment';
import { GoalNote as GoalNoteDTO } from './goal-file/dto/note/goal-note';

@Injectable({
  providedIn: 'root'
})
export class GoalService extends GenericService {
  constructor(protected client: ApiClientService) {
    super(client);
  }

  getGoals(id: string): Observable<Goal[]> {
    const uri = `v1/goalfile/${id}/tree/details`;
    return this.get('getGoals', uri).pipe(
      map(res => {
        if (Array.isArray(res)) {
          return res.map(Goal.fromJson);
        } else {
          throw new Error(res);
        }
      })
    );
  }

  getGoalRoots(): Observable<Goal[]> {
    const uri = `v1/goalfile/roots`;
    return this.get('getGoalRoots', uri).pipe(
      map(res => {
        if (ArrayUtils.notEmpty(res)) {
          return res.map(Goal.fromJson);
        } else {
          throw new Error(res);
        }
      })
    );
  }

  getNotes(id: string): Observable<Note[]> {
    const uri = `v1/goal/${id}/notes`;
    return this.get('getNotes', uri).pipe(
      map((res: GoalNoteDTO[]) => {
        if (ArrayUtils.notEmpty(res)) {
          return res.map(Note.fromResponse);
        } else {
          return [];
        }
      })
    );
  }

  updateNote(id: string, note: Note): Observable<any> {
    const uri = `v1/goal/note/${id}`;
    return this.put('updateNote', uri, note).pipe(
      map(res => {
        if (res === null) {
          return res;
        } else {
          throw new Error(res)
        }
      })
    );
  }

  getAttachments(id: string): Observable<Attachment[]> {
    const uri = `v1/goal/${id}/contents`;
    return this.get('getAttachments', uri).pipe(
      map(res => {
        if (Array.isArray(res)) {
          return res.map(Attachment.fromJson);
        } else {
          return [];
        }
      })
    );
  }

  uploadAttachment(form: FormData): Observable<any> {
    const uri = `v1/goal/content`;
    return this.postForm('uploadAttachment', uri, form).pipe(
      map(res => {
        if (res === null) {
          return res;
        } else {
          throw new Error(res)
        }
      })
    );
  }

  downloadAttachment(id: string): Observable<any> {
    const uri = `v1/goal/content/${id}/download`;
    return this.getFile('downloadAttachment', uri, null, null, 'blob').pipe(
      map(res => res)
    );
  }

  updateAttachment(attachmentId: string, data: Attachment): Observable<any> {
    const uri = `v1/goal/content/${attachmentId}`;
    return this.put('updateAttachment', uri, data).pipe(
      map(res => {
        if (res === null) {
          return res;
        } else {
          throw new Error(res)
        }
      })
    );
  }

  deleteAttachment(attachmentId: string): Observable<any> {
    const uri = `v1/goal/content/${attachmentId}`;
    return this.delete('deleteAttachment', uri).pipe(
      map(res => {
        if (res === null) {
          return res;
        } else {
          throw new Error(res)
        }
      })
    );
  }
}
