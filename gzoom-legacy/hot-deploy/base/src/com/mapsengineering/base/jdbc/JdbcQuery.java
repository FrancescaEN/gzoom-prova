package com.mapsengineering.base.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericDataSourceException;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.jdbc.SQLProcessor;

import com.mapsengineering.base.util.logging.FormattedStringBuilder;

public class JdbcQuery extends JdbcStatement {

    private String queryString;
    private List<JdbcParam> params;

    public JdbcQuery(Delegator delegator, String queryString) {
        super(delegator);
        this.queryString = queryString;
    }

    public String getQueryString() {
        if (queryString == null) {
            queryString = buildQueryString();
        }
        return queryString;
    }

    public List<JdbcParam> getParams() {
        return params;
    }

    public void clearParams() {
        if (params != null) {
            params.clear();
        }
    }

    public String upperCase(String value) {
        if (UtilValidate.isNotEmpty(value)) {
            value = value.replaceAll("ß", "ẞ"); // ß vs ẞ
            // Debug.log("upperCase value " + value);
            value = value.toUpperCase();
            // Debug.log("upperCase value " + value);
            value = value.replaceAll("ẞ", "ß"); // ẞ vs ß
        }
        return value;
    }
    
    public void addParam(Object value, int jdbcType) {
        if (params == null) {
            params = new ArrayList<JdbcParam>();
        }
        params.add(new JdbcParam(value, jdbcType));
    }

    public JdbcQueryIterator iterate() {
        return new JdbcQueryIterator(this);
    }

    public <T extends ResultSetWrapper> JdbcQueryWrapperIterator<T> iterate(T record) {
        return new JdbcQueryWrapperIterator<T>(this, record);
    }
    
    public JdbcQueryResult run() {
        return new JdbcQueryResult(this);
    }

    @Override
    public String toString() {
        return new FormattedStringBuilder().braceOpen().nl().indent() //
                .append("command:").nl().indent() //
                .append(getQueryString()).nl().unindent() //
                .append("params:").nl().indent() //
                .append(params).nl().unindent() //
                .unindent().braceClose().toString();
    }

    protected SQLProcessor prepareStatement(SQLProcessor sqlP) throws GenericDataSourceException, GenericEntityException, SQLException {
        return prepareStatement(sqlP, getQueryString(), getParams());
    }

    protected String buildQueryString() {
        return null;
    }
}
