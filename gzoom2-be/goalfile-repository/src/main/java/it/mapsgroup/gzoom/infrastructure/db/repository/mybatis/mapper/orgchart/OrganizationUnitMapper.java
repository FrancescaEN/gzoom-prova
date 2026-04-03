package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.orgchart;

import it.mapsgroup.gzoom.common.LanguageType;
import it.mapsgroup.gzoom.entity.goalfile.model.ShowUOCode;
import it.mapsgroup.gzoom.infrastructure.orgchart.dto.PartyDto;
import it.mapsgroup.gzoom.entity.user.model.PermissionView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Mapper
@Repository
public interface OrganizationUnitMapper {

    List<PartyDto> selectOrganizationUnitsAndTypes(String workEffortTypeId,
                                                   PermissionView permissionView,
                                                   Instant refDate,
                                                   String organizationId,
                                                   ShowUOCode showUoCode,
                                                   LanguageType languageType,
                                                   Boolean secondaryLang);

    List<PartyDto> selectOrganizationUnits(PermissionView permissionView,
                                           String organizationId,
                                           String roleTypeId,
                                           Instant refDate,
                                           Integer startYear,
                                           Integer endYear,
                                           ShowUOCode showUoCode,
                                           LanguageType languageType,
                                           Boolean secondaryLang);

    @Select(value = {"""
        <script>
            SELECT
            UO.PARTY_ID
            , UOC.PARENT_ROLE_CODE
            , UO.EXTERNAL_ID
            , UO.PARTY_NAME
            , UO.PARTY_NAME_LANG
            FROM PARTY_PARENT_ROLE UOC 
            INNER JOIN PARTY UO ON UO.PARTY_ID = UOC.PARTY_ID
            INNER JOIN PARTY_RELATIONSHIP SUPUO ON SUPUO.PARTY_RELATIONSHIP_TYPE_ID = 'GROUP_ROLLUP' AND SUPUO.PARTY_ID_FROM = UO.PARTY_ID
            WHERE UOC.ROLE_TYPE_ID = 'ORGANIZATION_UNIT' 
              AND UOC.ORGANIZATION_ID = #{organizationId, mode=IN, jdbcType=VARCHAR}
            GROUP BY UO.PARTY_ID, UOC.PARENT_ROLE_CODE, UO.EXTERNAL_ID, UO.PARTY_NAME, UO.PARTY_NAME_LANG
            <choose>
                <when test='showUoCode == null || showUoCode.name == "NONE"'>
                ORDER BY <choose><when test='languageType != null &amp;&amp; languageType.name()=="BILING" &amp;&amp; secondaryLang'>UO.PARTY_NAME_LANG</when><otherwise>UO.PARTY_NAME</otherwise></choose>
                </when>
                <when test='showUoCode.name == "MAIN"'>
                ORDER BY UOC.PARENT_ROLE_CODE
                </when>
                <when test='showUoCode.name == "EXT"'>
                ORDER BY UO.EXTERNAL_ID
                </when>
            </choose>
        </script>
    """}, databaseId = "")
    @ResultType(PartyDto.class)
    List<PartyDto> selectSupervisorOrganizationUnits(String organizationId, ShowUOCode showUoCode, LanguageType languageType, Boolean secondaryLang);
}