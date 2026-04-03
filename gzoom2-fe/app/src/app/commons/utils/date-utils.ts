export class DateUtils {
  /**
   * Returns date in ISO format: YYYY-MM-DDTHH:mm:ss.sssZ or YYYY-MM-DDThh24:mi:ssZ
   * @param date
   * @param keepMs when false, milliseconds are omitted: YYYY-MM-DDThh24:mi:ssZ
   */
  static dateToISO(date: Date, keepMs: boolean = false): string {
    return keepMs ? date.toISOString() : date.toISOString().substring(0, 19) + 'Z';
  }

  /**
   * Converts ISO date into Date object
   * @param date Date string: 2025-09-23T17:10:20.756852+02:00
   */
  static dateFromISO(date: string): Date {
    const dot = date.indexOf('.');
    if (dot >= 0) {
      let plus = date.indexOf('+');
      if (plus - dot > 4) {
        // decreasing ms to 3 for older browsers just in case
        date = date.substring(0, dot + 4) + date.substring(plus);
      }
    }
    return new Date(date);
  }
}
