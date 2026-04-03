import { AcctgTransEntry } from "./acctgTransEntry";
import { CustomTimePeriod } from "./customTimePeriod";
import { GlAccount } from "./glAccount";
import { GlFiscalType } from "./glFiscalType";

/**
 * Model of Score
 */
export class Score {
    public glAccount?: GlAccount;
    public glFiscalType?: GlFiscalType;
    public customTimePeriod?: CustomTimePeriod;
    public acctgTransEntry?: AcctgTransEntry;
}