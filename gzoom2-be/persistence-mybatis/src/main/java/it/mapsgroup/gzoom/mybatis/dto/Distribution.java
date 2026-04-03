package it.mapsgroup.gzoom.mybatis.dto;

public class Distribution {
    //Campi query_config
    private String queryId;
    private String workEffortId;
    private String userId;
    private String cond0;
    private String cond1;
    private String cond2;
    private String cond3;
    private String cond4;
    private String cond5;
    private String cond6;
    private String cond7;

    private String fileName; //Nome del file generato
    private String outputPath; //Cartella dove salvare il file

    private String commEventId;
    private String emailSubject;
    private String emailBody;
    private String bodyMimeType;
    private String emailFrom;
    private String emailTo;
    private String partyIdTo;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getWorkEffortId() {
        return workEffortId;
    }

    public void setWorkEffortId(String workEffortId) {
        this.workEffortId = workEffortId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCond0() {
        return cond0;
    }

    public void setCond0(String cond0) {
        this.cond0 = cond0;
    }

    public String getCond1() {
        return cond1;
    }

    public void setCond1(String cond1) {
        this.cond1 = cond1;
    }

    public String getCond2() {
        return cond2;
    }

    public void setCond2(String cond2) {
        this.cond2 = cond2;
    }

    public String getCond3() {
        return cond3;
    }

    public void setCond3(String cond3) {
        this.cond3 = cond3;
    }

    public String getCond4() {
        return cond4;
    }

    public void setCond4(String cond4) {
        this.cond4 = cond4;
    }

    public String getCond5() {
        return cond5;
    }

    public void setCond5(String cond5) {
        this.cond5 = cond5;
    }

    public String getCond6() {
        return cond6;
    }

    public void setCond6(String cond6) {
        this.cond6 = cond6;
    }

    public String getCond7() {
        return cond7;
    }

    public void setCond7(String cond7) {
        this.cond7 = cond7;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getCommEventId() {
        return commEventId;
    }

    public void setCommEventId(String commEventId) {
        this.commEventId = commEventId;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getBodyMimeType() {
        return bodyMimeType;
    }

    public void setBodyMimeType(String bodyMimeType) {
        this.bodyMimeType = bodyMimeType;
    }

    public String getPartyIdTo() {
        return partyIdTo;
    }

    public void setPartyIdTo(String partyIdTo) {
        this.partyIdTo = partyIdTo;
    }
}
