import { KeyValuePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

const keepOrder = (a, b) => a;

@Pipe({
  name: 'defaultOrderKeyValue',
  standalone: true
})
export class DefaultOrderKeyValuePipe extends KeyValuePipe implements PipeTransform {

  transform(value: any, ...args: any[]): any {
    return super.transform(value, keepOrder);
  }

}
