package it.mapsgroup.gzoom.commons;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Map;

public class TableLazyLoad {
    @JsonAlias("rows")
    private Integer limit;
    @JsonAlias("first")
    private Integer offset;
    private boolean isSecondaryLang;
    private String sortField;
    private Integer sortOrder;
    private Map<String, Filter[]> filters;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public boolean isSecondaryLang() {
        return isSecondaryLang;
    }

    public void setSecondaryLang(boolean secondaryLang) {
        isSecondaryLang = secondaryLang;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Map<String, Filter[]> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Filter[]> filters) {
        this.filters = filters;
    }
}
