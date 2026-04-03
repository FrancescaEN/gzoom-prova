import { VarUtils } from './var-utils';

export class ArrayUtils {
  static notEmpty(input: any): boolean {
    return VarUtils.isDefined(input) && Array.isArray(input) && input.length > 0;
  }
}
