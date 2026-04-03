import { Pipe, PipeTransform } from '@angular/core';
import { Observable, catchError, isObservable, map, of, startWith } from 'rxjs';

@Pipe({
  name: 'loading',
  standalone: true
})
export class LoadingPipe implements PipeTransform {

  transform<T>(val: Observable<T>): Observable<{ loading: boolean, value?: T, error?: unknown }> {
    return isObservable(val)
      ? val.pipe(
        map((value: any) => ({ loading: false, value })),
        startWith({ loading: true }),
        catchError((error) => of({ loading: false, error }))
      )
      : val
  }

}
