import { Injectable } from '@angular/core';
import { Observable, distinctUntilChanged, fromEvent, map, merge, of, switchMap } from 'rxjs';


@Injectable({
    providedIn: 'root'
})
export class ScrollableTabService {

    isScrollableWidth(value: number): Observable<boolean> {
        return merge(
            fromEvent(window, 'resize'),
            of(window.innerWidth)
        ).pipe(
            switchMap(() => of(window.innerWidth)),
            map(width => width > value),
            distinctUntilChanged(),
        )
    }

    isScrollableHeight(value: number): Observable<boolean> {
        return merge(
            fromEvent(window, 'resize'),
            of(window.innerHeight)
        ).pipe(
            switchMap(() => of(window.innerHeight)),
            map(width => width > value),
            distinctUntilChanged(),
        )
    }

}