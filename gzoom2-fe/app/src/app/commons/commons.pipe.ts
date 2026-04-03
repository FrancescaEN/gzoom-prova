import { Pipe, PipeTransform } from '@angular/core';

import * as _ from 'lodash';
import {isDefined} from "./model/commons";

/**
 * Transforms a user-like object into the fullname string.
 */
@Pipe({ name: 'fullname' })
export class FullnamePipe implements PipeTransform {

  transform(person: { firstName: string, lastName: string }, args?: any): any {
    if (!isDefined(person)) return null;
    return person.firstName + ' ' + person.lastName;
  }

}

/**
 * Transforms any value into one that is suitable for HTML id attribute.
 */
@Pipe({ name: 'asId' })
export class AsIdPipe implements PipeTransform {

  transform(value: any, prefix?: string): any {
    const p = prefix !== undefined ? prefix + '-' : '';
    const v = value ? (_.isString(value) ? value : value.toString()).replace(/[^a-zA-Z0-9]/g, '-') : '';
    return p + v;
  }

}

/**
 * Transforms any value into one that is suitable for HTML class attribute.
 */
@Pipe({ name: 'asClass' })
export class AsClassPipe implements PipeTransform {

  transform(value: any, prefix?: any): any {
    const p = prefix !== undefined ? prefix + '-' : '';
    return p + value.replace(/\./g, '-');
  }

}
