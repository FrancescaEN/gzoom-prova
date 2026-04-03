package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.PeriodType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PeriodTypeMapper {

    @Delete({
        "delete from period_type",
        "where period_type_id = #{periodTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String periodTypeId);

    @Insert({
        "insert into period_type (period_type_id, description, ",
        "period_length, uom_id, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login)",
        "values (#{periodTypeId,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{periodLength,jdbcType=NUMERIC}, #{uomId,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(PeriodType row);

    @Select({
        "select",
        "period_type_id, description, period_length, uom_id, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, last_modified_by_user_login, created_by_user_login",
        "from period_type",
        "where period_type_id = #{periodTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "periodType", value = {
        @Result(column="period_type_id", property="periodTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="period_length", property="periodLength", jdbcType=JdbcType.NUMERIC),
        @Result(column="uom_id", property="uomId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    PeriodType selectByPrimaryKey(String periodTypeId);

    @Select({
        "select",
        "period_type_id, description, period_length, uom_id, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, last_modified_by_user_login, created_by_user_login",
        "from period_type",
            "order by period_type_id"
    })
    @ResultMap("periodType")
    List<PeriodType> selectAllOrderByPrimaryKey();

    @Update({
        "update period_type",
        "set description = #{description,jdbcType=VARCHAR},",
          "period_length = #{periodLength,jdbcType=NUMERIC},",
          "uom_id = #{uomId,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where period_type_id = #{periodTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(PeriodType row);
}