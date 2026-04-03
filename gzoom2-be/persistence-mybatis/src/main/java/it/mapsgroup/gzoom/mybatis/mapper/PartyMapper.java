package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface PartyMapper {

    @Delete({
        "delete from party",
        "where party_id = #{partyId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String partyId);


    @Insert({
        "insert into party (party_id, party_type_id, ",
        "external_id, preferred_currency_uom_id, ",
        "description, status_id, ",
        "created_date, created_by_user_login, ",
        "last_modified_date, last_modified_by_user_login, ",
        "data_source_id, is_unread, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp, ",
        "fiscal_code, vat_code, ",
        "party_name, description_lang, ",
        "end_date, party_name_lang)",
        "values (#{partyId,jdbcType=VARCHAR}, #{partyTypeId,jdbcType=VARCHAR}, ",
        "#{externalId,jdbcType=VARCHAR}, #{preferredCurrencyUomId,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{statusId,jdbcType=VARCHAR}, ",
        "#{createdDate,jdbcType=TIMESTAMP}, #{createdByUserLogin,jdbcType=VARCHAR}, ",
        "#{lastModifiedDate,jdbcType=TIMESTAMP}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{dataSourceId,jdbcType=VARCHAR}, #{isUnread,jdbcType=CHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP}, ",
        "#{fiscalCode,jdbcType=VARCHAR}, #{vatCode,jdbcType=VARCHAR}, ",
        "#{partyName,jdbcType=VARCHAR}, #{descriptionLang,jdbcType=VARCHAR}, ",
        "#{endDate,jdbcType=TIMESTAMP}, #{partyNameLang,jdbcType=VARCHAR})"
    })
    int insert(Party row);


    @Select({
        "select",
        "party_id, party_type_id, external_id, preferred_currency_uom_id, description, ",
        "status_id, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, ",
        "data_source_id, is_unread, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, fiscal_code, vat_code, party_name, description_lang, end_date, ",
        "party_name_lang",
        "from party",
        "where party_id = #{partyId,jdbcType=VARCHAR}"
    })
    @Results(id = "party" , value = {
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="party_type_id", property="partyTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="external_id", property="externalId", jdbcType=JdbcType.VARCHAR),
        @Result(column="preferred_currency_uom_id", property="preferredCurrencyUomId", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_id", property="statusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_date", property="createdDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_date", property="lastModifiedDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="data_source_id", property="dataSourceId", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_unread", property="isUnread", jdbcType=JdbcType.CHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="fiscal_code", property="fiscalCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="vat_code", property="vatCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="party_name", property="partyName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description_lang", property="descriptionLang", jdbcType=JdbcType.VARCHAR),
        @Result(column="end_date", property="endDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="party_name_lang", property="partyNameLang", jdbcType=JdbcType.VARCHAR)
    })
    Party selectByPrimaryKey(String partyId);


    @Select({
        "select",
        "party_id, party_type_id, external_id, preferred_currency_uom_id, description, ",
        "status_id, created_date, created_by_user_login, last_modified_date, last_modified_by_user_login, ",
        "data_source_id, is_unread, last_updated_stamp, last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, fiscal_code, vat_code, party_name, description_lang, end_date, ",
        "party_name_lang",
        "from party"
    })
    @ResultMap("party")
    List<Party> selectAll();

    @Select({
            "select pa.*",
            "from party pa",
            "join party_role pr on pr.party_id = pa.party_id",
            "and pr.role_type_id = #{roleTypeId}"
    })
    @ResultMap("party")
    List<Party> selectByRoleTypeId(String roleTypeId);

    @Select({
            "select pa.*",
            "from party pa",
            "join party_role pr on pr.party_id = pa.party_id",
            "and pr.role_type_id = #{roleTypeId}",
            "where pa.party_id not in (" +
                    " select gao.organization_party_id from gl_account_organization gao " +
                    "where gao.gl_account_id = #{glAccountId} )"
    })
    @ResultMap("party")
    List<Party> selectByRoleTypeIdAndNotInGlAccount(String roleTypeId, String glAccountId);

    @Select({
            "select ",
            "pa.party_id, concat(pp.parent_role_code, concat(' - ', pa.party_name)) as party_name, concat(pp.parent_role_code, concat(' - ', pa.party_name_lang)) as party_name_lang",
            "from party pa",
            "inner join party_parent_role pp on pp.party_id = pa.party_id",
            " and pp.role_type_id = #{roleTypeId,jdbcType=VARCHAR} and pp.organization_id = #{organizationId,jdbcType=VARCHAR}"
    })
    @ResultType(Party.class)
    List<Party> getPartiesByRoleTypeIdAndOrganizationId(String roleTypeId,  String organizationId);

    List<Party> getParties(@Param("userLoginId") String userLoginId, @Param("parentTypeId") String parentTypeId, @Param("filter") Map<String, Object> filter);

    List<PersonEx> getPartiesExposed();

    List<Party> getRoleTypePartys(@Param("roleTypeId") String roleTypeId, @Param("roleTypeIdFrom") String roleTypeIdFrom, @Param("roleTypeIdFromArray") String[] roleTypeIdFromArray, @Param("workEffortTypeId") String workEffortTypeId, @Param("checkPartyRole") boolean checkPartyRole);

    List<PartyRole> checkPartyRole(String workEffortTypeId);

    List<Party> getRoleTypePartysBetween(@Param("roleTypeId0") String roleTypeId0, @Param("roleTypeId1") String roleTypeId1);

    List<PartyEx> getOrgUnits (@Param("userLoginId") String userLoginId, @Param("parentTypeId") String parentTypeId, @Param("filter") Map<String, Object> filterPermission, @Param("roleTypeIdFromArray") String[] roleType, @Param("workEffortTypeId") String workEffortTypeId, @Param("company") String company, @Param("isLanguageLang") boolean isLanguageLang, @Param("checkPartyRole") boolean checkPartyRole, @Param("orderUoBy") String orderUoBy);

    List<Party> getPartyByOrganizationId(@Param("organizationId") String organizationId);

    List<PartyEx> getUOGestore (String organizationId, String userLoginId, boolean isFullAdmin);

    List<PartyEx> getUOGestoreByRespCenterRoleTypeId (String organizationId, String respCenterRoleTypeId, String userLoginId, boolean isFullAdmin);

    int anonymizeParty(Instant expirationDate);

    @ResultType(Party.class)
    List<Party> getManagers();


    @Select({
            "select p2.party_name as schoolName, p3.party_name usrName",
            "from user_login ul",
            "join party_relationship pr on pr.party_id_to = ul.party_id and pr.party_relationship_type_id = 'ORG_RESPONSIBLE'",
            "join party p2 on p2.party_id = pr.party_id_from",
            "join party_relationship pr2 on pr2.party_id_to = p2.party_id and pr2.party_relationship_type_id = 'GROUP_ROLLUP'",
            "join party_relationship pr3 on pr3.party_id_to = pr2.party_id_from and pr3.party_relationship_type_id = 'GROUP_ROLLUP'",
            "join party p3 on p3.party_id = pr3.party_id_from",
            "where ul.user_login_id = #{userLoginId,jdbcType=VARCHAR}"
    })
    @Results(id = "orgAndGroupResult", value = {
            @Result(column="schoolName", property="schoolName", jdbcType=JdbcType.VARCHAR),
            @Result(column="usrName", property="usrName", jdbcType=JdbcType.VARCHAR)
    })
    HeaderPortalPage getHeaderPortalPage(@Param("userLoginId") String userLoginId);

}