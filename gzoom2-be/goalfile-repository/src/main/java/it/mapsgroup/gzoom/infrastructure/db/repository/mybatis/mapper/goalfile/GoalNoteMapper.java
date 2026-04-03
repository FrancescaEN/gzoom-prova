package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile;


import it.mapsgroup.gzoom.infrastructure.goalfile.dto.NoteDataDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortNoteDto;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Mapper
@Repository
public interface GoalNoteMapper {
    List<WorkEffortNoteDto> selectWorkEffortNotesFromWorkEffortId(String workEffortId,
                                                                  String nameSearch,
                                                                  String contentId,
                                                                  Boolean secondaryLang);

    @Insert(value = {"""
        <script>
    		insert into work_effort_note (work_effort_id
            , note_id
            , internal_note
            , created_stamp
            , created_tx_stamp
            , last_updated_stamp
            , last_updated_tx_stamp
            , is_main
            , is_html
            , is_posted
            , sequence_id
            , created_by_user_login)
            values (#{workEffortNoteDto.workEffortId,jdbcType=VARCHAR}
            , #{workEffortNoteDto.noteId,jdbcType=VARCHAR}
            , #{workEffortNoteDto.internalNote,jdbcType=VARCHAR, typeHandler=it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.typehandler.YesNoBooleanTypeHandler}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{workEffortNoteDto.main,jdbcType=VARCHAR, typeHandler=it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.typehandler.YesNoBooleanTypeHandler}
            , #{workEffortNoteDto.html,jdbcType=VARCHAR, typeHandler=it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.typehandler.YesNoBooleanTypeHandler}
            , #{workEffortNoteDto.posted,jdbcType=VARCHAR, typeHandler=it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.typehandler.YesNoBooleanTypeHandler}
            , #{workEffortNoteDto.sequenceId,jdbcType=NUMERIC}
            , #{userLoginId,jdbcType=VARCHAR})
        </script>
    """})
    int insertWorkEffortNote(WorkEffortNoteDto workEffortNoteDto, String userLoginId, Instant instant);


    @Insert(value = {"""
        <script>
            insert into note_data (note_id
            , note_name
            , note_name_lang
            , note_info
            , note_info_lang
            , created_stamp
            , created_tx_stamp
            , last_updated_stamp
            , last_updated_tx_stamp
            , note_date_time
            , created_by_user_login
            , note_party
            , is_public)
            values (#{noteDataDto.noteId,jdbcType=VARCHAR}
            , #{noteDataDto.noteName,jdbcType=VARCHAR}
            , #{noteDataDto.noteNameLang,jdbcType=VARCHAR}
            , #{noteDataDto.noteInfo,jdbcType=VARCHAR}
            , #{noteDataDto.noteInfoLang,jdbcType=VARCHAR}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{noteDataDto.noteDateTime,jdbcType=TIMESTAMP}
            , #{userLoginId,jdbcType=VARCHAR}
            , #{noteDataDto.noteParty,jdbcType=VARCHAR}
            , #{noteDataDto.publicNote,jdbcType=VARCHAR, typeHandler=it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.typehandler.YesNoBooleanTypeHandler})
        </script>
    """})
    int insertNoteData(NoteDataDto noteDataDto, String userLoginId, Instant instant);


    @Update({"""
        <script>
        UPDATE WORK_EFFORT_NOTE
        SET LAST_UPDATED_STAMP = #{instant,jdbcType=TIMESTAMP}
        , LAST_UPDATED_TX_STAMP = #{instant,jdbcType=TIMESTAMP}
        , LAST_MODIFIED_BY_USER_LOGIN = #{userLoginId,jdbcType=VARCHAR}
        <if test='workEffortNoteDto.posted != null'>
        , IS_POSTED = #{workEffortNoteDto.posted,jdbcType=VARCHAR, typeHandler=it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.typehandler.YesNoBooleanTypeHandler}
        </if>
        <if test='workEffortNoteDto.sequenceId != null &amp;&amp; workEffortNoteDto.sequenceId != ""'>
        , SEQUENCE_ID = #{workEffortNoteDto.sequenceId, jdbcType=NUMERIC}
        </if>
        WHERE NOTE_ID = #{workEffortNoteDto.noteId, mode=IN, jdbcType=VARCHAR}
        </script>
    """})
    int updateWorkEffortNote(WorkEffortNoteDto workEffortNoteDto, String userLoginId, Instant instant);

    @Update({"""
        <script>
        UPDATE NOTE_DATA
        SET LAST_UPDATED_STAMP = #{instant,jdbcType=TIMESTAMP}
        , LAST_UPDATED_TX_STAMP = #{instant,jdbcType=TIMESTAMP}
        , LAST_MODIFIED_BY_USER_LOGIN = #{userLoginId,jdbcType=VARCHAR}
        <if test='noteDataDto.noteName != null'>
        , NOTE_NAME = #{noteDataDto.noteName, mode=IN, jdbcType=VARCHAR}
        </if>
        <if test='noteDataDto.noteNameLang != null'>
        , NOTE_NAME_LANG = #{noteDataDto.noteNameLang, mode=IN, jdbcType=VARCHAR}
        </if>
        <if test='noteDataDto.noteInfo != null'>
        , NOTE_INFO = #{noteDataDto.noteInfo, mode=IN, jdbcType=VARCHAR}
        </if>
        <if test='noteDataDto.noteInfoLang != null'>
        , NOTE_INFO_LANG = #{noteDataDto.noteInfoLang, mode=IN, jdbcType=VARCHAR}
        </if>
        <if test='noteDataDto.noteDateTime != null'>
        , NOTE_DATE_TIME = #{noteDataDto.noteDateTime,jdbcType=TIMESTAMP}
        </if>
        <if test='noteDataDto.noteParty != null'>
        , NOTE_PARTY = #{noteDataDto.noteParty, mode=IN, jdbcType=VARCHAR}
        </if>
        WHERE NOTE_ID = #{noteDataDto.noteId, mode=IN, jdbcType=VARCHAR}
        </script>
    """})
    int updateNoteData(NoteDataDto noteDataDto, String userLoginId, Instant instant);


    @Delete({"""
        <script>
        DELETE FROM WORK_EFFORT_NOTE WHERE NOTE_ID = #{noteId, mode=IN, jdbcType=VARCHAR};
        </script>
    """})
    int deleteWorkEffortNote(String noteId);

    @Delete({"""
        <script>
        DELETE FROM NOTE_DATA WHERE NOTE_ID = #{noteId, mode=IN, jdbcType=VARCHAR};
        </script>
    """})
    int deleteNoteData(String noteId);
}