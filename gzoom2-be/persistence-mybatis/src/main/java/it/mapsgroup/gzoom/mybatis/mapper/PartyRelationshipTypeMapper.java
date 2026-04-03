package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.PartyRelationshipType;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PartyRelationshipTypeMapper {

    @Delete({
        "delete from party_relationship_type",
        "where party_relationship_type_id = #{partyRelationshipTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String partyRelationshipTypeId);


    @Insert({
        "insert into party_relationship_type (party_relationship_type_id, parent_type_id, ",
        "has_table, party_relationship_name, ",
        "description, role_type_id_valid_from, ",
        "role_type_id_valid_to, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp, explicit_role, ",
        "icon_content_id, last_modified_by_user_login, ",
        "created_by_user_login)",
        "values (#{partyRelationshipTypeId,jdbcType=VARCHAR}, #{parentTypeId,jdbcType=VARCHAR}, ",
        "#{hasTable,jdbcType=CHAR}, #{partyRelationshipName,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{roleTypeIdValidFrom,jdbcType=VARCHAR}, ",
        "#{roleTypeIdValidTo,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP}, #{explicitRole,jdbcType=CHAR}, ",
        "#{iconContentId,jdbcType=VARCHAR}, #{lastModifiedByUserLogin,jdbcType=VARCHAR}, ",
        "#{createdByUserLogin,jdbcType=VARCHAR})"
    })
    int insert(PartyRelationshipType row);


    @Select({
        "select",
        "party_relationship_type_id, parent_type_id, has_table, party_relationship_name, ",
        "description, role_type_id_valid_from, role_type_id_valid_to, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, explicit_role, icon_content_id, ",
        "last_modified_by_user_login, created_by_user_login",
        "from party_relationship_type",
        "where party_relationship_type_id = #{partyRelationshipTypeId,jdbcType=VARCHAR}"
    })
    @Results(id = "partyRelationshipType", value = {
        @Result(column="party_relationship_type_id", property="partyRelationshipTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="parent_type_id", property="parentTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="has_table", property="hasTable", jdbcType=JdbcType.CHAR),
        @Result(column="party_relationship_name", property="partyRelationshipName", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id_valid_from", property="roleTypeIdValidFrom", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id_valid_to", property="roleTypeIdValidTo", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="explicit_role", property="explicitRole", jdbcType=JdbcType.CHAR),
        @Result(column="icon_content_id", property="iconContentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_modified_by_user_login", property="lastModifiedByUserLogin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_by_user_login", property="createdByUserLogin", jdbcType=JdbcType.VARCHAR)
    })
    PartyRelationshipType selectByPrimaryKey(String partyRelationshipTypeId);


    @Select({
        "select",
        "party_relationship_type_id, parent_type_id, has_table, party_relationship_name, ",
        "description, role_type_id_valid_from, role_type_id_valid_to, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp, explicit_role, icon_content_id, ",
        "last_modified_by_user_login, created_by_user_login",
        "from party_relationship_type",
            "order by party_relationship_type_id"
    })
    @ResultMap("partyRelationshipType")
    List<PartyRelationshipType> selectAllOrderById();


    @Update({
        "update party_relationship_type",
        "set parent_type_id = #{parentTypeId,jdbcType=VARCHAR},",
          "has_table = #{hasTable,jdbcType=CHAR},",
          "party_relationship_name = #{partyRelationshipName,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "role_type_id_valid_from = #{roleTypeIdValidFrom,jdbcType=VARCHAR},",
          "role_type_id_valid_to = #{roleTypeIdValidTo,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP},",
          "explicit_role = #{explicitRole,jdbcType=CHAR},",
          "icon_content_id = #{iconContentId,jdbcType=VARCHAR},",
          "last_modified_by_user_login = #{lastModifiedByUserLogin,jdbcType=VARCHAR}",
        "where party_relationship_type_id = #{partyRelationshipTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(PartyRelationshipType row);
}