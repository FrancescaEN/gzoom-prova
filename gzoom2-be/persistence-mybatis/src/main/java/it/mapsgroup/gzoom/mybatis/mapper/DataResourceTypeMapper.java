package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.DataResourceType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataResourceTypeMapper {

    @Delete({
        "delete from data_resource_type",
        "where data_resource_type_id = #{dataResourceTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String dataResourceTypeId);


    @Insert({
        "insert into data_resource_type (data_resource_type_id, parent_type_id, ",
        "has_table, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{dataResourceTypeId,jdbcType=VARCHAR}, #{parentTypeId,jdbcType=VARCHAR}, ",
        "#{hasTable,jdbcType=CHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(DataResourceType row);


    @Select({
        "select",
        "data_resource_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from data_resource_type",
        "where data_resource_type_id = #{dataResourceTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "dataResourceType", value = {
        @Result(column="data_resource_type_id", property="dataResourceTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_type_id", property="parentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="has_table", property="hasTable", jdbcType=JdbcType.CHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    DataResourceType selectByPrimaryKey(String dataResourceTypeId);


    @Select({
        "select",
        "data_resource_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from data_resource_type"
    })
    @ResultMap("dataResourceType")
    List<DataResourceType> selectAll();


    @Update({
        "update data_resource_type",
        "set parent_type_id = #{parentTypeId,jdbcType=VARCHAR},",
          "has_table = #{hasTable,jdbcType=CHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where data_resource_type_id = #{dataResourceTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(DataResourceType row);
}