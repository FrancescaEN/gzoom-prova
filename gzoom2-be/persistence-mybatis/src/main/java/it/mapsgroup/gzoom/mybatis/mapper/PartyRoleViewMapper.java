package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.PartyRoleView;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PartyRoleViewMapper {

    @Select({
        "select",
        "parent_role_type_id, parent_description, parent_short_label, parent_role_code, ",
        "party_id, role_type_id, role_description, short_label, party_name, empl_position_type_id, ",
        "status_id, organization_id",
        "from party_role_view"
    })
    @Results(id = "partyRoleView", value = {
        @Result(column="parent_role_type_id", property="parentRoleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_description", property="parentDescription", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_short_label", property="parentShortLabel", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_role_code", property="parentRoleCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_description", property="roleDescription", jdbcType=JdbcType.VARCHAR),
        @Result(column="short_label", property="shortLabel", jdbcType=JdbcType.VARCHAR),
        @Result(column="party_name", property="partyName", jdbcType=JdbcType.VARCHAR),
        @Result(column="empl_position_type_id", property="emplPositionTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_id", property="statusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="organization_id", property="organizationId", jdbcType=JdbcType.VARCHAR)
    })
    List<PartyRoleView> selectAll();

    @Select({
            "select",
            "parent_role_type_id, parent_description, parent_short_label, parent_role_code, ",
            "party_id, role_type_id, role_description, short_label, party_name, empl_position_type_id, ",
            "status_id, organization_id",
            "from party_role_view",
            "where status_id = #{statusId,jdbcType=VARCHAR} and organization_id = #{organizationId,jdbcType=VARCHAR}",
            "order by party_name"
    })
    @ResultMap("partyRoleView")
    List<PartyRoleView> selectByStatusIdAndOrganizationIdOrderByPartyName(@Param("statusId") String statusId, @Param("organizationId") String organizationId);

    @Select({
            "select",
            "parent_role_type_id, parent_description, parent_short_label, parent_role_code, ",
            "party_id, role_type_id, role_description, short_label, party_name, empl_position_type_id, ",
            "status_id, organization_id",
            "from party_role_view",
            "where status_id = #{statusId,jdbcType=VARCHAR} and organization_id = #{organizationId,jdbcType=VARCHAR} and role_type_id = #{roleTypeId,jdbcType=VARCHAR}",
            "order by party_name"
    })
    @ResultMap("partyRoleView")
    List<PartyRoleView> selectByStatusIdAndOrganizationIdAndRoleTypeIdOrderByPartyName(@Param("statusId") String statusId, @Param("organizationId") String organizationId, @Param("roleTypeId") String roleTypeId);

    @Select({
            "select",
            "party_id, party_name ",
            "from party_role_view",
            "where role_type_id = #{roleTypeId,jdbcType=VARCHAR}",
            "order by party_name"
    })
    @ResultMap("partyRoleView")
    List<PartyRoleView> selectByRoleTypeId(String roleTypeId);

}