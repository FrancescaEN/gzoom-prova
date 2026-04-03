/**
 * Model of CustomTimePeriod
 */
export class CustomTimePeriod {
  constructor(
    public customTimePeriodId?: string,
    public customTimePeriodCode?: string,
    public customTimePeriodCodeLang?: string,
    public periodNum?: number,
    public periodTypeId?: string,
    public periodName?: string,
    public periodNameLang?: string,
    public isClosed?: string,
    public parentPeriodId?: string,
    public fromDate?: Date,
    public thruDate?: Date
  ) { }
}
