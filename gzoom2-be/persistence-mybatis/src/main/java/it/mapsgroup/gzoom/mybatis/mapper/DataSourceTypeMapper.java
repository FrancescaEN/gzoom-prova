package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.DataSourceType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DataSourceTypeMapper {

    @Delete({
        "delete from data_source_type",
        "where data_source_type_id = #{dataSourceTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String dataSourceTypeId);


    @Insert({
        "insert into data_source_type (data_source_type_id, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login)",
        "values (#{dataSourceTypeId,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(DataSourceType row);


    @Select({
        "select",
        "data_source_type_id, description, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, last_modified_by_user_login, created_by_user_login",
        "from data_source_type",
        "where data_source_type_id = #{dataSourceTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "dataSourceType", value = {
        @Result(column="data_source_type_id", property="dataSourceTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    DataSourceType selectByPrimaryKey(String dataSourceTypeId);


    @Select({
        "select",
        "data_source_type_id, description, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, last_modified_by_user_login, created_by_user_login",
        "from data_source_type",
            "order by data_source_type_id"
    })
    @ResultMap("dataSourceType")
    List<DataSourceType> selectAllOrderByPrimaryKey();


    @Update({
        "update data_source_type",
        "set description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where data_source_type_id = #{dataSourceTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(DataSourceType row);
}