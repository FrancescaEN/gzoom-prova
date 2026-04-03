import { AcctgTrans } from "./acctgTrans";
import { GlAccount } from "./glAccount";
import { GlFiscalType } from "./glFiscalType";
import { Uom } from "./uom";

/**
 * Model of AcctgTransEntry
 */
export class AcctgTransEntry {
    acctgTransId: string;
    acctgTrans?: AcctgTrans;
    acctgTransEntrySeqId: string;
    acctgTransEntryTypeId?: string;
    description?: string;
    voucherRef?: string;
    partyId?: string;
    roleTypeId?: string;
    theirPartyId?: string;
    productId?: string;
    theirProductId?: string;
    inventoryItemId?: string;
    glAccountTypeId?: string;
    glAccountId?: string;
    glAccount?: GlAccount;
    organizationPartyId?: string;
    amount?: number;
    currencyUomId?: string;
    currencyUom?: Uom;
    origAmount?: number;
    origCurrencyUomId?: string;
    debitCreditFlag?: string;
    dueDate?: Date;
    groupId?: string;
    taxId?: string;
    reconcileStatusId?: string;
    settlementTermId?: string;
    isSummary?: string;
    perfAmountCalc?: number;
    amountLocked?: string;
    workEffortSnapshotId?: string;
    snapshotDate?: Date;
    workEffortRevisionId?: string;
    descriptionLang?: string;
    glFiscalTypeId?: string;
    glFiscalType?: GlFiscalType;
    fromDateCompetence?: Date;
    toDateCompetence?: Date;
    glAccountFinId?: string;
    emplPositionTypeId?: string;
    hasScoreAlert?: string;
    perfAmountTarget?: number;
    perfAmountActual?: number;
    perfAmountMin?: number;
    perfAmountMax?: number;
    checkAmount1?: string;
    checkAmount2?: string;
    checkAmount3?: string;
    textValue1?: string;
}