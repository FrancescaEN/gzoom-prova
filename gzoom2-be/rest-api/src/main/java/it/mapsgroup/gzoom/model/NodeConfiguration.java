package it.mapsgroup.gzoom.model;

/**
 * @author Andrea Fossi.
 */
public class NodeConfiguration extends Node {
    private boolean showCompanyCciaa;
    private boolean generateContractNumber;
    private boolean showContractAuthorizedAmount;
    private boolean showContractCustomerAuthorization;
    private boolean showContractAuthorization;
    private boolean contractMgoDataAlwaysEditable;


    public boolean isShowCompanyCciaa() {
        return showCompanyCciaa;
    }

    public void setShowCompanyCciaa(boolean showCompanyCciaa) {
        this.showCompanyCciaa = showCompanyCciaa;
    }

    public boolean isGenerateContractNumber() {
        return generateContractNumber;
    }

    public void setGenerateContractNumber(boolean generateContractNumber) {
        this.generateContractNumber = generateContractNumber;
    }

    public boolean isShowContractAuthorizedAmount() {
        return showContractAuthorizedAmount;
    }

    public void setShowContractAuthorizedAmount(boolean showContractAuthorizedAmount) {
        this.showContractAuthorizedAmount = showContractAuthorizedAmount;
    }

    public boolean isShowContractCustomerAuthorization() {
        return showContractCustomerAuthorization;
    }

    public void setShowContractCustomerAuthorization(boolean showContractCustomerAuthorization) {
        this.showContractCustomerAuthorization = showContractCustomerAuthorization;
    }

    public boolean isShowContractAuthorization() {
        return showContractAuthorization;
    }

    public void setShowContractAuthorization(boolean showContractAuthorization) {
        this.showContractAuthorization = showContractAuthorization;
    }

    public boolean isContractMgoDataAlwaysEditable() {
        return contractMgoDataAlwaysEditable;
    }

    public void setContractMgoDataAlwaysEditable(boolean contractMgoDataAlwaysEditable) {
        this.contractMgoDataAlwaysEditable = contractMgoDataAlwaysEditable;
    }
}
