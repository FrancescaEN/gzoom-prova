package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.CommunicationEventRole;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommunicationEventRoleMapper {
    @Delete({
        "delete from communication_event_role",
        "where communication_event_id = #{communicationEventId,jdbcType=VARCHAR}",
          "and party_id = #{partyId,jdbcType=VARCHAR}",
          "and role_type_id = #{roleTypeId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("communicationEventId") String communicationEventId, @Param("partyId") String partyId, @Param("roleTypeId") String roleTypeId);

    @Insert({
        "insert into communication_event_role (communication_event_id, party_id, ",
        "role_type_id, contact_mech_id, ",
        "status_id, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{communicationEventId,jdbcType=VARCHAR}, #{partyId,jdbcType=VARCHAR}, ",
        "#{roleTypeId,jdbcType=VARCHAR}, #{contactMechId,jdbcType=VARCHAR}, ",
        "#{statusId,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(CommunicationEventRole row);

    @Select({
        "select",
        "communication_event_id, party_id, role_type_id, contact_mech_id, status_id, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from communication_event_role",
        "where communication_event_id = #{communicationEventId,jdbcType=VARCHAR}",
          "and party_id = #{partyId,jdbcType=VARCHAR}",
          "and role_type_id = #{roleTypeId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="communication_event_id", property="communicationEventId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="contact_mech_id", property="contactMechId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_id", property="statusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    CommunicationEventRole selectByPrimaryKey(@Param("communicationEventId") String communicationEventId, @Param("partyId") String partyId, @Param("roleTypeId") String roleTypeId);

    @Select({
        "select",
        "communication_event_id, party_id, role_type_id, contact_mech_id, status_id, ",
        "last_updated_stamp, last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from communication_event_role"
    })
    @Results({
        @Result(column="communication_event_id", property="communicationEventId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="party_id", property="partyId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="role_type_id", property="roleTypeId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="contact_mech_id", property="contactMechId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_id", property="statusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    List<CommunicationEventRole> selectAll();

    @Update({
        "update communication_event_role",
        "set contact_mech_id = #{contactMechId,jdbcType=VARCHAR},",
          "status_id = #{statusId,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where communication_event_id = #{communicationEventId,jdbcType=VARCHAR}",
          "and party_id = #{partyId,jdbcType=VARCHAR}",
          "and role_type_id = #{roleTypeId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(CommunicationEventRole row);
}