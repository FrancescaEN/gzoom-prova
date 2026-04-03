package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountRole;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import it.mapsgroup.gzoom.mybatis.dto.Party;
import it.mapsgroup.gzoom.mybatis.dto.PartyParentRole;
import it.mapsgroup.gzoom.mybatis.dto.RoleType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GlAccountRoleMapper {
    @Delete({
        "delete from gl_account_role",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}",
          "and party_id = #{partyId,jdbcType=VARCHAR}",
          "and role_type_id = #{roleTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int deleteByPrimaryKey(@Param("glAccountId") String glAccountId, @Param("partyId") String partyId, @Param("roleTypeId") String roleTypeId, @Param("fromDate") Instant fromDate);


    @Delete({
            "delete from gl_account_role",
            "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    int deleteByGlAccountId(@Param("glAccountId") String glAccountId);

    @Insert({
        "insert into gl_account_role (gl_account_id, party_id, ",
        "role_type_id, from_date, ",
        "thru_date, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{glAccountId,jdbcType=VARCHAR}, #{partyId,jdbcType=VARCHAR}, ",
        "#{roleTypeId,jdbcType=VARCHAR}, #{fromDate,jdbcType=TIMESTAMP}, ",
        "#{thruDate,jdbcType=TIMESTAMP}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(GlAccountRole row);

    @Select({
        "select",
        "gl_account_id, party_id, role_type_id, from_date, thru_date, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login",
        "from gl_account_role",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}",
          "and party_id = #{partyId,jdbcType=VARCHAR}",
          "and role_type_id = #{roleTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    @Results({
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    GlAccountRole selectByPrimaryKey(@Param("glAccountId") String glAccountId, @Param("partyId") String partyId, @Param("roleTypeId") String roleTypeId, @Param("fromDate") LocalDateTime fromDate);

    @Select({
        "select",
        "gl_account_id, party_id, role_type_id, from_date, thru_date, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, last_modified_by_user_login, ",
        "created_by_user_login",
        "from gl_account_role"
    })
    @Results({
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP, id=true),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    List<GlAccountRole> selectAll();

    @Select({
            "select *",
            "from gl_account_role",
            "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    @ResultType(GlAccountRole.class)
    List<GlAccountRole> selectByGlAccountId(String glAccountId);

    List<GlAccountRole> getUoDetectedByGlAccountId(String glAccountId);

    @Update({
        "update gl_account_role",
        "set thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}",
          "and party_id = #{partyId,jdbcType=VARCHAR}",
          "and role_type_id = #{roleTypeId,jdbcType=VARCHAR}",
          "and from_date = #{fromDate,jdbcType=TIMESTAMP}"
    })
    int updateByPrimaryKey(GlAccountRole row);
}