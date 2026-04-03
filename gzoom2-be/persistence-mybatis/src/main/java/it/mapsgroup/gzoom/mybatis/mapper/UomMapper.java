package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.Uom;
import it.mapsgroup.gzoom.mybatis.dto.UomEx;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UomMapper {

    @Delete({
        "delete from uom",
        "where uom_id = #{uomId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String uomId);


    @Insert({
        "insert into uom (uom_id, uom_type_id, ",
        "abbreviation, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "decimal_scale, min_value, ",
        "max_value, abbreviation_lang, ",
        "description_lang, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{uomId,jdbcType=VARCHAR}, #{uomTypeId,jdbcType=VARCHAR}, ",
        "#{abbreviation,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{decimalScale,jdbcType=NUMERIC}, #{minValue,jdbcType=DOUBLE}, ",
        "#{maxValue,jdbcType=DOUBLE}, #{abbreviationLang,jdbcType=VARCHAR}, ",
        "#{descriptionLang,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(Uom row);


    @Select({
        "select",
        "uom_id, uom_type_id, abbreviation, description, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, decimal_scale, min_value, max_value, abbreviation_lang, ",
        "description_lang, last_modified_by_user_login, created_by_user_login",
        "from uom",
        "where uom_id = #{uomId,jdbcType=VARCHAR}"
    })
    @Results(id = "uom", value = {
        @Result(column="uom_id", property="uomId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="uom_type_id", property="uomTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="abbreviation", property="abbreviation", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="decimal_scale", property="decimalScale", jdbcType=JdbcType.NUMERIC),
        @Result(column="min_value", property="minValue", jdbcType=JdbcType.DOUBLE),
        @Result(column="max_value", property="maxValue", jdbcType=JdbcType.DOUBLE),
        @Result(column="abbreviation_lang", property="abbreviationLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    Uom selectByPrimaryKey(String uomId);

    @Select({
            "select u.*",
            "from uom u",
            "join gl_account ga on ga.default_uom_id = u.uom_id",
            "where ga.gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    @ResultType(Uom.class)
    Uom selectByGlAccountId(String glAccountId);

    @Select({
        "select",
        "uom_id, uom_type_id, abbreviation, description, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, decimal_scale, min_value, max_value, abbreviation_lang, ",
        "description_lang, last_modified_by_user_login, created_by_user_login",
        "from uom"
    })
    @ResultMap("uom")
    List<Uom> selectAll();

    List<UomEx> getUoms();

    UomEx getUom(@Param("uomId")String uomId);

    @Update({
        "update uom",
        "set uom_type_id = #{uomTypeId,jdbcType=VARCHAR},",
          "abbreviation = #{abbreviation,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "decimal_scale = #{decimalScale,jdbcType=NUMERIC},",
          "min_value = #{minValue,jdbcType=DOUBLE},",
          "max_value = #{maxValue,jdbcType=DOUBLE},",
          "abbreviation_lang = #{abbreviationLang,jdbcType=VARCHAR},",
          "description_lang = #{descriptionLang,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where uom_id = #{uomId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(Uom row);
}