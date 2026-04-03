package it.mapsgroup.gzoom.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Fossi.
 */
public class ContractCatalog {
    private List<ContractCatalogItem> types;
    private List<ContractCatalogItem> authorizations;
    private List<ContractCatalogItem> commitmentModes;

    public ContractCatalog() {
        types = new ArrayList<>();
        authorizations = new ArrayList<>();
        commitmentModes = new ArrayList<>();
    }

    public List<ContractCatalogItem> getTypes() {
        return types;
    }

    public void setTypes(List<ContractCatalogItem> types) {
        this.types = types;
    }

    public List<ContractCatalogItem> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<ContractCatalogItem> authorizations) {
        this.authorizations = authorizations;
    }

    public List<ContractCatalogItem> getCommitmentModes() {
        return commitmentModes;
    }

    public void setCommitmentModes(List<ContractCatalogItem> commitmentModes) {
        this.commitmentModes = commitmentModes;
    }
}
