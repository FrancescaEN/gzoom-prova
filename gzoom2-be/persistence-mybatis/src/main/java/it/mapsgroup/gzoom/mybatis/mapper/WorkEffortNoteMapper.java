package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortNote;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortNoteExNoteData;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WorkEffortNoteMapper {

    @Delete({
        "delete from work_effort_note",
        "where work_effort_id = #{workEffortId,jdbcType=VARCHAR}",
          "and note_id = #{noteId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("workEffortId") String workEffortId, @Param("noteId") String noteId);


    @Insert({
        "insert into work_effort_note (work_effort_id, note_id, ",
        "internal_note, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, is_main, ",
        "is_html, sequence_id, ",
        "is_posted, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{workEffortId,jdbcType=VARCHAR}, #{noteId,jdbcType=VARCHAR}, ",
        "#{internalNote,jdbcType=CHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{isMain,jdbcType=CHAR}, ",
        "#{isHtml,jdbcType=CHAR}, #{sequenceId,jdbcType=NUMERIC}, ",
        "#{isPosted,jdbcType=CHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(WorkEffortNote row);


    @Select({
        "select",
        "work_effort_id, note_id, internal_note, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, is_main, is_html, sequence_id, is_posted, last_modified_by_user_login, ",
        "created_by_user_login",
        "from work_effort_note",
        "where work_effort_id = #{workEffortId,jdbcType=VARCHAR}",
          "and note_id = #{noteId,jdbcType=VARCHAR}"
    })
    @Results(id = "workEffortNote", value = {
        @Result(column="work_effort_id", property="workEffortId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="note_id", property="noteId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="internal_note", property="internalNote", jdbcType=JdbcType.CHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="is_main", property="isMain", jdbcType=JdbcType.CHAR),
        @Result(column="is_html", property="isHtml", jdbcType=JdbcType.CHAR),
        @Result(column="sequence_id", property="sequenceId", jdbcType=JdbcType.NUMERIC),
        @Result(column="is_posted", property="isPosted", jdbcType=JdbcType.CHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    WorkEffortNote selectByPrimaryKey(@Param("workEffortId") String workEffortId, @Param("noteId") String noteId);

    @Select({
            "select *",
            "from work_effort_note wen",
            "where wen.work_effort_id = #{workEffortId}"
    })
    @ResultMap("workEffortNote")
    List<WorkEffortNote> getWorkEffortNoteByWorkEffortId(@Param("workEffortId") String workEffortId);

    @Select({
        "select",
        "work_effort_id, note_id, internal_note, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, is_main, is_html, sequence_id, is_posted, last_modified_by_user_login, ",
        "created_by_user_login",
        "from work_effort_note"
    })
    @ResultMap("workEffortNote")
    List<WorkEffortNote> selectAll();

    @ResultType(int.class)
    int getTotale(@Param("LIMIT") Integer limit, @Param("OFFSET") Integer offset, @Param("filters") Filter[] filters, @Param("filterGenericLabel") Filter[] filterGenericLabel, @Param("SORTORDER") Integer sortOrder, @Param("SORTFIELD") String sortField,  @Param("matchModeSearch") String matchModeSearch, @Param("SECONDARYLANGUAGE") Boolean secondaryLanguage);


    WorkEffortNoteExNoteData getWorkEffortNoteExNoteData(@Param("workEffortId") String workEffortId, @Param("noteId") String noteId, @Param("noteName") String noteName);


    List<WorkEffortNoteExNoteData> getWorkEffortNoteExNoteDataList(@Param("LIMIT") Integer limit, @Param("OFFSET") Integer offset, @Param("filters") Filter[] filters, @Param("filterGenericLabel") Filter[] filterGenericLabel, @Param("SORTORDER") Integer sortOrder, @Param("SORTFIELD") String sortField,  @Param("matchModeSearch") String matchModeSearch, @Param("SECONDARYLANGUAGE") Boolean secondaryLanguage);


    @Update({
        "update work_effort_note",
        "set internal_note = #{internalNote,jdbcType=CHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "is_main = #{isMain,jdbcType=CHAR},",
          "is_html = #{isHtml,jdbcType=CHAR},",
          "sequence_id = #{sequenceId,jdbcType=NUMERIC},",
          "is_posted = #{isPosted,jdbcType=CHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where work_effort_id = #{workEffortId,jdbcType=VARCHAR}",
          "and note_id = #{noteId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(WorkEffortNote row);
}