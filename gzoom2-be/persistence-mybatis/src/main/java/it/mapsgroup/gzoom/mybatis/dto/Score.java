package it.mapsgroup.gzoom.mybatis.dto;

public class Score {
    private CustomTimePeriod customTimePeriod;
    private GlAccount glAccount;
    private AcctgTransEntry acctgTransEntry;
    private GlFiscalType glFiscalType;

    public CustomTimePeriod getCustomTimePeriod() {
        return customTimePeriod;
    }

    public void setCustomTimePeriod(CustomTimePeriod customTimePeriod) {
        this.customTimePeriod = customTimePeriod;
    }

    public AcctgTransEntry getAcctgTransEntry() {
        return acctgTransEntry;
    }

    public void setAcctgTransEntry(AcctgTransEntry acctgTransEntry) {
        this.acctgTransEntry = acctgTransEntry;
    }

    public GlFiscalType getGlFiscalType() {
        return glFiscalType;
    }

    public void setGlFiscalType(GlFiscalType glFiscalType) {
        this.glFiscalType = glFiscalType;
    }


    public GlAccount getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(GlAccount glAccount) {
        this.glAccount = glAccount;
    }
}
