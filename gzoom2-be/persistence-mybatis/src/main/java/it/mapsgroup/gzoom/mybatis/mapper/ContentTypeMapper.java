package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.ContentType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ContentTypeMapper {

    @Delete({
        "delete from content_type",
        "where content_type_id = #{contentTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String contentTypeId);


    @Insert({
        "insert into content_type (content_type_id, parent_type_id, ",
        "has_table, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{contentTypeId,jdbcType=VARCHAR}, #{parentTypeId,jdbcType=VARCHAR}, ",
        "#{hasTable,jdbcType=CHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(ContentType row);


    @Select({
        "select",
        "content_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from content_type",
        "where content_type_id = #{contentTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "contentType", value = {
        @Result(column="content_type_id", property="contentTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_type_id", property="parentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="has_table", property="hasTable", jdbcType=JdbcType.CHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    ContentType selectByPrimaryKey(String contentTypeId);

    @Select({
            "select",
            "content_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp",
            "from content_type",
            "where parent_type_id = #{parentTypeId,jdbcType=VARCHAR}"
    })
    @ResultMap("contentType")
    List<ContentType> selectByParentTypeId(@Param("parentTypeId") String parentTypeId);


    @Select({
        "select",
        "content_type_id, parent_type_id, has_table, description, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from content_type"
    })
    @ResultMap("contentType")
    List<ContentType> selectAll();


    @Update({
        "update content_type",
        "set parent_type_id = #{parentTypeId,jdbcType=VARCHAR},",
          "has_table = #{hasTable,jdbcType=CHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where content_type_id = #{contentTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(ContentType row);
}