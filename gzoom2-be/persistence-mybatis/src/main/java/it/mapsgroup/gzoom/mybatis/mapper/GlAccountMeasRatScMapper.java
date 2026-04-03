package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountMeasRatSc;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GlAccountMeasRatScMapper {
    @Delete({
        "delete from gl_account_meas_rat_sc",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}",
          "and uom_id = #{uomId,jdbcType=VARCHAR}",
          "and uom_rating_value = #{uomRatingValue,jdbcType=DOUBLE}"
    })
    int deleteByPrimaryKey(@Param("glAccountId") String glAccountId, @Param("uomId") String uomId, @Param("uomRatingValue") Double uomRatingValue);

    @Delete({
            "delete from gl_account_meas_rat_sc",
            "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    int deleteByGlAccountId(@Param("glAccountId") String glAccountId);

    @Insert({
        "insert into gl_account_meas_rat_sc (gl_account_id, uom_id, ",
        "uom_rating_value, uom_code, ",
        "uom_descr, uom_code_lang, ",
        "uom_descr_lang, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{glAccountId,jdbcType=VARCHAR}, #{uomId,jdbcType=VARCHAR}, ",
        "#{uomRatingValue,jdbcType=DOUBLE}, #{uomCode,jdbcType=VARCHAR}, ",
        "#{uomDescr,jdbcType=VARCHAR}, #{uomCodeLang,jdbcType=VARCHAR}, ",
        "#{uomDescrLang,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(GlAccountMeasRatSc row);

    @Select({
        "select",
        "gl_account_id, uom_id, uom_rating_value, uom_code, uom_descr, uom_code_lang, ",
        "uom_descr_lang, last_modified_by_user_login, created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from gl_account_meas_rat_sc",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}",
          "and uom_id = #{uomId,jdbcType=VARCHAR}",
          "and uom_rating_value = #{uomRatingValue,jdbcType=DOUBLE}"
    })
    @Results({
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="uom_id", property="uomId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="uom_rating_value", property="uomRatingValue", jdbcType=JdbcType.DOUBLE, id=true),
        @Result(column="uom_code", property="uomCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_descr", property="uomDescr", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_code_lang", property="uomCodeLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_descr_lang", property="uomDescrLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    GlAccountMeasRatSc selectByPrimaryKey(@Param("glAccountId") String glAccountId, @Param("uomId") String uomId, @Param("uomRatingValue") Double uomRatingValue);

    @Select({
        "select",
        "gl_account_id, uom_id, uom_rating_value, uom_code, uom_descr, uom_code_lang, ",
        "uom_descr_lang, last_modified_by_user_login, created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from gl_account_meas_rat_sc"
    })
    @Results({
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="uom_id", property="uomId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="uom_rating_value", property="uomRatingValue", jdbcType=JdbcType.DOUBLE, id=true),
        @Result(column="uom_code", property="uomCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_descr", property="uomDescr", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_code_lang", property="uomCodeLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="uom_descr_lang", property="uomDescrLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    List<GlAccountMeasRatSc> selectAll();

    @Select({
            "select *",
            "from gl_account_meas_rat_sc",
            "where gl_account_id = #{glAccountId}"
    })
    @ResultType(GlAccountMeasRatSc.class)
    List<GlAccountMeasRatSc> selectByGlAccountId(String glAccountId);

    @Select({
            "select count(*)",
            "from gl_account_meas_rat_sc",
            "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    @ResultType(GlAccountMeasRatSc.class)
    int countByGlAccountId(String glAccountId);

    @Update({
        "update gl_account_meas_rat_sc",
        "set uom_code = #{uomCode,jdbcType=VARCHAR},",
          "uom_descr = #{uomDescr,jdbcType=VARCHAR},",
          "uom_code_lang = #{uomCodeLang,jdbcType=VARCHAR},",
          "uom_descr_lang = #{uomDescrLang,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "created_by_user_login = #{createdByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "created_stamp = #{createdStamp,jdbcType=TIMESTAMP},",
          "created_tx_stamp = #{createdTxStamp,jdbcType=TIMESTAMP}",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}",
          "and uom_id = #{uomId,jdbcType=VARCHAR}",
          "and uom_rating_value = #{uomRatingValue,jdbcType=DOUBLE}"
    })
    int updateByPrimaryKey(GlAccountMeasRatSc row);
}