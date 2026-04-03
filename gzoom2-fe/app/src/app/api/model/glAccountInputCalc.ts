import { GlAccount } from "./glAccount";
import { GlFiscalType } from "./glFiscalType";

export class GlAccountInputCalc {
    glAccountInputCalcId: string;
    glAccountId: string;
    inputSequenceNum: string;
    glAccountIdRef: string;
    factorCalculator: string;
    glFiscalTypeId: string;

    glAccount: GlAccount;
    glAccountRef: GlAccount;
    glFiscalType: GlFiscalType;
}