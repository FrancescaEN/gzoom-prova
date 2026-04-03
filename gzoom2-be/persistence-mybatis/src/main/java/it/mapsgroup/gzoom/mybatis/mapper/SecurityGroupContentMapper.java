package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.SecurityGroupContent;

import java.time.Instant;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SecurityGroupContentMapper {
    @Delete({
        "delete from security_group_content",
        "where group_id = #{groupId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int deleteByPrimaryKey(@Param("groupId") String groupId, @Param("contentId") String contentId, @Param("fromDate") Instant fromDate);


    @Delete({
            "delete from security_group_content",
            "where group_id = #{groupId,jdbcType=VARCHAR}"
    })
    int deleteByGroupId(@Param("groupId") String groupId);

    @Insert({
        "insert into security_group_content (group_id, content_id, ",
        "from_date, thru_date, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{groupId,jdbcType=VARCHAR}, #{contentId,jdbcType=VARCHAR}, ",
        "#{fromDate,jdbcType=TIMESTAMP}, #{thruDate,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(SecurityGroupContent row);

    @Select({
        "select",
        "group_id, content_id, from_date, thru_date, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from security_group_content",
        "where group_id = #{groupId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    SecurityGroupContent selectByPrimaryKey(@Param("groupId") String groupId, @Param("contentId") String contentId, @Param("fromDate") Instant fromDate);


    @Select({
            "select",
            "group_id, content_id, from_date, thru_date, last_updated_stamp, last_updated_tx_stamp, ",
            "created_stamp, created_tx_stamp",
            "from security_group_content",
            "where group_id = #{groupId,jdbcType=VARCHAR}",
            "order by content_id, from_date"
    })
    @ResultType(SecurityGroupContent.class)
    List<SecurityGroupContent> selectByGroupIdOrdByContentIdAndFromDate(@Param("groupId") String groupId);

    @Select({
        "select",
        "group_id, content_id, from_date, thru_date, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from security_group_content"
    })
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    List<SecurityGroupContent> selectAll();

    @Update({
        "update security_group_content",
        "set thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where group_id = #{groupId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int updateByPrimaryKey(SecurityGroupContent row);
}