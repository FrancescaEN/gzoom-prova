package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.PartyContent;
import java.time.LocalDateTime;
import java.util.List;

import it.mapsgroup.gzoom.mybatis.dto.PartyContentEx;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PartyContentMapper {

    @Delete({
        "delete from party_content",
        "where party_id = #{partyId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}",
          "and party_content_type_id = #{partyContentTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int deleteByPrimaryKey(@Param("partyId") String partyId, @Param("contentId") String contentId, @Param("partyContentTypeId") String partyContentTypeId, @Param("fromDate") LocalDateTime fromDate);


    @Insert({
        "insert into party_content (party_id, content_id, ",
        "party_content_type_id, from_date, ",
        "thru_date, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{partyId,jdbcType=VARCHAR}, #{contentId,jdbcType=VARCHAR}, ",
        "#{partyContentTypeId,jdbcType=VARCHAR}, #{fromDate,jdbcType=TIMESTAMP}, ",
        "#{thruDate,jdbcType=TIMESTAMP}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(PartyContent row);


    @Select({
        "select",
        "party_id, content_id, party_content_type_id, from_date, thru_date, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login",
        "from party_content",
        "where party_id = #{partyId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}",
          "and party_content_type_id = #{partyContentTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    @Results({
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="party_content_type_id", property="partyContentTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    PartyContent selectByPrimaryKey(@Param("partyId") String partyId, @Param("contentId") String contentId, @Param("partyContentTypeId") String partyContentTypeId, @Param("fromDate") LocalDateTime fromDate);


    @Select({
        "select",
        "party_id, content_id, party_content_type_id, from_date, thru_date, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login",
        "from party_content"
    })
    @Results({
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="content_id", property="contentId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="party_content_type_id", property="partyContentTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    List<PartyContent> selectAll();

    List<PartyContentEx> getPartyContent(@Param("partyId") String partyId, @Param("partyContentTypeId") String partyContentTypeId);


    @Update({
        "update party_content",
        "set thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where party_id = #{partyId,jdbcType=VARCHAR}",
          "and content_id = #{contentId,jdbcType=VARCHAR}",
          "and party_content_type_id = #{partyContentTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int updateByPrimaryKey(PartyContent row);
}