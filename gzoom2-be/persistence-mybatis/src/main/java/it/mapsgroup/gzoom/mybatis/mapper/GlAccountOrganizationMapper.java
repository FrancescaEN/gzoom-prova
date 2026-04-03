package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.GlAccountOrganization;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GlAccountOrganizationMapper {
    @Delete({
        "delete from gl_account_organization",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}",
          "and organization_party_id = #{organizationPartyId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("glAccountId") String glAccountId, @Param("organizationPartyId") String organizationPartyId);

    @Delete({
            "delete from gl_account_organization",
            "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}"
    })
    int deleteByGlAccountId(@Param("glAccountId") String glAccountId);

    @Insert({
        "insert into gl_account_organization (gl_account_id, organization_party_id, ",
        "role_type_id, from_date, ",
        "thru_date, posted_balance, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login)",
        "values (#{glAccountId,jdbcType=VARCHAR}, #{organizationPartyId,jdbcType=VARCHAR}, ",
        "#{roleTypeId,jdbcType=VARCHAR}, #{fromDate,jdbcType=TIMESTAMP}, ",
        "#{thruDate,jdbcType=TIMESTAMP}, #{postedBalance,jdbcType=NUMERIC}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(GlAccountOrganization row);

    @Select({
        "select",
        "gl_account_id, organization_party_id, role_type_id, from_date, thru_date, posted_balance, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login",
        "from gl_account_organization",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}",
          "and organization_party_id = #{organizationPartyId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="organization_party_id", property="organizationPartyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="posted_balance", property="postedBalance", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    GlAccountOrganization selectByPrimaryKey(@Param("glAccountId") String glAccountId, @Param("organizationPartyId") String organizationPartyId);

    @Select({
        "select",
        "gl_account_id, organization_party_id, role_type_id, from_date, thru_date, posted_balance, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp, ",
        "last_modified_by_user_login, created_by_user_login",
        "from gl_account_organization"
    })
    @Results({
        @Result(column="gl_account_id", property="glAccountId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="organization_party_id", property="organizationPartyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="from_date", property="fromDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="thru_date", property="thruDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="posted_balance", property="postedBalance", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    List<GlAccountOrganization> selectAll();

    @Select({
            "select *",
            "from gl_account_organization",
            "where gl_Account_id = #{glAccountId}"
    })
    @ResultType(GlAccountOrganization.class)
    List<GlAccountOrganization> selectByGlAccountId(String glAccountId);

    @Update({
        "update gl_account_organization",
        "set role_type_id = #{roleTypeId,jdbcType=VARCHAR},",
          "from_date = #{fromDate,jdbcType=TIMESTAMP},",
          "thru_date = #{thruDate,jdbcType=TIMESTAMP},",
          "posted_balance = #{postedBalance,jdbcType=NUMERIC},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where gl_account_id = #{glAccountId,jdbcType=VARCHAR}",
          "and organization_party_id = #{organizationPartyId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(GlAccountOrganization row);
}