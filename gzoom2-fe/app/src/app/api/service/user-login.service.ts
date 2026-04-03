import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiClientService } from '../../commons/service/client.service';
import { UserLogin } from '../model/userLogin';


/**
 * @author Leonardo Minaudo
 */
@Injectable()
export class UserLoginService {

    constructor(private client: ApiClientService) { }

    findAllOrderByPrimaryKey(): Observable<UserLogin[]> {
        return this.client
            .get(`user-login/list`).pipe(
                map(json => json.results as UserLogin[])
            );
    }
}