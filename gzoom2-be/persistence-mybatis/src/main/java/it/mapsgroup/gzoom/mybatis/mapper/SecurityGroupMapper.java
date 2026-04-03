package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.SecurityGroup;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SecurityGroupMapper {
    @Delete({
        "delete from security_group",
        "where group_id = #{groupId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String groupId);

    @Insert({
        "insert into security_group (group_id, description, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "default_portal_page_id, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{groupId,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{defaultPortalPageId,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(SecurityGroup row);

    @Select({
        "select",
        "group_id, description, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, default_portal_page_id, last_modified_by_user_login, created_by_user_login",
        "from security_group",
        "where group_id = #{groupId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="default_portal_page_id", property="defaultPortalPageId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    SecurityGroup selectByPrimaryKey(String groupId);

    @Select({
        "select",
        "group_id, description, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, default_portal_page_id, last_modified_by_user_login, created_by_user_login",
        "from security_group",
            "order by group_id"
    })
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="default_portal_page_id", property="defaultPortalPageId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    List<SecurityGroup> selectAllOrderByPrimaryKey();

    @Update({
        "update security_group",
        "set description = #{description,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "default_portal_page_id = #{defaultPortalPageId,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where group_id = #{groupId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(SecurityGroup row);
}