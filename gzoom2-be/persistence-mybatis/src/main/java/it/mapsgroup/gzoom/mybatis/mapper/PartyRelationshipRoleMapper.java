package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.PartyRelationshipRole;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PartyRelationshipRoleMapper {

    @Delete({
        "delete from party_relationship_role",
        "where party_relationship_type_id = #{partyRelationshipTypeId,jdbcType=VARCHAR}",
          "and role_type_valid_from = #{roleTypeValidFrom,jdbcType=VARCHAR}",
          "and role_type_valid_to = #{roleTypeValidTo,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("partyRelationshipTypeId") String partyRelationshipTypeId, @Param("roleTypeValidFrom") String roleTypeValidFrom, @Param("roleTypeValidTo") String roleTypeValidTo);

    @Delete({
            "delete from party_relationship_role",
            "where party_relationship_type_id = #{partyRelationshipTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPartyRelationshipTypeId(@Param("partyRelationshipTypeId") String partyRelationshipTypeId);

    @Insert({
        "insert into party_relationship_role (party_relationship_type_id, role_type_valid_from, ",
        "role_type_valid_to, informative_sequence, ",
        "last_modified_by_user_login, created_by_user_login, ",
        "last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp)",
        "values (#{partyRelationshipTypeId,jdbcType=VARCHAR}, #{roleTypeValidFrom,jdbcType=VARCHAR}, ",
        "#{roleTypeValidTo,jdbcType=VARCHAR}, #{informativeSequence,jdbcType=NUMERIC}, ",
        "#{lastModifiedByUserLogin,jdbcType=VARCHAR}, #{createdByUserLogin,jdbcType=VARCHAR}, ",
        "#{lastUpdatedStamp,jdbcType=TIMESTAMP}, #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, ",
        "#{createdStamp,jdbcType=TIMESTAMP}, #{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(PartyRelationshipRole row);


    @Select({
        "select",
        "party_relationship_type_id, role_type_valid_from, role_type_valid_to, informative_sequence, ",
        "last_modified_by_user_login, created_by_user_login, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from party_relationship_role",
        "where party_relationship_type_id = #{partyRelationshipTypeId,jdbcType=VARCHAR}",
          "and role_type_valid_from = #{roleTypeValidFrom,jdbcType=VARCHAR}",
          "and role_type_valid_to = #{roleTypeValidTo,jdbcType=VARCHAR}"
    })
    @Results(id = "partyRelationshipRole", value = {
        @Result(column="party_relationship_type_id", property="partyRelationshipTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="role_type_valid_from", property="roleTypeValidFrom", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="role_type_valid_to", property="roleTypeValidTo", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="informative_sequence", property="informativeSequence", jdbcType=JdbcType.NUMERIC),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    PartyRelationshipRole selectByPrimaryKey(@Param("partyRelationshipTypeId") String partyRelationshipTypeId, @Param("roleTypeValidFrom") String roleTypeValidFrom, @Param("roleTypeValidTo") String roleTypeValidTo);


    @Select({
        "select",
        "party_relationship_type_id, role_type_valid_from, role_type_valid_to, informative_sequence, ",
        "last_modified_by_user_login, created_by_user_login, last_updated_stamp, last_updated_tx_stamp, ",
        "created_stamp, created_tx_stamp",
        "from party_relationship_role",
            "where party_relationship_type_id = #{partyRelationshipTypeId,jdbcType=VARCHAR}",
    })
    @ResultMap("partyRelationshipRole")
    List<PartyRelationshipRole> selectByPartyRelationshipTypeId(@Param("partyRelationshipTypeId") String partyRelationshipTypeId);


    @Update({
        "update party_relationship_role",
        "set informative_sequence = #{informativeSequence,jdbcType=NUMERIC},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where party_relationship_type_id = #{partyRelationshipTypeId,jdbcType=VARCHAR}",
          "and role_type_valid_from = #{roleTypeValidFrom,jdbcType=VARCHAR}",
          "and role_type_valid_to = #{roleTypeValidTo,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(PartyRelationshipRole row);
}