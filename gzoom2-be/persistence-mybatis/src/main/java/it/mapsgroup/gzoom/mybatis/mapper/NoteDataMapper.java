package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.NoteData;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NoteDataMapper {

    @Delete({
        "delete from note_data",
        "where note_id = #{noteId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String noteId);


    @Insert({
        "insert into note_data (note_id, note_name, ",
        "note_info, note_date_time, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "note_party, more_info_portlet_id, ",
        "more_info_item_id, is_public, ",
        "note_name_lang, note_info_lang, ",
        "last_modified_by_user_login, created_by_user_login)",
        "values (#{noteId,jdbcType=VARCHAR}, #{noteName,jdbcType=VARCHAR}, ",
        "#{noteInfo,jdbcType=VARCHAR}, #{noteDateTime,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{noteParty,jdbcType=VARCHAR}, #{moreInfoPortletId,jdbcType=VARCHAR}, ",
        "#{moreInfoItemId,jdbcType=VARCHAR}, #{isPublic,jdbcType=CHAR}, ",
        "#{noteNameLang,jdbcType=VARCHAR}, #{noteInfoLang,jdbcType=VARCHAR}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(NoteData row);


    @Select({
        "select",
        "note_id, note_name, note_info, note_date_time, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, note_party, more_info_portlet_id, more_info_item_id, ",
        "is_public, note_name_lang, note_info_lang, last_modified_by_user_login, created_by_user_login",
        "from note_data",
        "where note_id = #{noteId,jdbcType=VARCHAR}"
    })
    @Results(id = "noteData", value = {
        @Result(column="note_id", property="noteId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="note_name", property="noteName", jdbcType=JdbcType.VARCHAR),
        @Result(column="note_info", property="noteInfo", jdbcType=JdbcType.VARCHAR),
        @Result(column="note_date_time", property="noteDateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="note_party", property="noteParty", jdbcType=JdbcType.VARCHAR),
        @Result(column="more_info_portlet_id", property="moreInfoPortletId", jdbcType=JdbcType.VARCHAR),
        @Result(column="more_info_item_id", property="moreInfoItemId", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_public", property="isPublic", jdbcType=JdbcType.CHAR),
        @Result(column="note_name_lang", property="noteNameLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="note_info_lang", property="noteInfoLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    NoteData selectByPrimaryKey(String noteId);


    @Select({
        "select",
        "note_id, note_name, note_info, note_date_time, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, note_party, more_info_portlet_id, more_info_item_id, ",
        "is_public, note_name_lang, note_info_lang, last_modified_by_user_login, created_by_user_login",
        "from note_data"
    })
    @ResultMap("noteData")
    List<NoteData> selectAll();


    @Update({
        "update note_data",
        "set note_name = #{noteName,jdbcType=VARCHAR},",
          "note_info = #{noteInfo,jdbcType=VARCHAR},",
          "note_date_time = #{noteDateTime,jdbcType=TIMESTAMP},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "note_party = #{noteParty,jdbcType=VARCHAR},",
          "more_info_portlet_id = #{moreInfoPortletId,jdbcType=VARCHAR},",
          "more_info_item_id = #{moreInfoItemId,jdbcType=VARCHAR},",
          "is_public = #{isPublic,jdbcType=CHAR},",
          "note_name_lang = #{noteNameLang,jdbcType=VARCHAR},",
          "note_info_lang = #{noteInfoLang,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where note_id = #{noteId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(NoteData row);

    List<NoteData> getNoteDataParamsTimesheet(@Param("userLoginId") String userLoginId);

    NoteData getNoteDataByPrefValue(@Param("prefValue") String prefValue);
}