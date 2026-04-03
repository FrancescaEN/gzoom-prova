package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.ContentAttribute;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ContentAttributeMapper {

    @Delete({
        "delete from content_attribute",
        "where content_id = #{contentId,jdbcType=VARCHAR}",
          "and attr_name = #{attrName,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("contentId") String contentId, @Param("attrName") String attrName);


    @Insert({
        "insert into content_attribute (content_id, attr_name, ",
        "attr_value, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{contentId,jdbcType=VARCHAR}, #{attrName,jdbcType=VARCHAR}, ",
        "#{attrValue,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(ContentAttribute row);


    @Select({
        "select",
        "content_id, attr_name, attr_value, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from content_attribute",
        "where content_id = #{contentId,jdbcType=VARCHAR}",
          "and attr_name = #{attrName,jdbcType=VARCHAR}"
    })
    @Results(id = "contentAttribute", value = {
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="attr_name", property="attrName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="attr_value", property="attrValue", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    ContentAttribute selectByPrimaryKey(@Param("contentId") String contentId, @Param("attrName") String attrName);


    @Select({
        "select",
        "content_id, attr_name, attr_value, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from content_attribute"
    })
    @Results({
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="attr_name", property="attrName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="attr_value", property="attrValue", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    List<ContentAttribute> selectAll();


    @Update({
        "update content_attribute",
        "set attr_value = #{attrValue,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
        "where content_id = #{contentId,jdbcType=VARCHAR}",
          "and attr_name = #{attrName,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(ContentAttribute row);
}