package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile;

import it.mapsgroup.gzoom.infrastructure.content.dto.ContentDto;
import it.mapsgroup.gzoom.infrastructure.content.dto.DataResourceDto;
import it.mapsgroup.gzoom.infrastructure.content.dto.FileExtensionDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortContentDetailDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortContentDto;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


@Mapper
@Repository
public interface GoalContentMapper {
    List<WorkEffortContentDetailDto> selectWorkEffortContentDetailListFromWorkEffortId(String workEffortId,
                                                                                       Instant fromDate,
                                                                                       Instant thruDate);

    @Select(value = {"""
        <script>
        SELECT DATA_RESOURCE_TYPE_ID
        , DATA_TEMPLATE_TYPE_ID
        , STATUS_ID
        , DATA_RESOURCE_NAME
        , MIME_TYPE_ID
        , OBJECT_INFO
        , IS_PUBLIC
        FROM DATA_RESOURCE
        WHERE DATA_RESOURCE_ID = #{dataResourceId, mode=IN, jdbcType=VARCHAR}
        </script>
    """})
    @ResultType(DataResourceDto.class)
    DataResourceDto selectDataResourceFromDataResourceId(String dataResourceId);

    FileExtensionDto selectMimeTypeIdByFileExtensionId(String fileExtensionId);

    @Insert(value = {"""
        <script>
    		insert into work_effort_content (work_effort_id
            , content_id
            , work_effort_content_type_id
            , from_date
            , thru_date
            , created_stamp
            , created_tx_stamp
            , last_updated_stamp
            , last_updated_tx_stamp
            , created_by_user_login
            , last_modified_by_user_login)
            values (#{workEffortContentDto.workEffortId,jdbcType=VARCHAR}
            , #{workEffortContentDto.contentId,jdbcType=VARCHAR}
            , #{workEffortContentDto.workEffortContentTypeId,jdbcType=VARCHAR}
            , #{workEffortContentDto.fromDate,jdbcType=TIMESTAMP}
            , #{workEffortContentDto.thruDate,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{userLoginId,jdbcType=VARCHAR}
            , #{userLoginId,jdbcType=VARCHAR})
        </script>
    """})
    int insertWorkEffortContent(WorkEffortContentDto workEffortContentDto, String userLoginId, Instant instant);

    @Insert(value = {"""
        <script>
    		insert into content (CONTENT_ID,
                CONTENT_TYPE_ID,
                DATA_RESOURCE_ID,
                STATUS_ID,
                CONTENT_NAME,
                DESCRIPTION,
                DESCRIPTION_LANG,
                MIME_TYPE_ID,
                CREATED_DATE,
                LAST_MODIFIED_DATE,
                LAST_UPDATED_STAMP,
                LAST_UPDATED_TX_STAMP,
                CREATED_STAMP,
                CREATED_TX_STAMP,
                CREATED_BY_USER_LOGIN,
                LAST_MODIFIED_BY_USER_LOGIN)
            values (#{contentDto.contentId,jdbcType=VARCHAR}
            , #{contentDto.contentTypeId,jdbcType=VARCHAR}
            , #{contentDto.dataResourceId,jdbcType=VARCHAR}
            , #{contentDto.statusId,jdbcType=VARCHAR}
            , #{contentDto.contentName,jdbcType=VARCHAR}
            , #{contentDto.description,jdbcType=VARCHAR}
            , #{contentDto.descriptionLang,jdbcType=VARCHAR}
            , #{contentDto.mimeTypeId,jdbcType=VARCHAR}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{userLoginId,jdbcType=VARCHAR}
            , #{userLoginId,jdbcType=VARCHAR})
        </script>
    """})
    int insertContent(ContentDto contentDto, String userLoginId, Instant instant);

    @Insert(value = {"""
        <script>
    		insert into data_resource (DATA_RESOURCE_ID,
                DATA_RESOURCE_TYPE_ID,
                DATA_TEMPLATE_TYPE_ID,
                STATUS_ID,
                DATA_RESOURCE_NAME,
                MIME_TYPE_ID,
                OBJECT_INFO,
                IS_PUBLIC,
                CREATED_DATE,
                LAST_MODIFIED_DATE,
                LAST_UPDATED_STAMP,
                LAST_UPDATED_TX_STAMP,
                CREATED_STAMP,
                CREATED_TX_STAMP,
                CREATED_BY_USER_LOGIN,
                LAST_MODIFIED_BY_USER_LOGIN)
            values (#{dataResourceDto.dataResourceId,jdbcType=VARCHAR}
            , #{dataResourceDto.dataResourceTypeId,jdbcType=VARCHAR}
            , #{dataResourceDto.dataTemplateTypeId,jdbcType=VARCHAR}
            , #{dataResourceDto.statusId,jdbcType=VARCHAR}
            , #{dataResourceDto.dataResourceName,jdbcType=VARCHAR}
            , #{dataResourceDto.mimeTypeId,jdbcType=VARCHAR}
            , #{dataResourceDto.objectInfo,jdbcType=VARCHAR}
            , #{dataResourceDto.isPublic,jdbcType=VARCHAR}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{instant,jdbcType=TIMESTAMP}
            , #{userLoginId,jdbcType=VARCHAR}
            , #{userLoginId,jdbcType=VARCHAR})
        </script>
    """})
    int insertDataResource(DataResourceDto dataResourceDto, String userLoginId, Instant instant);


    @Update({"""
        <script>
        UPDATE WORK_EFFORT_CONTENT
        SET LAST_UPDATED_STAMP = #{instant,jdbcType=TIMESTAMP}
        , LAST_UPDATED_TX_STAMP = #{instant,jdbcType=TIMESTAMP}
        , LAST_MODIFIED_BY_USER_LOGIN = #{userLoginId,jdbcType=VARCHAR}
        <if test='workEffortContentDto.fromDate != null'>
        , FROM_DATE = #{workEffortContentDto.fromDate,jdbcType=TIMESTAMP}
        </if>
        <if test='workEffortContentDto.thruDate != null'>
        , THRU_DATE = #{workEffortContentDto.thruDate,jdbcType=TIMESTAMP}
        </if>
        <if test='workEffortContentDto.workEffortContentTypeId != null'>
        , WORK_EFFORT_CONTENT_TYPE_ID = #{workEffortContentDto.workEffortContentTypeId, mode=IN, jdbcType=VARCHAR}
        </if>
        WHERE CONTENT_ID = #{workEffortContentDto.contentId, mode=IN, jdbcType=VARCHAR}
        </script>
    """})
    int updateWorkEffortContent(WorkEffortContentDto workEffortContentDto, String userLoginId, Instant instant);

    @Update({"""
        <script>
        UPDATE CONTENT
        SET LAST_UPDATED_STAMP = #{instant,jdbcType=TIMESTAMP}
        , LAST_UPDATED_TX_STAMP = #{instant,jdbcType=TIMESTAMP}
        , LAST_MODIFIED_BY_USER_LOGIN = #{userLoginId,jdbcType=VARCHAR}
        <if test='contentDto.contentTypeId != null'>
        , CONTENT_TYPE_ID = #{contentDto.contentTypeId, mode=IN, jdbcType=VARCHAR}
        </if>
        <if test='contentDto.contentName != null'>
        , CONTENT_NAME = #{contentDto.contentName, mode=IN, jdbcType=VARCHAR}
        </if>
        <if test='contentDto.description != null'>
        , DESCRIPTION = #{contentDto.description, mode=IN, jdbcType=VARCHAR}
        </if>        <if test='contentDto.descriptionLang != null'>
        , DESCRIPTION_LANG = #{contentDto.descriptionLang, mode=IN, jdbcType=VARCHAR}
        </if>
        WHERE CONTENT_ID = #{contentDto.contentId, mode=IN, jdbcType=VARCHAR}
        </script>
    """})
    int updateContent(ContentDto contentDto, String userLoginId, Instant instant);

    @Update({"""
        <script>
        UPDATE DATA_RESOURCE
        SET LAST_UPDATED_STAMP = #{instant,jdbcType=TIMESTAMP}
        , LAST_UPDATED_TX_STAMP = #{instant,jdbcType=TIMESTAMP}
        , LAST_MODIFIED_BY_USER_LOGIN = #{userLoginId,jdbcType=VARCHAR}
        <if test='dataResourceDto.objectInfo != null'>
        , OBJECT_INFO = #{dataResourceDto.objectInfo, mode=IN, jdbcType=VARCHAR}
        </if>
        <if test='dataResourceDto.dataResourceName != null'>
        , DATA_RESOURCE_NAME = #{dataResourceDto.dataResourceName, mode=IN, jdbcType=VARCHAR}
        </if>
        WHERE DATA_RESOURCE_ID = #{dataResourceDto.dataResourceId, mode=IN, jdbcType=VARCHAR}
        </script>
    """})
    int updateDataResource(DataResourceDto dataResourceDto, String userLoginId, Instant instant);

    @Delete({"""
        <script>
        DELETE FROM WORK_EFFORT_CONTENT WHERE CONTENT_ID = #{contentId, mode=IN, jdbcType=VARCHAR};
        </script>
    """})
    int deleteWorkEffortContent(String contentId);

    @Delete({"""
        <script>
        DELETE FROM CONTENT WHERE CONTENT_ID = #{contentId, mode=IN, jdbcType=VARCHAR};
        </script>
    """})
    int deleteContent(String contentId);

    @Delete({"""
        <script>
        DELETE FROM DATA_RESOURCE WHERE DATA_RESOURCE_ID = #{dataResourceId, mode=IN, jdbcType=VARCHAR};
        </script>
    """})
    int deleteDataResource(String dataResourceId);
}