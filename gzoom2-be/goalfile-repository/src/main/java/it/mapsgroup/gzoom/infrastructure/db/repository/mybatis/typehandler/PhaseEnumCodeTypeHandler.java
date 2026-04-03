package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.typehandler;

import io.micrometer.common.util.StringUtils;
import it.mapsgroup.gzoom.entity.goalfile.model.PhaseEnumCode;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

public class PhaseEnumCodeTypeHandler implements TypeHandler<PhaseEnumCode> {
    @Override
    public void setParameter(PreparedStatement ps, int i, PhaseEnumCode phaseEnumCode, JdbcType jdbcType) throws SQLException {
        if (phaseEnumCode == null) {
            ps.setNull(i, Types.VARCHAR);
        } else {
            ps.setString(i, phaseEnumCode.name());
        }
    }

    @Override
    public PhaseEnumCode getResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return StringUtils.isEmpty(code) ? null : PhaseEnumCode.valueOf(code);
    }

    @Override
    public PhaseEnumCode getResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return StringUtils.isEmpty(code) ? null : PhaseEnumCode.valueOf(code);
    }

    @Override
    public PhaseEnumCode getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return StringUtils.isEmpty(code) ? null : PhaseEnumCode.valueOf(code);
    }
}
