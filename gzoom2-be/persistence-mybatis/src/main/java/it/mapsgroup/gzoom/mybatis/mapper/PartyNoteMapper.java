package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.PartyNote;
import java.util.List;

import it.mapsgroup.gzoom.mybatis.dto.PartyNoteEx;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PartyNoteMapper {

    @Delete({
        "delete from party_note",
        "where party_id = #{partyId,jdbcType=VARCHAR}",
          "and note_id = #{noteId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("partyId") String partyId, @Param("noteId") String noteId);


    @Insert({
        "insert into party_note (party_id, note_id, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login)",
        "values (#{partyId,jdbcType=VARCHAR}, #{noteId,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(PartyNote row);


    @Select({
        "select",
        "party_id, note_id, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, last_modified_by_user_login, created_by_user_login",
        "from party_note",
        "where party_id = #{partyId,jdbcType=VARCHAR}",
          "and note_id = #{noteId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="note_id", property="noteId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    PartyNote selectByPrimaryKey(@Param("partyId") String partyId, @Param("noteId") String noteId);


    @Select({
        "select",
        "party_id, note_id, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, last_modified_by_user_login, created_by_user_login",
        "from party_note"
    })
    @Results({
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="note_id", property="noteId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    List<PartyNote> selectAll();

    List<PartyNoteEx> getPartyNote(@Param("partyId") String partyId, @Param("noteName") String noteName);

    @Update({
        "update party_note",
        "set last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where party_id = #{partyId,jdbcType=VARCHAR}",
          "and note_id = #{noteId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(PartyNote row);
}