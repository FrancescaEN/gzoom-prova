package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.ContentAssoc;
import java.time.Instant;
import java.util.List;

import it.mapsgroup.gzoom.mybatis.dto.ContentAssocMenu;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ContentAssocMapper {

    @Delete({
        "delete from content_assoc",
        "where content_id = #{contentId,jdbcType=VARCHAR}",
          "and content_id_to = #{contentIdTo,jdbcType=VARCHAR}",
          "and content_assoc_type_id = #{contentAssocTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int deleteByPrimaryKey(@Param("contentId") String contentId, @Param("contentIdTo") String contentIdTo, @Param("contentAssocTypeId") String contentAssocTypeId, @Param("fromDate") Instant fromDate);


    @Insert({
        "insert into content_assoc (content_id, content_id_to, ",
        "content_assoc_type_id, from_date, ",
        "thru_date, content_assoc_predicate_id, ",
        "data_source_id, sequence_num, ",
        "map_key, upper_coordinate, ",
        "left_coordinate, created_date, ",
        "created_by_user_login, last_modified_date, ",
        "last_modified_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{contentId,jdbcType=VARCHAR}, #{contentIdTo,jdbcType=VARCHAR}, ",
        "#{contentAssocTypeId,jdbcType=VARCHAR}, #{fromDate,jdbcType=TIMESTAMP}, ",
        "#{thruDate,jdbcType=TIMESTAMP}, #{contentAssocPredicateId,jdbcType=VARCHAR}, ",
        "#{dataSourceId,jdbcType=VARCHAR}, #{sequenceNum,jdbcType=NUMERIC}, ",
        "#{mapKey,jdbcType=VARCHAR}, #{upperCoordinate,jdbcType=NUMERIC}, ",
        "#{leftCoordinate,jdbcType=NUMERIC}, #{createdDate,jdbcType=TIMESTAMP}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR}, #{lastModifiedDate,jdbcType=TIMESTAMP}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(ContentAssoc row);


    @Select({
        "select",
        "content_id, content_id_to, content_assoc_type_id, from_date, thru_date, content_assoc_predicate_id, ",
        "data_source_id, sequence_num, map_key, upper_coordinate, left_coordinate, created_date, ",
        "created_by_user_login, last_modified_date, last_modified_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from content_assoc",
        "where content_id = #{contentId,jdbcType=VARCHAR}",
          "and content_id_to = #{contentIdTo,jdbcType=VARCHAR}",
          "and content_assoc_type_id = #{contentAssocTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    @Results(id = "contentAssoc", value = {
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="content_id_to", property="contentIdTo", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="content_assoc_type_id", property="contentAssocTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="content_assoc_predicate_id", property="contentAssocPredicateId", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_source_id", property="dataSourceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="sequence_num", property="sequenceNum", jdbcType=JdbcType.NUMERIC),
        @Result(column="map_key", property="mapKey", jdbcType=JdbcType.VARCHAR),
        @Result(column="upper_coordinate", property="upperCoordinate", jdbcType=JdbcType.NUMERIC),
        @Result(column="left_coordinate", property="leftCoordinate", jdbcType=JdbcType.NUMERIC),
        @Result(column="created_date", property="createdDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_date", property="lastModifiedDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    ContentAssoc selectByPrimaryKey(@Param("contentId") String contentId, @Param("contentIdTo") String contentIdTo, @Param("contentAssocTypeId") String contentAssocTypeId, @Param("fromDate") Instant fromDate);


    @Select({
        "select",
        "content_id, content_id_to, content_assoc_type_id, from_date, thru_date, content_assoc_predicate_id, ",
        "data_source_id, sequence_num, map_key, upper_coordinate, left_coordinate, created_date, ",
        "created_by_user_login, last_modified_date, last_modified_by_user_login, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from content_assoc"
    })
    @ResultMap("contentAssoc")
    List<ContentAssoc> selectAll();

    @Select({
            "select",
            "content_id, content_id_to, content_assoc_type_id, from_date, thru_date, content_assoc_predicate_id, ",
            "data_source_id, sequence_num, map_key, upper_coordinate, left_coordinate, created_date, ",
            "created_by_user_login, last_modified_date, last_modified_by_user_login, last_updated_stamp, ",
            "last_updated_tx_stamp, created_stamp, created_tx_stamp",
            "from content_assoc",
            "where content_id_to = #{contentIdTo,jdbcType=VARCHAR}",
            "and content_assoc_type_id = 'HELP'"
    })
    @ResultMap("contentAssoc")
    List<ContentAssoc> getHelpId(@Param("contentIdTo") String contentIdTo);

    List<ContentAssocMenu> getMenuPath(@Param("contentIdTo") String contentIdTo);


    @Update({
        "update content_assoc",
        "set thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "content_assoc_predicate_id = #{contentAssocPredicateId,jdbcType=VARCHAR},",
          "data_source_id = #{dataSourceId,jdbcType=VARCHAR},",
          "sequence_num = #{sequenceNum,jdbcType=NUMERIC},",
          "map_key = #{mapKey,jdbcType=VARCHAR},",
          "upper_coordinate = #{upperCoordinate,jdbcType=NUMERIC},",
          "left_coordinate = #{leftCoordinate,jdbcType=NUMERIC},",
          "last_modified_date = #{lastModifiedDate,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where content_id = #{contentId,jdbcType=VARCHAR}",
          "and content_id_to = #{contentIdTo,jdbcType=VARCHAR}",
          "and content_assoc_type_id = #{contentAssocTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int updateByPrimaryKey(ContentAssoc row);
}