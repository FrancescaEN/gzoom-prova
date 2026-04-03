package it.mapsgroup.gzoom.mybatis.dto;

public class DataSourceEx extends DataSource {
    private Enumeration enumeration;
    private DataSourceType dataSourceType;

    public DataSourceType getDataSourceType() {
        return dataSourceType;
    }

    public Enumeration getEnumeration() {
        return enumeration;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public void setEnumeration(Enumeration enumeration) {
        this.enumeration = enumeration;
    }
}
