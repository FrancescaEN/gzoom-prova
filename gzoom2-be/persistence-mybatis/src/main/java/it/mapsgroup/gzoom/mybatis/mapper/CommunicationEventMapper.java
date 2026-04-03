package it.mapsgroup.gzoom.mybatis.mapper;

import it.mapsgroup.gzoom.mybatis.dto.CommunicationEvent;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommunicationEventMapper {

    @Delete({
        "delete from communication_event",
        "where communication_event_id = #{communicationEventId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String communicationEventId);


    @Insert({
        "insert into communication_event (communication_event_id, communication_event_type_id, ",
        "orig_comm_event_id, parent_comm_event_id, ",
        "status_id, contact_mech_type_id, ",
        "contact_mech_id_from, contact_mech_id_to, ",
        "role_type_id_from, role_type_id_to, ",
        "party_id_from, party_id_to, ",
        "entry_date, datetime_started, ",
        "datetime_ended, subject, ",
        "content_mime_type_id, content, ",
        "note, reason_enum_id, ",
        "contact_list_id, header_string, ",
        "from_string, to_string, ",
        "cc_string, bcc_string, ",
        "message_id, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, ",
        "created_tx_stamp)",
        "values (#{communicationEventId,jdbcType=VARCHAR}, #{communicationEventTypeId,jdbcType=VARCHAR}, ",
        "#{origCommEventId,jdbcType=VARCHAR}, #{parentCommEventId,jdbcType=VARCHAR}, ",
        "#{statusId,jdbcType=VARCHAR}, #{contactMechTypeId,jdbcType=VARCHAR}, ",
        "#{contactMechIdFrom,jdbcType=VARCHAR}, #{contactMechIdTo,jdbcType=VARCHAR}, ",
        "#{roleTypeIdFrom,jdbcType=VARCHAR}, #{roleTypeIdTo,jdbcType=VARCHAR}, ",
        "#{partyIdFrom,jdbcType=VARCHAR}, #{partyIdTo,jdbcType=VARCHAR}, ",
        "#{entryDate,jdbcType=TIMESTAMP}, #{datetimeStarted,jdbcType=TIMESTAMP}, ",
        "#{datetimeEnded,jdbcType=TIMESTAMP}, #{subject,jdbcType=VARCHAR}, ",
        "#{contentMimeTypeId,jdbcType=VARCHAR}, #{content,jdbcType=VARCHAR}, ",
        "#{note,jdbcType=VARCHAR}, #{reasonEnumId,jdbcType=VARCHAR}, ",
        "#{contactListId,jdbcType=VARCHAR}, #{headerString,jdbcType=VARCHAR}, ",
        "#{fromString,jdbcType=VARCHAR}, #{toString,jdbcType=VARCHAR}, ",
        "#{ccString,jdbcType=VARCHAR}, #{bccString,jdbcType=VARCHAR}, ",
        "#{messageId,jdbcType=VARCHAR}, #{lastUpdatedStamp,jdbcType=TIMESTAMP}, ",
        "#{lastUpdatedTxStamp,jdbcType=TIMESTAMP}, #{createdStamp,jdbcType=TIMESTAMP}, ",
        "#{createdTxStamp,jdbcType=TIMESTAMP})"
    })
    int insert(CommunicationEvent row);


    @Select({
        "select",
        "communication_event_id, communication_event_type_id, orig_comm_event_id, parent_comm_event_id, ",
        "status_id, contact_mech_type_id, contact_mech_id_from, contact_mech_id_to, role_type_id_from, ",
        "role_type_id_to, party_id_from, party_id_to, entry_date, datetime_started, datetime_ended, ",
        "subject, content_mime_type_id, content, note, reason_enum_id, contact_list_id, ",
        "header_string, from_string, to_string, cc_string, bcc_string, message_id, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from communication_event",
        "where communication_event_id = #{communicationEventId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="communication_event_id", property="communicationEventId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="communication_event_type_id", property="communicationEventTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="orig_comm_event_id", property="origCommEventId", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_comm_event_id", property="parentCommEventId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_id", property="statusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="contact_mech_type_id", property="contactMechTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="contact_mech_id_from", property="contactMechIdFrom", jdbcType=JdbcType.VARCHAR),
        @Result(column="contact_mech_id_to", property="contactMechIdTo", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id_from", property="roleTypeIdFrom", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id_to", property="roleTypeIdTo", jdbcType=JdbcType.VARCHAR),
        @Result(column="party_id_from", property="partyIdFrom", jdbcType=JdbcType.VARCHAR),
        @Result(column="party_id_to", property="partyIdTo", jdbcType=JdbcType.VARCHAR),
        @Result(column="entry_date", property="entryDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="datetime_started", property="datetimeStarted", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="datetime_ended", property="datetimeEnded", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="subject", property="subject", jdbcType=JdbcType.VARCHAR),
        @Result(column="content_mime_type_id", property="contentMimeTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="note", property="note", jdbcType=JdbcType.VARCHAR),
        @Result(column="reason_enum_id", property="reasonEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="contact_list_id", property="contactListId", jdbcType=JdbcType.VARCHAR),
        @Result(column="header_string", property="headerString", jdbcType=JdbcType.VARCHAR),
        @Result(column="from_string", property="fromString", jdbcType=JdbcType.VARCHAR),
        @Result(column="to_string", property="toString", jdbcType=JdbcType.VARCHAR),
        @Result(column="cc_string", property="ccString", jdbcType=JdbcType.VARCHAR),
        @Result(column="bcc_string", property="bccString", jdbcType=JdbcType.VARCHAR),
        @Result(column="message_id", property="messageId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    CommunicationEvent selectByPrimaryKey(String communicationEventId);


    @Select({
        "select",
        "communication_event_id, communication_event_type_id, orig_comm_event_id, parent_comm_event_id, ",
        "status_id, contact_mech_type_id, contact_mech_id_from, contact_mech_id_to, role_type_id_from, ",
        "role_type_id_to, party_id_from, party_id_to, entry_date, datetime_started, datetime_ended, ",
        "subject, content_mime_type_id, content, note, reason_enum_id, contact_list_id, ",
        "header_string, from_string, to_string, cc_string, bcc_string, message_id, last_updated_stamp, ",
        "last_updated_tx_stamp, created_stamp, created_tx_stamp",
        "from communication_event"
    })
    @Results({
        @Result(column="communication_event_id", property="communicationEventId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="communication_event_type_id", property="communicationEventTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="orig_comm_event_id", property="origCommEventId", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_comm_event_id", property="parentCommEventId", jdbcType=JdbcType.VARCHAR),
        @Result(column="status_id", property="statusId", jdbcType=JdbcType.VARCHAR),
        @Result(column="contact_mech_type_id", property="contactMechTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="contact_mech_id_from", property="contactMechIdFrom", jdbcType=JdbcType.VARCHAR),
        @Result(column="contact_mech_id_to", property="contactMechIdTo", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id_from", property="roleTypeIdFrom", jdbcType=JdbcType.VARCHAR),
        @Result(column="role_type_id_to", property="roleTypeIdTo", jdbcType=JdbcType.VARCHAR),
        @Result(column="party_id_from", property="partyIdFrom", jdbcType=JdbcType.VARCHAR),
        @Result(column="party_id_to", property="partyIdTo", jdbcType=JdbcType.VARCHAR),
        @Result(column="entry_date", property="entryDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="datetime_started", property="datetimeStarted", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="datetime_ended", property="datetimeEnded", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="subject", property="subject", jdbcType=JdbcType.VARCHAR),
        @Result(column="content_mime_type_id", property="contentMimeTypeId", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="note", property="note", jdbcType=JdbcType.VARCHAR),
        @Result(column="reason_enum_id", property="reasonEnumId", jdbcType=JdbcType.VARCHAR),
        @Result(column="contact_list_id", property="contactListId", jdbcType=JdbcType.VARCHAR),
        @Result(column="header_string", property="headerString", jdbcType=JdbcType.VARCHAR),
        @Result(column="from_string", property="fromString", jdbcType=JdbcType.VARCHAR),
        @Result(column="to_string", property="toString", jdbcType=JdbcType.VARCHAR),
        @Result(column="cc_string", property="ccString", jdbcType=JdbcType.VARCHAR),
        @Result(column="bcc_string", property="bccString", jdbcType=JdbcType.VARCHAR),
        @Result(column="message_id", property="messageId", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_updated_stamp", property="lastUpdatedStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_updated_tx_stamp", property="lastUpdatedTxStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_stamp", property="createdStamp", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="created_tx_stamp", property="createdTxStamp", jdbcType=JdbcType.TIMESTAMP)
    })
    List<CommunicationEvent> selectAll();


    @Update({
        "update communication_event",
        "set communication_event_type_id = #{communicationEventTypeId,jdbcType=VARCHAR},",
          "orig_comm_event_id = #{origCommEventId,jdbcType=VARCHAR},",
          "parent_comm_event_id = #{parentCommEventId,jdbcType=VARCHAR},",
          "status_id = #{statusId,jdbcType=VARCHAR},",
          "contact_mech_type_id = #{contactMechTypeId,jdbcType=VARCHAR},",
          "contact_mech_id_from = #{contactMechIdFrom,jdbcType=VARCHAR},",
          "contact_mech_id_to = #{contactMechIdTo,jdbcType=VARCHAR},",
          "role_type_id_from = #{roleTypeIdFrom,jdbcType=VARCHAR},",
          "role_type_id_to = #{roleTypeIdTo,jdbcType=VARCHAR},",
          "party_id_from = #{partyIdFrom,jdbcType=VARCHAR},",
          "party_id_to = #{partyIdTo,jdbcType=VARCHAR},",
          "entry_date = #{entryDate,jdbcType=TIMESTAMP},",
          "datetime_started = #{datetimeStarted,jdbcType=TIMESTAMP},",
          "datetime_ended = #{datetimeEnded,jdbcType=TIMESTAMP},",
          "subject = #{subject,jdbcType=VARCHAR},",
          "content_mime_type_id = #{contentMimeTypeId,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=VARCHAR},",
          "note = #{note,jdbcType=VARCHAR},",
          "reason_enum_id = #{reasonEnumId,jdbcType=VARCHAR},",
          "contact_list_id = #{contactListId,jdbcType=VARCHAR},",
          "header_string = #{headerString,jdbcType=VARCHAR},",
          "from_string = #{fromString,jdbcType=VARCHAR},",
          "to_string = #{toString,jdbcType=VARCHAR},",
          "cc_string = #{ccString,jdbcType=VARCHAR},",
          "bcc_string = #{bccString,jdbcType=VARCHAR},",
          "message_id = #{messageId,jdbcType=VARCHAR},",
          "last_updated_stamp = #{lastUpdatedStamp,jdbcType=TIMESTAMP},",
          "last_updated_tx_stamp = #{lastUpdatedTxStamp,jdbcType=TIMESTAMP}",
        "where communication_event_id = #{communicationEventId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(CommunicationEvent row);
}