package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.UomRangeValues;

import java.math.BigDecimal;
import java.util.List;

import it.mapsgroup.gzoom.mybatis.dto.UomRangeValuesExt;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UomRangeValuesMapper {

    @Delete({
        "delete from uom_range_values",
        "where uom_range_values_id = #{uomRangeValuesId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String uomRangeValuesId);

    @Delete({
            "delete from uom_range_values",
            "where uom_range_id = #{uomRangeId,jdbcType=VARCHAR}"
    })
    int deleteByUomRangeId(String uomRangeId);

    @Insert({
        "insert into uom_range_values (uom_range_values_id, uom_range_id, ",
        "comments, is_positive, ",
        "from_value, thru_value, ",
        "icon_content_id, alert, ",
        "range_values_factor, range_values_factor_min, ",
        "color_enum_id, prorate_range, ",
        "comments_lang, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{uomRangeValuesId,jdbcType=VARCHAR}, #{uomRangeId,jdbcType=VARCHAR}, ",
        "#{comments,jdbcType=VARCHAR}, #{isPositive,jdbcType=CHAR}, ",
        "#{fromValue,jdbcType=DOUBLE}, #{thruValue,jdbcType=DOUBLE}, ",
        "#{iconContentId,jdbcType=VARCHAR}, #{alert,jdbcType=CHAR}, ",
        "#{rangeValuesFactor,jdbcType=DOUBLE}, #{rangeValuesFactorMin,jdbcType=DOUBLE}, ",
        "#{colorEnumId,jdbcType=VARCHAR}, #{prorateRange,jdbcType=CHAR}, ",
        "#{commentsLang,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(UomRangeValues row);


    @Select({
        "select",
        "uom_range_values_id, uom_range_id, comments, is_positive, from_value, thru_value, ",
        "icon_content_id, alert, range_values_factor, range_values_factor_min, color_enum_id, ",
        "prorate_range, comments_lang, last_modified_by_user_login, created_by_user_login, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from uom_range_values",
        "where uom_range_values_id = #{uomRangeValuesId,jdbcType=VARCHAR}"
    })
    @Results(id = "uomRangeValues", value = {
        @Result(column="uom_range_values_id", property="uomRangeValuesId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="uom_range_id", property="uomRangeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="comments", property="comments", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_positive", property="isPositive", jdbcType=JdbcType.CHAR),
        @Result(column="from_value", property="fromValue", jdbcType=JdbcType.DOUBLE),
        @Result(column="thru_value", property="thruValue", jdbcType=JdbcType.DOUBLE),
        @Result(column="icon_content_id", property="iconContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="alert", property="alert", jdbcType=JdbcType.CHAR),
        @Result(column="range_values_factor", property="rangeValuesFactor", jdbcType=JdbcType.DOUBLE),
        @Result(column="range_values_factor_min", property="rangeValuesFactorMin", jdbcType=JdbcType.DOUBLE),
        @Result(column="color_enum_id", property="colorEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="prorate_range", property="prorateRange", jdbcType=JdbcType.CHAR),
        @Result(column="comments_lang", property="commentsLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    UomRangeValues selectByPrimaryKey(String uomRangeValuesId);


    @Select({
        "select",
        "uom_range_values_id, uom_range_id, comments, is_positive, from_value, thru_value, ",
        "icon_content_id, alert, range_values_factor, range_values_factor_min, color_enum_id, ",
        "prorate_range, comments_lang, last_modified_by_user_login, created_by_user_login, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from uom_range_values"
    })
    @ResultMap("uomRangeValues")
    List<UomRangeValues> selectAll();

    @Select({
            "select * ",
            "from uom_range_values",
            "where uom_range_id = #{uomRangeId,jdbcType=VARCHAR}",
            "order by uom_range_values_id"
    })
    @ResultMap("uomRangeValues")
    List<UomRangeValues> selectAllByUomRangeIdOrderByPK(@Param("uomRangeId") String uomRangeId);

    @Select({
            "select * ",
            "from uom_range_values",
            "where uom_range_id = #{uomRangeId,jdbcType=VARCHAR}",
            "order by comments"
    })
    @ResultMap("uomRangeValues")
    List<UomRangeValues> selectAllByUomRangeIdOrderByComments(@Param("uomRangeId") String uomRangeId);


    List<UomRangeValuesExt> getPathEmoticon(@Param("rangeDefault") String rangeDefault,@Param("amount") Float amount);

    @Select({
            "select",
            "min(range_values_factor) as min",
            "from uom_range_values",
            "where uom_range_id = #{uomRangeId,jdbcType=VARCHAR}"
    })
    @ResultType(BigDecimal.class)
    List<BigDecimal> getUomRangeValuesMin(@Param("uomRangeId") String uomRangeId);

    @Select({
            "select",
            "max(range_values_factor) as max",
            "from uom_range_values",
           "where uom_range_id = #{uomRangeId,jdbcType=VARCHAR}"
    })
    @ResultType(BigDecimal.class)
    List<BigDecimal> getUomRangeValuesMax(@Param("uomRangeId") String uomRangeId);


    @Update({
        "update uom_range_values",
        "set uom_range_id = #{uomRangeId,jdbcType=VARCHAR},",
          "comments = #{comments,jdbcType=VARCHAR},",
          "is_positive = #{isPositive,jdbcType=CHAR},",
          "from_value = #{fromValue,jdbcType=DOUBLE},",
          "thru_value = #{thruValue,jdbcType=DOUBLE},",
          "icon_content_id = #{iconContentId,jdbcType=VARCHAR},",
          "alert = #{alert,jdbcType=CHAR},",
          "range_values_factor = #{rangeValuesFactor,jdbcType=DOUBLE},",
          "range_values_factor_min = #{rangeValuesFactorMin,jdbcType=DOUBLE},",
          "color_enum_id = #{colorEnumId,jdbcType=VARCHAR},",
          "prorate_range = #{prorateRange,jdbcType=CHAR},",
          "comments_lang = #{commentsLang,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where uom_range_values_id = #{uomRangeValuesId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(UomRangeValues row);
}