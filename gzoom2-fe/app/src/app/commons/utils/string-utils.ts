import { VarUtils } from './var-utils';

export function splitStringEveryNum(label: string, num: number): string[] {
  let arr = label?.split(' ')
  let result = [];
  for (let i = 0; i < arr.length; i += num) {
    result.push(arr.slice(i, i + num).join(' '));
  }
  return result;
}

export class StringUtils {
  static isEmpty(val: any): boolean {
    return !this.notEmpty(val);
  }

  static notEmpty(val: any): boolean {
    return VarUtils.isDefined(val) && (typeof (val) === 'string') && (val.length > 0);
  }

  /**
   * Checks if val is empty or consists only of whitespaces (" ", "\n\t ")
   * @param val
   */
  static isBlank(val: any): boolean {
    return this.isEmpty(val) || /^\s*$/.test(val);
  }

  static trimLeft(haystack: string, needle: string = ' '): string {
    let res = haystack || '';
    while (res.startsWith(needle)) {
      res = res.substring(needle.length);
    }
    return res;
  }

  static trimRight(haystack: string, needle: string = ' '): string {
    let res = haystack || '';
    while (res.endsWith(needle)) {
      res = res.substring(0, res.length - needle.length);
    }
    return res;
  }

  static replaceLast(hay: string, needle: string, replacement: string): string {
    const i = hay.lastIndexOf(needle);
    return i === -1 ? hay : hay.substring(0, i) + replacement + hay.substring(i + needle.length);
  }

  static startsWith(hay: string, needle: string): boolean {
    return hay.indexOf(needle) === 0;
  }

  static subString(value: string, from: number, length: number = 0): string {
    return value.substring(from, length > 0 ? from + length : value.length);
  }
}
