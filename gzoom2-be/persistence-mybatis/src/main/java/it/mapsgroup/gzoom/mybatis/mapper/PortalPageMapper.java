package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.PortalPage;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PortalPageMapper {
    @Delete({
        "delete from portal_page",
        "where portal_page_id = #{portalPageId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String portalPageId);

    @Insert({
        "insert into portal_page (portal_page_id, portal_page_name, ",
        "description, owner_user_login_id, ",
        "original_portal_page_id, parent_portal_page_id, ",
        "sequence_num, security_group_id, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "help_content_id)",
        "values (#{portalPageId,jdbcType=VARCHAR}, #{portalPageName,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{ownerUserLoginId,jdbcType=VARCHAR}, ",
        "#{originalPortalPageId,jdbcType=VARCHAR}, #{parentPortalPageId,jdbcType=VARCHAR}, ",
        "#{sequenceNum,jdbcType=NUMERIC}, #{securityGroupId,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{helpContentId,jdbcType=VARCHAR})"
    })
    int insert(PortalPage row);

    @Select({
        "select",
        "portal_page_id, portal_page_name, description, owner_user_login_id, original_portal_page_id, ",
        "parent_portal_page_id, sequence_num, security_group_id, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, help_content_id",
        "from portal_page",
        "where portal_page_id = #{portalPageId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="portal_page_id", property="portalPageId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="portal_page_name", property="portalPageName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="owner_user_login_id", property="ownerUserLoginId", jdbcType=JdbcType.VARCHAR),
        @Result(column="original_portal_page_id", property="originalPortalPageId", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_portal_page_id", property="parentPortalPageId", jdbcType=JdbcType.VARCHAR),
        @Result(column="sequence_num", property="sequenceNum", jdbcType=JdbcType.NUMERIC),
        @Result(column="security_group_id", property="securityGroupId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="help_content_id", property="helpContentId", jdbcType=JdbcType.VARCHAR)
    })
    PortalPage selectByPrimaryKey(String portalPageId);

    @Select({
        "select",
        "portal_page_id, portal_page_name, description, owner_user_login_id, original_portal_page_id, ",
        "parent_portal_page_id, sequence_num, security_group_id, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, help_content_id",
        "from portal_page"
    })
    @Results({
        @Result(column="portal_page_id", property="portalPageId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="portal_page_name", property="portalPageName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="owner_user_login_id", property="ownerUserLoginId", jdbcType=JdbcType.VARCHAR),
        @Result(column="original_portal_page_id", property="originalPortalPageId", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_portal_page_id", property="parentPortalPageId", jdbcType=JdbcType.VARCHAR),
        @Result(column="sequence_num", property="sequenceNum", jdbcType=JdbcType.NUMERIC),
        @Result(column="security_group_id", property="securityGroupId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="help_content_id", property="helpContentId", jdbcType=JdbcType.VARCHAR)
    })
    List<PortalPage> selectAll();

    @Select({
            "select * ",
            "from portal_page",
            "where parent_portal_page_id = #{parentPortalPageId,jdbcType=VARCHAR}"
    })
    @ResultType(PortalPage.class)
    List<PortalPage> selectByParentPortalPageId(String parentPortalPageId);

    @Update({
        "update portal_page",
        "set portal_page_name = #{portalPageName,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "owner_user_login_id = #{ownerUserLoginId,jdbcType=VARCHAR},",
          "original_portal_page_id = #{originalPortalPageId,jdbcType=VARCHAR},",
          "parent_portal_page_id = #{parentPortalPageId,jdbcType=VARCHAR},",
          "sequence_num = #{sequenceNum,jdbcType=NUMERIC},",
          "security_group_id = #{securityGroupId,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "created_stamp = #{createdStamp,jdbcType=TIMESTAMP},",
          "created_tx_stamp = #{createdTxStamp,jdbcType=TIMESTAMP},",
          "help_content_id = #{helpContentId,jdbcType=VARCHAR}",
        "where portal_page_id = #{portalPageId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(PortalPage row);
}