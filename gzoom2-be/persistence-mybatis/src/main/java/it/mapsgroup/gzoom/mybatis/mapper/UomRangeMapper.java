package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.UomRange;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UomRangeMapper {

    @Delete({
        "delete from uom_range",
        "where uom_range_id = #{uomRangeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String uomRangeId);


    @Insert({
        "insert into uom_range (uom_range_id, uom_id, ",
        "description, last_modified_by_user_login, ",
        "created_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{uomRangeId,jdbcType=VARCHAR}, #{uomId,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(UomRange row);


    @Select({
        "select",
        "uom_range_id, uom_id, description, last_modified_by_user_login, created_by_user_login, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from uom_range",
        "where uom_range_id = #{uomRangeId,jdbcType=VARCHAR}"
    })
    @Results(id = "uomRange", value = {
        @Result(column="uom_range_id", property="uomRangeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="uom_id", property="uomId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    UomRange selectByPrimaryKey(String uomRangeId);


    @Select({
        "select",
        "uom_range_id, uom_id, description, last_modified_by_user_login, created_by_user_login, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from uom_range",
            "order by uom_range_id"
    })
    @ResultMap("uomRange")
    List<UomRange> selectAllOrderByPrimaryKey();


    @Update({
        "update uom_range",
        "set uom_id = #{uomId,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where uom_range_id = #{uomRangeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(UomRange row);
}