package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountTypeGlFiscalType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GlAccountTypeGlFiscalTypeMapper {

    @Delete({
        "delete from gl_account_type_gl_fiscal_type",
        "where gl_account_type_id = #{glAccountTypeId,jdbcType=VARCHAR}",
          "and gl_fiscal_type_id = #{glFiscalTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("glAccountTypeId") String glAccountTypeId, @Param("glFiscalTypeId") String glFiscalTypeId);

    @Delete({
            "delete from gl_account_type_gl_fiscal_type",
            "where gl_account_type_id = #{glAccountTypeId,jdbcType=VARCHAR}"
    })
    int deleteByGlAccountTypeId(@Param("glAccountTypeId") String glAccountTypeId);


    @Insert({
        "insert into gl_account_type_gl_fiscal_type (gl_account_type_id, gl_fiscal_type_id, ",
        "sequence_id, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{glAccountTypeId,jdbcType=VARCHAR}, #{glFiscalTypeId,jdbcType=VARCHAR}, ",
        "#{sequenceId,jdbcType=NUMERIC}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(GlAccountTypeGlFiscalType row);


    @Select({
        "select",
        "gl_account_type_id, gl_fiscal_type_id, sequence_id, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp",
        "from gl_account_type_gl_fiscal_type",
        "where gl_account_type_id = #{glAccountTypeId,jdbcType=VARCHAR}",
          "and gl_fiscal_type_id = #{glFiscalTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "glAccountTypeGlFiscalType", value = {
        @Result(column="gl_account_type_id", property="glAccountTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="gl_fiscal_type_id", property="glFiscalTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="sequence_id", property="sequenceId", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    GlAccountTypeGlFiscalType selectByPrimaryKey(@Param("glAccountTypeId") String glAccountTypeId, @Param("glFiscalTypeId") String glFiscalTypeId);


    @Select({
            "select",
            "gl_account_type_id, gl_fiscal_type_id, sequence_id, last_modified_by_user_login, ",
            "created_by_user_login, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
            "created_tx_stamp",
            "from gl_account_type_gl_fiscal_type",
            "where gl_account_type_id = #{glAccountTypeId,jdbcType=VARCHAR}"
    })
    @ResultMap("glAccountTypeGlFiscalType")
    List<GlAccountTypeGlFiscalType> selectByGlAccountTypeId(@Param("glAccountTypeId") String glAccountTypeId);


    @Select({
        "select",
        "gl_account_type_id, gl_fiscal_type_id, sequence_id, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp",
        "from gl_account_type_gl_fiscal_type"
    })
    @ResultMap("glAccountTypeGlFiscalType")
    List<GlAccountTypeGlFiscalType> selectAll();


    @Update({
        "update gl_account_type_gl_fiscal_type",
        "set sequence_id = #{sequenceId,jdbcType=NUMERIC},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where gl_account_type_id = #{glAccountTypeId,jdbcType=VARCHAR}",
          "and gl_fiscal_type_id = #{glFiscalTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(GlAccountTypeGlFiscalType row);
}