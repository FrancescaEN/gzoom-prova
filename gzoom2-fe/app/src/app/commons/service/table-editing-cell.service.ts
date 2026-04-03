import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiClientService } from './client.service';
import { RootMenu } from '../model/dto';
import { map } from 'rxjs/operators';


/**
 * API service that operates on table editing cell.
 */
@Injectable()
export class TableEditingCellService {

    constructor(private readonly client: ApiClientService) { }

    deleteRow(): void {
    }

    Row(): void {
    }

    /**
     * Check that all required fields have been filled in.
     * 
     * @param array 
     * @param fieldName mandatory fieldname
     * @returns true if fields are filled
     */
    validGridArray(array: any[], ...fieldName: string[]): boolean {
        let ctrl: boolean = true
        array.forEach(element => {
            fieldName.forEach(f => {
                if (element[f] === null || element[f] === undefined || element[f] === "") {
                    ctrl = false;
                }
            })
        });
        return ctrl;
    }

    /**
     * Verify that the fields are unique
     * 
     * @param array 
     * @param idFieldName fieldName to check
     * @returns true if there are no duplicates
     */
    hasDuplicateIds(array: any[], ...idFieldName: string[]): boolean {
        let ctrl: boolean = true;
        if (idFieldName.length == 1) {
            idFieldName.forEach(fn => {
                let oldArrayId = array.filter(x => !x.variableGridArray.id.includes("new")).map(x => x[fn]);
                array.filter(x => x.variableGridArray.id.includes("new")).map(x => x[fn]).forEach(newId =>
                    oldArrayId.forEach(old => {
                        if (old == newId) ctrl = false;
                    })
                );
            });
        }
        if (idFieldName.length > 1) {

            let oldArray = array.filter(x => !x.variableGridArray.id.includes("new"));
            let newArray = array.filter(x => x.variableGridArray.id.includes("new"));

            for (let i = 0; i < oldArray.length; i++) {
                let diff: boolean = false;
                const elementI = oldArray[i];
                for (let j = 0; j < newArray.length; j++) {
                    const elementJ = newArray[j];

                    for (let fn = 0; fn < idFieldName.length; fn++) {
                        const name = idFieldName[fn];

                        if (JSON.stringify(elementI[name]) !== JSON.stringify(elementJ[name])) {
                            diff = true;
                        }

                    }

                    if (!diff) {
                        ctrl = false;
                        break;
                    }

                }

                if (!ctrl) break;

            }

        }

        return ctrl;
    }

    /**
   * Create a new date set to UTC
   * 
   * @param date 
   * @returns 
   */
    setDateUtc(date: Date): Date {
        return (!!date) ? new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0)) : null;
    }

    sortDataDW(a, b, param: string) {
        let aa = a[param];
        let bb = b[param];
        if (!!aa
            && !!bb) {
            const nameA = aa.toUpperCase(); // ignore upper and lowercase
            const nameB = bb.toUpperCase(); // ignore upper and lowercase
            if (nameA < nameB) {
                return -1;
            }
            if (nameA > nameB) {
                return 1;
            }
            // names must be equal
            return 0;
        }
        else return 0;
    }


}
