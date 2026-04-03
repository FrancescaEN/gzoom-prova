import { CustomTimePeriod } from "./customTimePeriod"
import { WorkEffortMeasure } from "./workEffortMeasure";

export class AcctgTrans {
    acctgTransId: string;
    transactionDate?: Date;
    acctgTransTypeId?: string;
    isPosted?: string;
    glFiscalTypeId: string;
    voucherRef?: string;
    voucherDate?: string;
    partyId?: string;
    roleTypeId?: string;
    workEffortId?: string;
    description?: string;
    descriptionLang?: string;
    customTimePeriod?: CustomTimePeriod;
    workEffortMeasure?: WorkEffortMeasure;
}