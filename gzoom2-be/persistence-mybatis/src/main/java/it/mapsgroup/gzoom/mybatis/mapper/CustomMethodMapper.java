package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.CustomMethod;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CustomMethodMapper {

    @Delete({
        "delete from custom_method",
        "where custom_method_id = #{customMethodId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String customMethodId);


    @Insert({
        "insert into custom_method (custom_method_id, custom_method_type_id, ",
        "custom_method_name, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{customMethodId,jdbcType=VARCHAR}, #{customMethodTypeId,jdbcType=VARCHAR}, ",
        "#{customMethodName,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(CustomMethod row);


    @Select({
        "select",
        "custom_method_id, custom_method_type_id, custom_method_name, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from custom_method",
        "where custom_method_id = #{customMethodId,jdbcType=VARCHAR}"
    })
    @Results(id = "customMethod", value = {
        @Result(column="custom_method_id", property="customMethodId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="custom_method_type_id", property="customMethodTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="custom_method_name", property="customMethodName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    CustomMethod selectByPrimaryKey(String customMethodId);


    @Select({
        "select",
        "custom_method_id, custom_method_type_id, custom_method_name, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from custom_method",
            "order by custom_method_id"
    })
    @ResultMap("customMethod")
    List<CustomMethod> selectAllOrderByPrimaryKey();


    @Update({
        "update custom_method",
        "set custom_method_type_id = #{customMethodTypeId,jdbcType=VARCHAR},",
          "custom_method_name = #{customMethodName,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where custom_method_id = #{customMethodId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(CustomMethod row);
}