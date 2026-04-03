package it.mapsgroup.gzoom.commons;

public class InfoPage {

    private int limit;
    private int offset;
    private Filter[] filter;
    private Filter[] filterGenericLabel;
    private int sortOrder;
    private String sortField;
    private String organizationId;
    private boolean secondaryLang;
    private String matchModeSearch;

    public Filter[] getFilterGenericLabel() {
        return filterGenericLabel;
    }

    public String getMatchModeSearch() {
        return matchModeSearch;
    }

    public void setMatchModeSearch(String matchModeSearch) {
        this.matchModeSearch = matchModeSearch;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Filter[] getFilter() {
        return filter;
    }

    public void setFilters(Filter[] filter) {
        this.filter = filter;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public boolean getSecondaryLang() {
        return secondaryLang;
    }

    public void setSecondaryLang(boolean secondaryLang) {
        this.secondaryLang = secondaryLang;
    }
}
